package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.vaadin.samples.cafetycoon.domain.CoffeeDrink;
import org.vaadin.samples.cafetycoon.domain.SaleEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.IndexedContainer;

public class CafeModel extends AbstractModel {

    private final IndexedContainer container;

    public static final String COL_DRINK = "Coffee Drink";
    public static final String COL_PRICE = "Price";
    public static final String COL_UNITS_SOLD = "Units Sold";
    public static final String COL_INCOME = "Income";

    public CafeModel() {
        container = createContainer();
    }

    private IndexedContainer createContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(COL_DRINK, CoffeeDrink.class, null);
        container.addContainerProperty(COL_PRICE, BigDecimal.class, BigDecimal.ZERO);
        container.addContainerProperty(COL_UNITS_SOLD, Integer.class, 0);
        container.addContainerProperty(COL_INCOME, BigDecimal.class, BigDecimal.ZERO);
        return container;
    }

    public IndexedContainer get24hSalesData() {
        return container;
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent event) {
    }

    public static class CoffeeDrinkSaleStats implements Serializable {

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
    }
}
