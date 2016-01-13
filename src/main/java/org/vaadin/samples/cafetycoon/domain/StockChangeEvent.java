package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;

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
