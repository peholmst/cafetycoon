package org.vaadin.samples.cafetycoon.domain.events;

import org.vaadin.samples.cafetycoon.domain.BaseDomainEvent;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;

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
