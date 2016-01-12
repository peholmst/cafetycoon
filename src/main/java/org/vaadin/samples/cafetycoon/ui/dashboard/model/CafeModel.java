package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.vaadin.data.Item;
import org.vaadin.samples.cafetycoon.domain.*;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

public class CafeModel extends AbstractModel {

    private final IndexedContainer container;
    private final CafeSelectionModel cafeSelectionModel;
    private BigDecimal totalIncome;
    private int totalUnitsSold;

    public static final String PROP_TOTAL_INCOME = "totalIncome";
    public static final String PROP_TOTAL_UNITS_SOLD = "totalUnitsSold";

    public static final String COL_DRINK = "Coffee Drink";
    public static final String COL_PRICE = "Price";
    public static final String COL_UNITS_SOLD = "Units Sold";
    public static final String COL_INCOME = "Income";

    public CafeModel(CafeSelectionModel cafeSelectionModel) {
        container = createContainer();
        this.cafeSelectionModel = cafeSelectionModel;
    }

    @Override
    protected void modelAttached() {
        cafeSelectionModel.getSelection().addValueChangeListener(this::cafeSelectionChanged);
        updateModel();
    }

    @Override
    public void detach() {
        cafeSelectionModel.getSelection().removeValueChangeListener(this::cafeSelectionChanged);
        super.detach();
    }

    private void cafeSelectionChanged(Property.ValueChangeEvent event) {
        updateModel();
    }

    private IndexedContainer createContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(COL_DRINK, String.class, null);
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
        if (event.getCafe().equals(cafeSelectionModel.getSelection().getValue())) {
            updateModel();
        }
    }

    private void updateModel() {
        Cafe cafe = cafeSelectionModel.getSelection().getValue();
        List<CoffeeDrinkSaleStats> saleStats;
        if (cafe != null) {
            saleStats = Services.getInstance().getSalesService().get24hSaleStats(cafe);
        } else {
            saleStats = Collections.emptyList();
        }
        BigDecimal oldTotalIncome = totalIncome;
        int oldTotalUnitsSold = totalUnitsSold;
        access(() -> {
            totalIncome = BigDecimal.ZERO;
            totalUnitsSold = 0;
            container.removeAllItems();
            saleStats.forEach(this::addSaleStats);
            firePropertyChange(PROP_TOTAL_INCOME, oldTotalIncome, totalIncome);
            firePropertyChange(PROP_TOTAL_UNITS_SOLD, oldTotalUnitsSold, totalUnitsSold);
        });
    }

    @SuppressWarnings("unchecked")
    private void addSaleStats(CoffeeDrinkSaleStats stats) {
        Item item = container.addItem(stats);
        item.getItemProperty(COL_DRINK).setValue(stats.getDrink().getName());
        item.getItemProperty(COL_PRICE).setValue(stats.getDrink().getPrice());
        item.getItemProperty(COL_UNITS_SOLD).setValue(stats.getUnitsSold());
        item.getItemProperty(COL_INCOME).setValue(stats.getTotalIncome());
        totalUnitsSold += stats.getUnitsSold();
        totalIncome = totalIncome.add(stats.getTotalIncome());
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public int getTotalUnitsSold() {
        return totalUnitsSold;
    }
}
