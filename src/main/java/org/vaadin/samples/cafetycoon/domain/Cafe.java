package org.vaadin.samples.cafetycoon.domain;

import com.vaadin.tapio.googlemaps.client.LatLon;

public class Cafe extends BaseEntity {

    private final String name;
    private final String address;
    private final LatLon coordinates;
    // TODO Coordinates

    public Cafe(String name, String address, LatLon coordinates) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLon getCoordinates() {
        return coordinates;
    }
}
