package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.math.BigDecimal;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.domain.events.CafeStatusChangedEvent;
import org.vaadin.samples.cafetycoon.domain.events.RestockEvent;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;

@SuppressWarnings("serial")
public class SalesOverviewModel extends AbstractModel {

	public static final String COL_CAFE = "cafe";
	public static final String COL_24INCOME = "24h income";
	public static final String COL_STATUS = "status";

	private final ObjectProperty<BigDecimal> balance = new ObjectProperty<>(BigDecimal.ZERO, BigDecimal.class);
	private final ObjectProperty<BigDecimal> income24h = new ObjectProperty<>(BigDecimal.ZERO, BigDecimal.class);
	private final IndexedContainer cafes = new IndexedContainer();

	public interface Observer {
		void setSalesOverviewModel(SalesOverviewModel model);
	}
	
	public SalesOverviewModel() {
		cafes.addContainerProperty(COL_CAFE, String.class, "");
		cafes.addContainerProperty(COL_24INCOME, BigDecimal.class, BigDecimal.ZERO);
		cafes.addContainerProperty(COL_STATUS, CafeStatus.class, null);
	}

	public ObjectProperty<BigDecimal> balance() {
		return balance;
	}

	public ObjectProperty<BigDecimal> income24h() {
		return income24h;
	}

	public Indexed cafes() {
		return cafes;
	}

	@Subscribe
	protected void onCafeStatusChangedEvent(CafeStatusChangedEvent event) {
		access(() -> updateCafe(event.getCafe()));
	}

	@Subscribe
	protected void onSaleEvent(SaleEvent event) {
		access(() -> {
			updateBalances();
			updateCafe(event.getCafe());
		});
	}

	@Subscribe
	protected void onRestockEvent(RestockEvent event) {
		access(() -> {
			updateBalances();
			updateCafe(event.getCafe());
		});
	}

	@Override
	protected void modelAttached() {
		super.modelAttached();
		access(() -> {
			updateBalances();
			updateCafes();
		});
	}

	private void updateCafes() {
		Services.getInstance().getCafeRepository().getCafes().forEach(this::updateCafe);
	}

	@SuppressWarnings("unchecked")
	private void updateCafe(Cafe cafe) {
		Item cafeItem = cafes.getItem(cafe);
		if (cafeItem == null) {
			cafeItem = cafes.addItem(cafe);
			cafeItem.getItemProperty(COL_CAFE).setValue(cafe.getName());
		}
		cafeItem.getItemProperty(COL_24INCOME).setValue(Services.getInstance().getSalesService().get24hIncome(cafe));
		cafeItem.getItemProperty(COL_STATUS)
				.setValue(Services.getInstance().getCafeStatusService().getCurrentStatus(cafe));
	}

	private void updateBalances() {
		balance.setValue(Services.getInstance().getBalanceService().getCurrentTotalBalance());
		income24h.setValue(Services.getInstance().getSalesService().getTotal24hIncome());
	}
}
