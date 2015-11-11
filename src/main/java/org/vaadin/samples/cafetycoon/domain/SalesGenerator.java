package org.vaadin.samples.cafetycoon.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SalesGenerator {

    private final Random rnd = new Random();
    private final CafeRepository cafeRepository;
    private final CoffeeDrinkRepository coffeeDrinkRepository;
    private final SalesService salesService;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public SalesGenerator(CafeRepository cafeRepository, CoffeeDrinkRepository coffeeDrinkRepository, SalesService salesService) {
        this.salesService = salesService;
        this.cafeRepository = cafeRepository;
        this.coffeeDrinkRepository = coffeeDrinkRepository;

        executorService.scheduleWithFixedDelay(this::generateSale, 5, 1, TimeUnit.SECONDS);
    }

    public void generateSale() {
        if (rnd.nextInt(3) > 0) {
            return;
        }

        Cafe cafe = pickRandom(cafeRepository.getCafes());
        CoffeeDrink drink = pickRandom(coffeeDrinkRepository.getCoffeeDrinks());
        int qty = rnd.nextInt(10);
        try {
            salesService.buy(cafe, drink, qty);
        } catch (SalesService.OutOfStockException e) {
            // Ignore it
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
