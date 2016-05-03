package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;

import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class CafeOverview extends CafeOverviewDesign implements CafeSelectionModel.Observer {

	private CafeSelectionModel cafeSelectionModel;
	
	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		// We're only setting this once and the model and the component have the same scope -> no need to clean up
		cafeSelectionModel = model;
		model.selection().addValueChangeListener(this::cafeSelected);
	}
	
	private void cafeSelected(Property.ValueChangeEvent event) {
		if (cafeSelectionModel.selection().getValue() == null) {
			hide();
		} else {
			show();
		}
	}	

	private void show() {
		removeStyleName("hidden");
		addStyleName("visible");
	}
	
	private void hide() {
		removeStyleName("visible");
		addStyleName("hidden");
	}	
}
