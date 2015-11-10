package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class CafeStatusService {

    private final EventBus eventBus;
    private final StockService stockService;

    private final Map<Cafe, CafeStatus> status = new HashMap<>();
    private final Map<Cafe, Instant> latestSales = new HashMap<>();
    private final BigDecimal outOfStockThreshold;

    public CafeStatusService(EventBus eventBus, CoffeeDrinkRepository coffeeDrinkRepository,
        CafeRepository cafeRepository, StockService stockService) {
        this.eventBus = eventBus;
        this.stockService = stockService;

        outOfStockThreshold = calculateOutOfStockThreshold(coffeeDrinkRepository);

        cafeRepository.getCafes().forEach(this::updateStatus);

        eventBus.register(this);
    }

    private BigDecimal calculateOutOfStockThreshold(CoffeeDrinkRepository coffeeDrinkRepository) {
        BigDecimal threshold = BigDecimal.ZERO;
        for (CoffeeDrink coffeeDrink : coffeeDrinkRepository.getCoffeDrinks()) {
            if (coffeeDrink.getBeanUnits().compareTo(threshold) > 0) {
                threshold = coffeeDrink.getBeanUnits();
            }
        }
        return threshold;
    }

    public synchronized CafeStatus getCurrentStatus(Cafe cafe) {
        return status.getOrDefault(cafe, CafeStatus.UNKNOWN);
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent saleEvent) {
        updateStatus(saleEvent.getCafe());
    }

    @Subscribe
    protected synchronized void onRestockEvent(RestockEvent restockEvent) {
        updateStatus(restockEvent.getCafe());
    }

    private void updateStatus(Cafe cafe) {
        // TODO Implement me!
    }

    private boolean isOutOfStock(Cafe cafe) {
        return stockService.getCurrentStock(cafe).compareTo(outOfStockThreshold) <= 0;
    }

}
