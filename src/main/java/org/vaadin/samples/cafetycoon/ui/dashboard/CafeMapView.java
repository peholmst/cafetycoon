package org.vaadin.samples.cafetycoon.ui.dashboard;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.OverviewModel;

import com.vaadin.data.Property;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CustomComponent;
import org.vaadin.teemu.VaadinIcons;

public class CafeMapView extends CustomComponent {

    private GoogleMap map;
    private Map<Cafe, GoogleMapMarker> cafeToMarkerMap;
    private Map<GoogleMapMarker, Cafe> markerToCafeMap;
    private OverviewModel model;
    private CafeSelectionModel selectionModel;
    private PropertyChangeListener cafeStatusListener = this::cafeStatusUpdated;
    private Property.ValueChangeListener cafeSelectionListener = this::cafeSelectionChanged;

    public CafeMapView(OverviewModel model, CafeSelectionModel selectionModel) {
        addStyleName("cafe-map-view");
        map = new GoogleMap(null, null, null);
        map.addMarkerClickListener(this::markerClicked);
        map.setSizeFull();
        setCompositionRoot(map);
        setSizeFull();

        cafeToMarkerMap = new HashMap<>();
        markerToCafeMap = new HashMap<>();

        this.model = model;
        this.selectionModel = selectionModel;
    }

    private void refresh(Cafe cafe) {
        GoogleMapMarker marker = cafeToMarkerMap.remove(cafe);
        if (marker != null) {
            map.removeMarker(marker);
            markerToCafeMap.remove(marker);
        }
        Optional<OverviewModel.CafeStatusAndIncomeDTO> statusAndIncome = model.getCurrentStatusAndIncome(cafe);
        if (statusAndIncome.isPresent()) {
            marker = createMarker(cafe, statusAndIncome.get().getStatus());
            map.addMarker(marker);
            cafeToMarkerMap.put(cafe, marker);
            markerToCafeMap.put(marker, cafe);
        }
    }

    private void remove(Cafe cafe) {
        GoogleMapMarker marker = cafeToMarkerMap.remove(cafe);
        if (marker != null) {
            map.removeMarker(marker);
            markerToCafeMap.remove(marker);
        }
    }

    private void refreshAll() {
        cafeToMarkerMap.values().forEach(map::removeMarker);
        cafeToMarkerMap.clear();
        markerToCafeMap.clear();
        Services.getInstance().getCafeRepository().getCafes().forEach(this::refresh);
    }

    private GoogleMapMarker createMarker(Cafe cafe, CafeStatus status) {
        GoogleMapMarker marker = new GoogleMapMarker(cafe.getName(), cafe.getCoordinates(), false);
        switch (status) {
        case OUT_OF_STOCK:
            marker.setIconUrl("VAADIN/mapicons/out-of-stock.png");
            break;
        case RUNNING:
        case UNKNOWN:
            marker.setIconUrl("VAADIN/mapicons/running.png");
            break;
        case SERVING_COFFEE:
            marker.setIconUrl("VAADIN/mapicons/serving-coffee.png");
            break;
        }
        marker.setAnimationEnabled(false);
        return marker;
    }

    private void cafeStatusUpdated(PropertyChangeEvent event) {
        if (event instanceof IndexedPropertyChangeEvent) {
            // Only a single cafe has been updated
            if (event.getNewValue() == null) {
                OverviewModel.CafeStatusAndIncomeDTO dto = (OverviewModel.CafeStatusAndIncomeDTO) event.getOldValue();
                remove(dto.getCafe());
            } else {
                OverviewModel.CafeStatusAndIncomeDTO dto = (OverviewModel.CafeStatusAndIncomeDTO) event.getNewValue();
                refresh(dto.getCafe());
            }
        } else {
            // All cafes have been updated
            refreshAll();
        }
    }

    private void cafeSelectionChanged(Property.ValueChangeEvent event) {
        Cafe selectedCafe = selectionModel.getSelection().getValue();
        if (selectedCafe != null) {
            map.setCenter(selectedCafe.getCoordinates());
        }
    }

    private void markerClicked(GoogleMapMarker clickedMarker) {
        Cafe cafe = markerToCafeMap.get(clickedMarker);
        if (cafe != null) {
            selectionModel.getSelection().setValue(cafe);
        }
    }

    @Override
    public void attach() {
        super.attach();
        model.addPropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusListener);
        selectionModel.getSelection().addValueChangeListener(cafeSelectionListener);
        refreshAll();
    }

    @Override
    public void detach() {
        selectionModel.getSelection().removeValueChangeListener(cafeSelectionListener);
        model.removePropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusListener);
        super.detach();
    }
}
