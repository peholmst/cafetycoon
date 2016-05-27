package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.math.BigDecimal;

import org.vaadin.samples.cafetycoon.domain.BalanceService;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeRepository;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.CafeStatusService;
import org.vaadin.samples.cafetycoon.domain.SalesService;
import org.vaadin.samples.cafetycoon.domain.ServiceProvider;
import org.vaadin.samples.cafetycoon.domain.events.CafeStatusChangedEvent;
import org.vaadin.samples.cafetycoon.domain.events.RestockEvent;
import org.vaadin.samples.cafetycoon.domain.events.SaleEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;

/**
 * Application model that contains an overview of all the cafes, their current
 * status, sales during the past 24 hours and total balance of all cafes.
 */
@SuppressWarnings("serial")
public class SalesOverviewModel extends AbstractModel {

	/**
	 * Property ID for the name of the cafe.
	 */
	public static final String COL_CAFE = "cafe";
	/**
	 * Property ID for the income of the cafe during the past 24 hours.
	 */
	public static final String COL_24INCOME = "24h income";
	/**
	 * Property ID for the current status of the cafe.
	 */
	public static final String COL_STATUS = "status";

	private final ObjectProperty<BigDecimal> balance = new ObjectProperty<>(BigDecimal.ZERO, BigDecimal.class);
	private final ObjectProperty<BigDecimal> income24h = new ObjectProperty<>(BigDecimal.ZERO, BigDecimal.class);
	private final IndexedContainer cafes = new IndexedContainer();

	private final ServiceProvider<CafeStatusService> cafeStatusService;
	private final ServiceProvider<SalesService> salesService;
	private final ServiceProvider<BalanceService> balanceService;
	private final ServiceProvider<CafeRepository> cafeRepository;

	/**
	 * Convenience interface for model observers, not a requirement.
	 */
	public interface Observer {
		void setSalesOverviewModel(SalesOverviewModel model);
	}

	/**
	 * Creates a new {@code SalesOverviewModel}. The constructor parameters are
	 * all required backend services.
	 */
	public SalesOverviewModel(ServiceProvider<CafeStatusService> cafeStatusService,
			ServiceProvider<SalesService> salesService, ServiceProvider<BalanceService> balanceService,
			ServiceProvider<CafeRepository> cafeRepository) {
		this.cafeStatusService = cafeStatusService;
		this.salesService = salesService;
		this.balanceService = balanceService;
		this.cafeRepository = cafeRepository;

		cafes.addContainerProperty(COL_CAFE, String.class, "");
		cafes.addContainerProperty(COL_24INCOME, BigDecimal.class, BigDecimal.ZERO);
		cafes.addContainerProperty(COL_STATUS, CafeStatus.class, null);
	}

	/**
	 * Returns a property containing the current total balance.
	 */
	public ObjectProperty<BigDecimal> balance() {
		return balance;
	}

	/**
	 * Returns a property containing the total income during the last 24 hours.
	 */
	public ObjectProperty<BigDecimal> income24h() {
		return income24h;
	}

	/**
	 * Returns a container of all the cafes and their status.
	 * 
	 * @see #COL_24INCOME
	 * @see #COL_CAFE
	 * @see #COL_STATUS
	 */
	public Indexed cafes() {
		return cafes;
	}

	/**
	 * Updates the model whenever the state of a cafe is changed.
	 */
	@Subscribe
	protected void onCafeStatusChangedEvent(CafeStatusChangedEvent event) {
		access(() -> updateCafe(event.getCafe()));
	}

	/**
	 * Updates the model whenever a sale has been registered (more income,
	 * increase in balance).
	 */
	@Subscribe
	protected void onSaleEvent(SaleEvent event) {
		access(() -> {
			updateBalances();
			updateCafe(event.getCafe());
		});
	}

	/**
	 * Updates the model whenever a cafe has been restocked (decrease in
	 * balance).
	 */
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
		cafeRepository.get().getCafes().forEach(this::updateCafe);
	}

	@SuppressWarnings("unchecked")
	private void updateCafe(Cafe cafe) {
		Item cafeItem = cafes.getItem(cafe);
		if (cafeItem == null) {
			cafeItem = cafes.addItem(cafe);
			cafeItem.getItemProperty(COL_CAFE).setValue(cafe.getName());
		}
		cafeItem.getItemProperty(COL_24INCOME).setValue(salesService.get().get24hIncome(cafe));
		cafeItem.getItemProperty(COL_STATUS).setValue(cafeStatusService.get().getCurrentStatus(cafe));
	}

	private void updateBalances() {
		balance.setValue(balanceService.get().getCurrentTotalBalance());
		income24h.setValue(salesService.get().getTotal24hIncome());
	}
}
