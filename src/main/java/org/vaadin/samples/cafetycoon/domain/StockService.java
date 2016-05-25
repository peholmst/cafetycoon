package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.vaadin.samples.cafetycoon.domain.events.RestockEvent;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;
import org.vaadin.samples.cafetycoon.domain.events.StockChangeEvent;

public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final Map<Cafe, BigDecimal> stock = new HashMap<>();
    private final EventBus eventBus;
    private final Map<Cafe, TemporalCache<StockChangeEvent>> stockChangeEvents24h = new HashMap<>();

    public StockService(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public synchronized BigDecimal getCurrentStock(Cafe cafe) {
        return stock.getOrDefault(cafe, BigDecimal.ZERO);
    }

    public synchronized void restock(Cafe cafe, BigDecimal beanUnits) {
        LOGGER.info("Restocking {} with {} bean units", cafe, beanUnits);
        final RestockEvent event = new RestockEvent(cafe, beanUnits);
        BigDecimal currentStock = stock.getOrDefault(cafe, BigDecimal.ZERO);
        BigDecimal newStock = currentStock.add(beanUnits);
        stock.put(cafe, newStock);
        getStockChangEventsCache(cafe).add(new StockChangeEvent(newStock, cafe));
        eventBus.post(event);
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent saleEvent) {
        Cafe cafe = saleEvent.getCafe();
        BigDecimal currentStock = stock.getOrDefault(cafe, BigDecimal.ZERO);
        BigDecimal newStock = currentStock.subtract(saleEvent.getConsumedBeanUnits());
        getStockChangEventsCache(cafe).add(new StockChangeEvent(newStock, cafe));
        stock.put(cafe, newStock);
        LOGGER.info("New stock of {} is {}", cafe, newStock);
    }

    public synchronized List<StockChangeEvent> get24hStockChangeEvents(Cafe cafe) {
        return getStockChangEventsCache(cafe).getEntries();
    }

    private TemporalCache<StockChangeEvent> getStockChangEventsCache(Cafe cafe) {
        TemporalCache<StockChangeEvent> cache = stockChangeEvents24h.get(cafe);
        if (cache == null) {
            cache = new TemporalCache<>();
            stockChangeEvents24h.put(cafe, cache);
        }
        return cache;
    }    
}
