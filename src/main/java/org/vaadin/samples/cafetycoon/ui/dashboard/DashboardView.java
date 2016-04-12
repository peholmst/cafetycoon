package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
public class DashboardView extends DashboardDesign implements View, TitledElement {

    public static final String VIEW_NAME = "dashboard";	
	
	private SalesOverviewModel salesOverviewModel;
	
	public DashboardView() {
		salesOverviewModel = new SalesOverviewModel();
		salesOverview.setSalesOverviewModel(salesOverviewModel);
	}
	
	@Override
	public void attach() {
		super.attach();
		salesOverviewModel.attach(getUI());
	}
	
	@Override
	public void detach() {
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
