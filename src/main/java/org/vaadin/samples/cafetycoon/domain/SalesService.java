package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.eventbus.EventBus;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;

public class SalesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesService.class);
    private final EventBus eventBus;
    private final StockService stockService;
    private final Map<Cafe, TemporalCache<SaleEvent>> salesEvents24h = new HashMap<>();

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

    private TemporalCache<SaleEvent> getSalesEvents24h(Cafe cafe) {
        TemporalCache<SaleEvent> events = salesEvents24h.get(cafe);
        if (events == null) {
            events = new TemporalCache<>();
            salesEvents24h.put(cafe, events);
        }
        return events;
    }

    public BigDecimal get24hIncome(Cafe cafe) {
        List<SaleEvent> eventListCopy;
        synchronized (this) {
            eventListCopy = new LinkedList<>(getSalesEvents24h(cafe).getEntries());
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (SaleEvent event : eventListCopy) {
            sum = sum.add(event.getTotalIncome());
        }
        return sum;
    }

    public synchronized BigDecimal getTotal24hIncome() {
        BigDecimal sum = BigDecimal.ZERO;
        Set<Cafe> cafes;
        synchronized (this) {
            cafes = new HashSet<>(salesEvents24h.keySet());
        }
        for (Cafe cafe : cafes) {
            sum = sum.add(get24hIncome(cafe));
        }
        return sum;
    }

    public List<CoffeeDrinkSaleStats> get24hSaleStats(Cafe cafe) {
        List<SaleEvent> eventListCopy;
        synchronized (this) {
            eventListCopy = new LinkedList<>(getSalesEvents24h(cafe).getEntries());
        }
        Map<CoffeeDrink, CoffeeDrinkSaleStats.Builder> builderMap = new HashMap<>();
        for (SaleEvent event : eventListCopy) {
            CoffeeDrinkSaleStats.Builder builder = builderMap.get(event.getDrink());
            if (builder == null) {
                builder = new CoffeeDrinkSaleStats.Builder(event.getDrink());
                builderMap.put(event.getDrink(), builder);
            }
            builder.addSaleEvent(event);
        }
        return builderMap.values().stream().map(CoffeeDrinkSaleStats.Builder::build).collect(Collectors.toList());
    }

    @SuppressWarnings("serial")
	public static class OutOfStockException extends Exception {
    }
}
