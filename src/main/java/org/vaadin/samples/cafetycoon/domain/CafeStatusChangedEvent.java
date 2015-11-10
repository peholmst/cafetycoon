package org.vaadin.samples.cafetycoon.domain;

public class CafeStatusChangedEvent extends BaseDomainEvent {

    private final Cafe cafe;
    private final CafeStatus newStatus;

    public CafeStatusChangedEvent(Cafe cafe, CafeStatus newStatus) {
        this.cafe = cafe;
        this.newStatus = newStatus;
    }

    public Cafe getCafe() {
        return cafe;
    }

    public CafeStatus getNewStatus() {
        return newStatus;
    }
}
