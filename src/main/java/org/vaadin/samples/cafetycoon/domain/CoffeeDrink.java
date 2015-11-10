package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;

public class CoffeeDrink extends BaseEntity {

    private final String name;
    private final BigDecimal price;
    private final BigDecimal beanUnits;

    public CoffeeDrink(String name, BigDecimal price, BigDecimal beanUnits) {
        this.name = name;
        this.price = price;
        this.beanUnits = beanUnits;
    }

    /**
     * The name of this drink.
     */
    public String getName() {
        return name;
    }

    /**
     * The price of a cup of this drink.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * The number of bean units required to make a cup of this drink.
     */
    public BigDecimal getBeanUnits() {
        return beanUnits;
    }
}
