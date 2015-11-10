package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StockService {

    private final Map<Cafe, BigDecimal> stock = new HashMap<>();
    private final EventBus eventBus;

    public StockService(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public synchronized BigDecimal getCurrentStock(Cafe cafe) {
        return stock.getOrDefault(cafe, BigDecimal.ZERO);
    }

    public synchronized void restock(Cafe cafe, BigDecimal beanUnits) {
        final RestockEvent event = new RestockEvent(cafe, beanUnits);
        BigDecimal currentStock = stock.getOrDefault(cafe, BigDecimal.ZERO);
        BigDecimal newStock = currentStock.add(beanUnits);
        stock.put(cafe, newStock);
        eventBus.post(event);
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent saleEvent) {
        Cafe cafe = saleEvent.getCafe();
        BigDecimal currentStock = stock.getOrDefault(cafe, BigDecimal.ZERO);
        BigDecimal newStock = currentStock.subtract(saleEvent.getConsumedBeanUnits());
        stock.put(cafe, newStock);
    }
}
