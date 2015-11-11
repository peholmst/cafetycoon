package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class CafeStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CafeStatusService.class);
    private final static int SERVING_COFFEE_DURATION_SECONDS = 10;

    private final EventBus eventBus;
    private final StockService stockService;

    private final Map<Cafe, CafeStatus> status = new HashMap<>();
    private final Map<Cafe, Instant> latestSales = new HashMap<>();
    private final BigDecimal outOfStockThreshold;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public CafeStatusService(EventBus eventBus, CoffeeDrinkRepository coffeeDrinkRepository,
        CafeRepository cafeRepository, StockService stockService) {
        this.eventBus = eventBus;
        this.stockService = stockService;

        outOfStockThreshold = calculateOutOfStockThreshold(coffeeDrinkRepository);

        cafeRepository.getCafes().forEach(this::updateStatus);

        eventBus.register(this);

        // In a real application, you would also stop this job when the application shuts down
        executorService.scheduleWithFixedDelay(this::updateServingCoffeeStatus, 10, 1, TimeUnit.SECONDS);
    }

    private BigDecimal calculateOutOfStockThreshold(CoffeeDrinkRepository coffeeDrinkRepository) {
        BigDecimal threshold = BigDecimal.ZERO;
        for (CoffeeDrink coffeeDrink : coffeeDrinkRepository.getCoffeeDrinks()) {
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
        latestSales.put(saleEvent.getCafe(), saleEvent.getInstant());
        updateStatus(saleEvent.getCafe());
    }

    @Subscribe
    protected synchronized void onRestockEvent(RestockEvent restockEvent) {
        updateStatus(restockEvent.getCafe());
    }

    private synchronized void updateServingCoffeeStatus() {
        status.keySet().forEach(this::updateStatus);
    }

    private void updateStatus(Cafe cafe) {
        if (isOutOfStock(cafe)) {
            setStatus(cafe, CafeStatus.OUT_OF_STOCK);
        } else if (isServingCoffee(cafe)) {
            setStatus(cafe, CafeStatus.SERVING_COFFEE);
        } else {
            setStatus(cafe, CafeStatus.RUNNING);
        }
    }

    private void setStatus(Cafe cafe, CafeStatus status) {
        if (!status.equals(this.status.get(cafe))) {
            LOGGER.info("Status of {} is now {}", cafe, status);
            this.status.put(cafe, status);
            eventBus.post(new CafeStatusChangedEvent(cafe, status));
        }
    }

    private boolean isOutOfStock(Cafe cafe) {
        return stockService.getCurrentStock(cafe).compareTo(outOfStockThreshold) <= 0;
    }

    private boolean isServingCoffee(Cafe cafe) {
        Instant instant = latestSales.get(cafe);
        return instant != null && Duration.between(instant, ClockProvider.getClock().instant())
            .getSeconds() < SERVING_COFFEE_DURATION_SECONDS;
    }

}
