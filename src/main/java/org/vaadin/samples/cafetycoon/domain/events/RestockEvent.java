package org.vaadin.samples.cafetycoon.domain.events;

import org.vaadin.samples.cafetycoon.domain.BaseDomainEvent;
import org.vaadin.samples.cafetycoon.domain.Cafe;

import java.math.BigDecimal;

public class RestockEvent extends BaseDomainEvent {

    private final Cafe cafe;
    private final BigDecimal beanUnits;

    public RestockEvent(Cafe cafe, BigDecimal beanUnits) {
        this.cafe = cafe;
        this.beanUnits = beanUnits;
    }

    /**
     * The café that was restocked.
     */
    public Cafe getCafe() {
        return cafe;
    }

    /**
     * The amount of bean units that was added to the stock.
     */
    public BigDecimal getBeanUnits() {
        return beanUnits;
    }
}
