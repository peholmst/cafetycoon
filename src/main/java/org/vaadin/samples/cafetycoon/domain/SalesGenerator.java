package org.vaadin.samples.cafetycoon.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class SalesGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesGenerator.class);
    private final Random rnd = new Random();
    private final CafeRepository cafeRepository;
    private final CoffeeDrinkRepository coffeeDrinkRepository;
    private final SalesService salesService;
    private ScheduledExecutorService executorService;

    public SalesGenerator(CafeRepository cafeRepository, CoffeeDrinkRepository coffeeDrinkRepository,
        SalesService salesService) {
        this.salesService = salesService;
        this.cafeRepository = cafeRepository;
        this.coffeeDrinkRepository = coffeeDrinkRepository;
    }

    public synchronized void start() {
    	if (executorService == null) {
    		executorService = Executors.newSingleThreadScheduledExecutor();
    		executorService.scheduleWithFixedDelay(this::generateSale, 5, 1, TimeUnit.SECONDS);
    	}
    }
    
    public synchronized void stop() {
    	if (executorService != null) {
    		try {
    			executorService.shutdown();
    		} finally {
    			executorService = null;
    		}
    	}
    }
    
    public void generateSale() {
        if (rnd.nextInt(3) > 0) {
            return;
        }
        Cafe cafe = pickRandom(cafeRepository.getCafes());
        CoffeeDrink drink = pickRandom(coffeeDrinkRepository.getCoffeeDrinks());
        int qty = rnd.nextInt(10);
        LOGGER.info("Generating sale, cafe = {}, drink = {}, qty = {}", new Object[] { cafe, drink, qty });
        try {
            salesService.buy(cafe, drink, qty);
        } catch (SalesService.OutOfStockException e) {
            // Ignore it
        } catch (Exception ex) {
            LOGGER.error("An error occurred while generating sale", ex);
            // Don't throw it
        }
    }

    private <T> T pickRandom(Collection<T> items) {
        List<T> itemList;
        if (items instanceof List) {
            itemList = (List<T>) items;
        } else {
            itemList = new ArrayList<>(items);
        }
        return itemList.get(rnd.nextInt(itemList.size()));
    }
}
