package org.vaadin.samples.cafetycoon.domain.events;

import org.vaadin.samples.cafetycoon.domain.BaseDomainEvent;
import org.vaadin.samples.cafetycoon.domain.Cafe;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class StockChangeEvent extends BaseDomainEvent {

    private final BigDecimal currentStock;
    private final Cafe cafe;

    public StockChangeEvent(BigDecimal currentStock, Cafe cafe) {
        this.currentStock = currentStock;
        this.cafe = cafe;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public Cafe getCafe() {
        return cafe;
    }
}
