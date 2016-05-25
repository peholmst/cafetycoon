package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.domain.ServiceProvider;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeOverviewModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.PersonnelModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;

import com.google.common.eventbus.EventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
public class Dashboard extends DashboardDesign implements View, TitledElement {

	public static final String VIEW_NAME = "dashboard";

	private SalesOverviewModel salesOverviewModel;
	private CafeSelectionModel cafeSelectionModel;
	private CafeOverviewModel cafeOverviewModel;
	private PersonnelModel personnelModel;

	public Dashboard() {
		// Since we're not using a dependency injection framework, we have to
		// wire up everything manually. In this case, we're simply using method
		// pointers as service providers.
		// In a real application, you would probably want to use more advanced
		// service providers that serialize and deserialize properly, check for
		// the availability of the backend service, etc.

		Services s = Services.getInstance();

		salesOverviewModel = new SalesOverviewModel(s::getCafeStatusService, s::getSalesService, s::getBalanceService,
				s::getCafeRepository);
		salesOverview.setSalesOverviewModel(salesOverviewModel);
		cafeMap.setSalesOverviewModel(salesOverviewModel);

		cafeSelectionModel = new CafeSelectionModel();
		salesOverview.setCafeSelectionModel(cafeSelectionModel);
		cafeMap.setCafeSelectionModel(cafeSelectionModel);
		cafeOverview.setCafeSelectionModel(cafeSelectionModel);

		cafeOverviewModel = new CafeOverviewModel(s::getStockService, s::getSalesService, s::getCafeStatusService);
		cafeOverviewModel.setCafeSelectionModel(cafeSelectionModel);
		cafeOverview.setCafeOverviewModel(cafeOverviewModel);
		
		personnelModel = new PersonnelModel(s::getEmployeeRepository);
		personnelModel.setCafeSelectionModel(cafeSelectionModel);
		cafeOverview.setPersonnelModel(personnelModel);
	}

	@Override
	public void attach() {
		super.attach();
		ServiceProvider<EventBus> eventBus = Services.getInstance()::getEventBus;
		salesOverviewModel.attach(getUI(), eventBus);
		cafeOverviewModel.attach(getUI(), eventBus);
		personnelModel.attach(getUI(), eventBus);
	}

	@Override
	public void detach() {
		personnelModel.detach();
		cafeOverviewModel.detach();
		salesOverviewModel.detach();
		super.detach();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// NOP
	}

	@Override
	public String getTitle() {
		return "Dashboard";
	}
}
