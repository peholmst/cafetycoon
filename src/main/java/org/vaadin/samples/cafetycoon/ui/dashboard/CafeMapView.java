package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.Services;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.ui.CustomComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by petterwork on 10/11/15.
 */
public class CafeMapView extends CustomComponent {

    private final GoogleMap map;
    private final Map<Cafe, GoogleMapMarker> markers;

    public CafeMapView() {
        map = new GoogleMap(null, null, null);
        map.setSizeFull();
        setCompositionRoot(map);
        setSizeFull();

        markers = new HashMap<>();
    }

    private void refreshAll() {
        markers.values().forEach(map::removeMarker);
        markers.clear();
        for (Cafe cafe : Services.getInstance().getCafeRepository().getCafes()) {
            GoogleMapMarker marker = createMarker(cafe, CafeStatus.RUNNING); // TODO get status from business logic
            markers.put(cafe, marker);
            map.addMarker(marker);
        }
    }

    private GoogleMapMarker createMarker(Cafe cafe, CafeStatus status) {
        GoogleMapMarker marker = new GoogleMapMarker(cafe.getName(), cafe.getCoordinates(), false);
        // TODO icon
        return marker;
    }

    @Override
    public void attach() {
        super.attach();
        Services.getInstance().getEventBus().register(this);
        refreshAll();
    }

    @Override
    public void detach() {
        Services.getInstance().getEventBus().unregister(this);
        super.detach();
    }
}
