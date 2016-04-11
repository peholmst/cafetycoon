package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.samples.cafetycoon.domain.*;

import com.google.common.eventbus.Subscribe;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import org.vaadin.samples.cafetycoon.domain.events.RestockEvent;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;
import org.vaadin.samples.cafetycoon.domain.events.StockChangeEvent;

public class CafeModel extends AbstractModel {

    private final IndexedContainer salesData;
    private final DataSeries beanStockChanges;
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
        salesData = createSalesDataContainer();
        beanStockChanges = createBeanStockSeries();
        this.cafeSelectionModel = cafeSelectionModel;
    }

    @Override
    protected void modelAttached() {
        cafeSelectionModel.getSelection().addValueChangeListener(this::cafeSelectionChanged);
        updateModel(true, true);
    }

    @Override
    public void detach() {
        cafeSelectionModel.getSelection().removeValueChangeListener(this::cafeSelectionChanged);
        super.detach();
    }

    private void cafeSelectionChanged(Property.ValueChangeEvent event) {
        updateModel(true, true);
    }

    private IndexedContainer createSalesDataContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(COL_DRINK, String.class, null);
        container.addContainerProperty(COL_PRICE, BigDecimal.class, BigDecimal.ZERO);
        container.addContainerProperty(COL_UNITS_SOLD, Integer.class, 0);
        container.addContainerProperty(COL_INCOME, BigDecimal.class, BigDecimal.ZERO);
        return container;
    }

    private DataSeries createBeanStockSeries() {
        DataSeries series = new DataSeries();
        return series;
    }

    public IndexedContainer get24hSalesData() {
        return salesData;
    }

    public DataSeries get24hBeanStockChanges() {
        return beanStockChanges;
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent event) {
        if (event.getCafe().equals(cafeSelectionModel.getSelection().getValue())) {
            updateModel(true, true);
        }
    }

    @Subscribe
    protected synchronized void onRestockEvent(RestockEvent event) {
        if (event.getCafe().equals(cafeSelectionModel.getSelection().getValue())) {
            updateModel(false, true);
        }
    }

    private void updateModel(boolean updateSales, boolean updateStock) {
        final Cafe cafe = cafeSelectionModel.getSelection().getValue();
        final List<Runnable> jobs = new LinkedList<>();
        if (updateSales) {
            List<CoffeeDrinkSaleStats> saleStats;
            if (cafe != null) {
                saleStats = Services.getInstance().getSalesService().get24hSaleStats(cafe);
            } else {
                saleStats = Collections.emptyList();
            }
            BigDecimal oldTotalIncome = totalIncome;
            int oldTotalUnitsSold = totalUnitsSold;
            jobs.add(() -> {
                totalIncome = BigDecimal.ZERO;
                totalUnitsSold = 0;
                salesData.removeAllItems();
                saleStats.forEach(this::addSaleStats);
                firePropertyChange(PROP_TOTAL_INCOME, oldTotalIncome, totalIncome);
                firePropertyChange(PROP_TOTAL_UNITS_SOLD, oldTotalUnitsSold, totalUnitsSold);
            });
        }
        if (updateStock) {
            List<StockChangeEvent> stockChanges;
            if (cafe != null) {
                stockChanges = new ArrayList<>(Services.getInstance().getStockService().get24hStockChangeEvents(cafe));
            } else {
                stockChanges = Collections.emptyList();
            }
            jobs.add(() -> {
                beanStockChanges.setData(stockChanges.stream()
                    .map(change -> new DataSeriesItem(Date.from(change.getInstant()), change.getCurrentStock()))
                    .collect(Collectors.toList()));
            });
        }
        if (jobs.size() > 0) {
            access(() -> jobs.forEach(Runnable::run));
        }
    }

    @SuppressWarnings("unchecked")
    private void addSaleStats(CoffeeDrinkSaleStats stats) {
        Item item = salesData.addItem(stats);
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
