package org.vaadin.samples.cafetycoon.domain;

import com.google.common.eventbus.EventBus;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class SalesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesService.class);
    private final EventBus eventBus;
    private final StockService stockService;
    private final Map<Cafe, LinkedList<SaleEvent>> salesEvents24h = new HashMap<>();

    public SalesService(EventBus eventBus, StockService stockService) {
        this.eventBus = eventBus;
        this.stockService = stockService;
    }

    public synchronized void buy(Cafe cafe, CoffeeDrink drink, int qty) throws OutOfStockException {
        SaleEvent event = new SaleEvent(cafe, drink, qty);
        if (stockService.getCurrentStock(cafe).compareTo(event.getConsumedBeanUnits()) >= 0) {
            LOGGER.info("Registering {}", event);
            registerSale(event);
            eventBus.post(event);
        } else {
            LOGGER.info("{} is out of stock", cafe);
            throw new OutOfStockException();
        }
    }

    private void registerSale(SaleEvent event) {
        getSalesEvents24h(event.getCafe()).add(event);
    }

    private void removeEventsOlderThan(Instant instant, LinkedList<SaleEvent> events) {
        Iterator<SaleEvent> it = events.iterator();
        while (it.hasNext()) {
            SaleEvent event = it.next();
            if (event.getInstant().isBefore(instant)) {
                it.remove();
            } else {
                return;
            }
        }
    }

    private List<SaleEvent> getSalesEvents24h(Cafe cafe) {
        LinkedList<SaleEvent> events = salesEvents24h.get(cafe);
        if (events == null) {
            events = new LinkedList<>();
            salesEvents24h.put(cafe, events);
        } else {
            removeEventsOlderThan(getInstant24hoursAgo(), events);
        }
        return events;
    }

    private Instant getInstant24hoursAgo() {
        // For demonstrational purposes, we've sped up the world so that 24 hours is 5 minutes
        return ClockProvider.getClock().instant().minusSeconds(300);
    }

    public BigDecimal get24hIncome(Cafe cafe) {
        List<SaleEvent> eventListCopy;
        synchronized (this) {
            eventListCopy = new LinkedList<>(getSalesEvents24h(cafe));
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (SaleEvent event : eventListCopy) {
            sum = sum.add(event.getTotalIncome());
        }
        return sum;
    }

    public static class OutOfStockException extends Exception {
    }
}
