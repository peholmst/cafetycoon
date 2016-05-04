package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeOverviewModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
public class Dashboard extends DashboardDesign implements View, TitledElement {

    public static final String VIEW_NAME = "dashboard";	
	
	private SalesOverviewModel salesOverviewModel;
	private CafeSelectionModel cafeSelectionModel;
	private CafeOverviewModel cafeOverviewModel;
	
	public Dashboard() {
		salesOverviewModel = new SalesOverviewModel();
		salesOverview.setSalesOverviewModel(salesOverviewModel);
		cafeMap.setSalesOverviewModel(salesOverviewModel);
		
		cafeSelectionModel = new CafeSelectionModel();
		salesOverview.setCafeSelectionModel(cafeSelectionModel);
		cafeMap.setCafeSelectionModel(cafeSelectionModel);
		cafeOverview.setCafeSelectionModel(cafeSelectionModel);
		
		cafeOverviewModel = new CafeOverviewModel();
		cafeOverviewModel.setCafeSelectionModel(cafeSelectionModel);
		cafeOverview.setCafeOverviewModel(cafeOverviewModel);
	}
	
	@Override
	public void attach() {
		super.attach();
		salesOverviewModel.attach(getUI());
		cafeOverviewModel.attach(getUI());
	}
	
	@Override
	public void detach() {
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
