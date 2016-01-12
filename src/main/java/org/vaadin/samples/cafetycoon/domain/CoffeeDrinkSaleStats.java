package org.vaadin.samples.cafetycoon.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class CoffeeDrinkSaleStats implements Serializable {

    private final CoffeeDrink drink;
    private final int unitsSold;
    private final BigDecimal totalIncome;

    public CoffeeDrinkSaleStats(CoffeeDrink drink, int unitsSold, BigDecimal totalIncome) {
        this.drink = drink;
        this.unitsSold = unitsSold;
        this.totalIncome = totalIncome;
    }

    public CoffeeDrink getDrink() {
        return drink;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public static class Builder {

        private CoffeeDrink coffeeDrink;
        private int unitsSold;
        private BigDecimal totalIncome = BigDecimal.ZERO;

        public Builder(CoffeeDrink coffeeDrink) {
            this.coffeeDrink = coffeeDrink;
        }

        public Builder addSaleEvent(SaleEvent saleEvent) {
            if (saleEvent.getDrink().equals(coffeeDrink)) {
                unitsSold += saleEvent.getQuantity();
                totalIncome = totalIncome.add(saleEvent.getTotalIncome());
            }
            return this;
        }

        public CoffeeDrinkSaleStats build() {
            return new CoffeeDrinkSaleStats(coffeeDrink, unitsSold, totalIncome);
        }
    }
}