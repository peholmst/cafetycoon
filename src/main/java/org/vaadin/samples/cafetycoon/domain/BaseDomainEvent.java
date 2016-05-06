package org.vaadin.samples.cafetycoon.domain;

import java.time.Instant;

@SuppressWarnings("serial")
public abstract class BaseDomainEvent extends BaseEntity implements TemporalCache.Entry {

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

    @Override
    public String toString() {
        return String.format("BaseDomainEvent{id=%d, instant=%s}", getId(), instant);
    }
}
