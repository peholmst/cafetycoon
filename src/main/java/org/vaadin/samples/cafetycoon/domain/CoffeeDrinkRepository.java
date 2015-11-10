package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoffeeDrinkRepository {

    private final List<CoffeeDrink> coffeDrinks;

    public CoffeeDrinkRepository() {
        List<CoffeeDrink> coffeeDrinks = new ArrayList<>();

        coffeeDrinks.add(new CoffeeDrink("Americano", new BigDecimal("1.60"), new BigDecimal(2)));
        coffeeDrinks.add(new CoffeeDrink("Cappuccino", new BigDecimal("2.10"), new BigDecimal(3)));
        coffeeDrinks.add(new CoffeeDrink("Espresso", new BigDecimal("1.20"), new BigDecimal("1.5")));
        coffeeDrinks.add(new CoffeeDrink("Latte", new BigDecimal("2.55"), new BigDecimal(5)));
        coffeeDrinks.add(new CoffeeDrink("Mocha", new BigDecimal("2.35"), new BigDecimal(4)));

        this.coffeDrinks = Collections.unmodifiableList(coffeeDrinks);
    }

    public List<CoffeeDrink> getCoffeDrinks() {
        return coffeDrinks;
    }
}
