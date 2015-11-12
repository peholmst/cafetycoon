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
import org.vaadin.samples.cafetycoon.ui.dashboard.model.OverviewModel;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CustomComponent;

public class CafeMapView extends CustomComponent {

    private GoogleMap map;
    private Map<Cafe, GoogleMapMarker> markers;
    private OverviewModel model;
    private PropertyChangeListener cafeStatusUpdatedListener = this::cafeStatusUpdated;

    public CafeMapView(OverviewModel model) {
        map = new GoogleMap(null, null, null);
        map.setSizeFull();
        setCompositionRoot(map);
        setSizeFull();

        markers = new HashMap<>();

        this.model = model;
    }

    private void refresh(Cafe cafe) {
        GoogleMapMarker marker = markers.remove(cafe);
        if (marker != null) {
            map.removeMarker(marker);
        }
        Optional<OverviewModel.CafeStatusAndIncomeDTO> statusAndIncome = model.getCurrentStatusAndIncome(cafe);
        if (statusAndIncome.isPresent()) {
            marker = createMarker(cafe, statusAndIncome.get().getStatus());
            map.addMarker(marker);
            markers.put(cafe, marker);
        }
    }

    private void remove(Cafe cafe) {
        GoogleMapMarker marker = markers.get(cafe);
        if (marker != null) {
            map.removeMarker(marker);

        }
    }

    private void refreshAll() {
        markers.values().forEach(map::removeMarker);
        markers.clear();
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

    @Override
    public void attach() {
        super.attach();
        model.addPropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusUpdatedListener);
        refreshAll();
    }

    @Override
    public void detach() {
        model.removePropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusUpdatedListener);
        super.detach();
    }
}
