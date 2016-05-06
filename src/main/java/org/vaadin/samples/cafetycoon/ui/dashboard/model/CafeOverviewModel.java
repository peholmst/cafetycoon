package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.CoffeeDrink;
import org.vaadin.samples.cafetycoon.domain.CoffeeDrinkSaleStats;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.domain.events.CafeStatusChangedEvent;
import org.vaadin.samples.cafetycoon.domain.events.RestockEvent;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;
import org.vaadin.samples.cafetycoon.domain.events.StockChangeEvent;
import org.vaadin.samples.cafetycoon.ui.utils.EventContainer;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;

@SuppressWarnings("serial")
public class CafeOverviewModel extends AbstractModel implements CafeSelectionModel.Observer {

	public static final String COL_DRINK = "Coffee Drink";
	public static final String COL_PRICE = "Price";
	public static final String COL_UNITS_SOLD = "Units Sold";
	public static final String COL_INCOME = "Income";

	private CafeSelectionModel selectionModel;
	private final EventContainer<StockChangeEvent> beanStockChanges24h = new EventContainer<>();
	private final IndexedContainer salesData24h = createSalesDataContainer();
	private final ObjectProperty<CafeStatus> currentStatus = new ObjectProperty<>(CafeStatus.UNKNOWN, CafeStatus.class);
	
	private IndexedContainer createSalesDataContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(COL_DRINK, String.class, null);
		container.addContainerProperty(COL_PRICE, BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty(COL_UNITS_SOLD, Integer.class, 0);
		container.addContainerProperty(COL_INCOME, BigDecimal.class, BigDecimal.ZERO);
		return container;
	}

	public Indexed messages() {
		return null; // TODO Implement me!
	}

	public Indexed salesData24h() {
		return salesData24h;
	}

	public EventContainer<StockChangeEvent> beanStockChanges24h() {
		return beanStockChanges24h;
	}
	
	public ObjectProperty<CafeStatus> currentStatus() {
		return currentStatus;
	}

	@Subscribe
	protected synchronized void onSaleEvent(SaleEvent event) {
		if (event.getCafe().equals(selectionModel.selection().getValue())) {
			access(this::updateSalesData, this::updateBeanStock);
		}
	}

	@Subscribe
	protected synchronized void onRestockEvent(RestockEvent event) {
		if (event.getCafe().equals(selectionModel.selection().getValue())) {
			access(this::updateBeanStock);
		}
	}
	
	@Subscribe	
	protected synchronized void onCafeStatusChangedEvent(CafeStatusChangedEvent event) {
		if (event.getCafe().equals(selectionModel.selection().getValue())) {
			access(this::updateStatus);
		}
	}

	private void updateStatus() {
		Cafe cafe = selectionModel.selection().getValue();
		if (cafe != null) {
			currentStatus.setValue(Services.getInstance().getCafeStatusService().getCurrentStatus(cafe));
		} else {
			currentStatus.setValue(CafeStatus.UNKNOWN);
		}
	}
	
	private void updateBeanStock() {
		Cafe cafe = selectionModel.selection().getValue();
		List<StockChangeEvent> stockChanges;
		if (cafe != null) {
			stockChanges = new ArrayList<>(Services.getInstance().getStockService().get24hStockChangeEvents(cafe));
		} else {
			stockChanges = Collections.emptyList();
		}
		beanStockChanges24h.setEvents(stockChanges);
	}
	
	@SuppressWarnings("unchecked")
	private void updateSalesData() {
		Cafe cafe = selectionModel.selection().getValue();
		List<CoffeeDrinkSaleStats> saleStats;
		if (cafe != null) {
			saleStats = Services.getInstance().getSalesService().get24hSaleStats(cafe);
		} else {
			saleStats = Collections.emptyList();
		}
		
		Set<CoffeeDrink> drinksToDelete = new HashSet<>((Collection<CoffeeDrink>) salesData24h.getItemIds());
		saleStats.forEach(stats -> addOrUpdateSaleStats(drinksToDelete, stats));
		drinksToDelete.forEach(salesData24h::removeItem);
	}

	@SuppressWarnings("unchecked")
	private void addOrUpdateSaleStats(Set<CoffeeDrink> drinksToDelete, CoffeeDrinkSaleStats stats) {
		Item item = salesData24h.getItem(stats.getDrink());
		if (item == null) {
			item = salesData24h.addItem(stats.getDrink());
		}
		drinksToDelete.remove(stats.getDrink());
		item.getItemProperty(COL_DRINK).setValue(stats.getDrink().getName());
		item.getItemProperty(COL_PRICE).setValue(stats.getDrink().getPrice());
		item.getItemProperty(COL_UNITS_SOLD).setValue(stats.getUnitsSold());
		item.getItemProperty(COL_INCOME).setValue(stats.getTotalIncome());
	}

	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		if (selectionModel != null) {
			selectionModel.selection().removeValueChangeListener(this::cafeSelectionChanged);
		}
		selectionModel = model;
		if (selectionModel != null) {
			selectionModel.selection().addValueChangeListener(this::cafeSelectionChanged);
		}
	}

	private void cafeSelectionChanged(Property.ValueChangeEvent event) {
		access(this::updateBeanStock, this::updateSalesData, this::updateStatus);
	}

	public interface Observer {
		void setCafeOverviewModel(CafeOverviewModel model);
	}
}
