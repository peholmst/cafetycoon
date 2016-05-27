package org.vaadin.samples.cafetycoon.ui.dashboard.components;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CustomComponent;

/**
 * A custom component that shows the location and status of all the cafes on a
 * Google map. You can also select the current cafe by clicking on it on the
 * map. This component observes a {@link SalesOverviewModel} and a
 * {@link CafeSelectionModel}.
 */
@SuppressWarnings("serial")
public class CafeMap extends CustomComponent implements SalesOverviewModel.Observer, CafeSelectionModel.Observer {

	private GoogleMap map;
	private Set<Marker> markers;
	private SalesOverviewModel salesOverviewModel;
	private CafeSelectionModel cafeSelectionModel;

	public CafeMap() {
		map = new GoogleMap(null, null, null);
		map.addMarkerClickListener(this::markerClicked);
		map.setSizeFull();
		setCompositionRoot(map);
		setSizeFull();

		markers = new HashSet<>();
	}

	@Override
	public void setSalesOverviewModel(SalesOverviewModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up
		salesOverviewModel = Objects.requireNonNull(model);
		((ItemSetChangeNotifier) model.cafes()).addItemSetChangeListener(this::cafesChanged);
		refreshAll();
	}

	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up
		cafeSelectionModel = Objects.requireNonNull(model);
		model.selection().addValueChangeListener(this::cafeSelected);
	}

	private void cafesChanged(Container.ItemSetChangeEvent event) {
		refreshAll();
	}

	private void refreshAll() {
		markers.forEach(Marker::dispose);
		markers.clear();

		Indexed cafes = salesOverviewModel.cafes();
		for (int i = 0; i < cafes.size(); ++i) {
			Cafe cafe = (Cafe) cafes.getIdByIndex(i);
			Item cafeItem = cafes.getItem(cafe);
			markers.add(new Marker(cafe, cafeItem));
		}
	}

	private void markerClicked(GoogleMapMarker clickedMarker) {
		System.out.println("Marker clicked");
		findMarkerByGoogleMapMarker(clickedMarker)
				.ifPresent(marker -> cafeSelectionModel.selection().setValue(marker.getCafe()));
	}

	private void cafeSelected(Property.ValueChangeEvent event) {
		findMarkerByCafe(cafeSelectionModel.selection().getValue()).flatMap(Marker::getMarker)
				.ifPresent(marker -> map.setCenter(marker.getPosition()));
	}

	private Optional<Marker> findMarkerByCafe(Cafe cafe) {
		if (cafe == null) {
			return Optional.empty();
		}
		return markers.stream().filter(marker -> cafe.equals(marker.getCafe())).findFirst();
	}

	private Optional<Marker> findMarkerByGoogleMapMarker(GoogleMapMarker mapMarker) {
		if (mapMarker == null) {
			return Optional.empty();
		}
		return markers.stream()
				.filter(marker -> marker.getMarker().isPresent() && mapMarker.equals(marker.getMarker().get()))
				.findFirst();
	}

	class Marker implements Serializable {
		private Cafe cafe;
		private Item item;
		private GoogleMapMarker marker;
		private CafeStatus status;

		Marker(Cafe cafe, Item item) {
			this.cafe = Objects.requireNonNull(cafe);
			this.item = Objects.requireNonNull(item);
			status = (CafeStatus) item.getItemProperty(SalesOverviewModel.COL_STATUS).getValue();
			((Property.ValueChangeNotifier) item.getItemProperty(SalesOverviewModel.COL_STATUS))
					.addValueChangeListener(this::statusChanged);
			createOrReplaceMarker();
		}

		private void statusChanged(Property.ValueChangeEvent event) {
			status = (CafeStatus) event.getProperty().getValue();
			createOrReplaceMarker();
		}

		private void createOrReplaceMarker() {
			if (marker != null) {
				map.removeMarker(marker);
			}
			if (status != null) {
				marker = createMarker(cafe, status);
				map.addMarker(marker);
			}
		}

		Optional<GoogleMapMarker> getMarker() {
			return Optional.ofNullable(marker);
		}

		Cafe getCafe() {
			return cafe;
		}

		void dispose() {
			if (marker != null) {
				map.removeMarker(marker);
			}
			((Property.ValueChangeNotifier) item.getItemProperty(SalesOverviewModel.COL_STATUS))
					.removeValueChangeListener(this::statusChanged);
		}
	}

	private static GoogleMapMarker createMarker(Cafe cafe, CafeStatus status) {
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
}
