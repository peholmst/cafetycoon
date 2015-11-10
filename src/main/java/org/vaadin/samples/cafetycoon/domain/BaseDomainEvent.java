package org.vaadin.samples.cafetycoon.domain;

import java.time.Instant;

public abstract class BaseDomainEvent extends BaseEntity {

    private final Instant instant;

    protected BaseDomainEvent() {
        this.instant = Instant.now(ClockProvider.getClock());
    }

    protected BaseDomainEvent(long id, Instant instant) {
        super(id);
        this.instant = instant;
    }

    public Instant getInstant() {
        return instant;
    }
}
