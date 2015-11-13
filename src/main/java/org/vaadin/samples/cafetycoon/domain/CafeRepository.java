package org.vaadin.samples.cafetycoon.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.tapio.googlemaps.client.LatLon;

public class CafeRepository {

    private final List<Cafe> cafes;

    public CafeRepository() {
        List<Cafe> cafes = new ArrayList<>();

        cafes.add(new Cafe("San Mateo", "92 E 3rd Ave, San Mateo", new LatLon(37.563878, -122.324175)));
        cafes.add(new Cafe("Alameda", "Webster Square Shopping Center, 720 Atlantic Ave, Alameda",
            new LatLon(37.779561, -122.275648)));
        cafes.add(new Cafe("Berkeley", "2925 College Ave, Berkeley", new LatLon(37.857863, -122.252953)));
        cafes.add(new Cafe("San Lorenzo", "15600 Hesperian Blvd, San Lorenzo", new LatLon(37.686916, -122.129718)));
        cafes.add(new Cafe("Palo Alto", "2675 Middlefield Rd, Palo Alto", new LatLon(37.434028, -122.129547)));
        cafes.add(new Cafe("Union City", "1707 Decoto Rd, Union City", new LatLon(37.589401, -122.021768)));
        cafes.add(new Cafe("Alcatraz", "Alcatraz Island, San Fransisco", new LatLon(37.826978, -122.422955)));

        this.cafes = Collections.unmodifiableList(cafes);
    }

    public List<Cafe> getCafes() {
        return cafes;
    }
}
