package org.vaadin.samples.cafetycoon.ui.dashboard;

import java.util.Objects;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyConverter;

@SuppressWarnings("serial")
public class SalesOverview extends SalesOverviewDesign
		implements SalesOverviewModel.Observer, CafeSelectionModel.Observer {

	public SalesOverview() {
		// MoneyConverter is one-way, we don't want any validation
		balance.setValidationVisible(false); 
		income24h.setValidationVisible(false);
		
		balance.setConverter(new MoneyConverter());
		income24h.setConverter(new MoneyConverter());

		cafes.setCellStyleGenerator(cellReference -> {
			if (cellReference.getPropertyId().equals(SalesOverviewModel.COL_STATUS)) {
				return "cafe-status-" + cellReference.getProperty().getValue();
			} else if (cellReference.getPropertyId().equals(SalesOverviewModel.COL_24INCOME)) {
				return "cafe-income";
			} else {
				return null;
			}
		});
	}

	@Override
	public void setSalesOverviewModel(SalesOverviewModel model) {
		// We're only setting this once and the model and the component have the same scope -> no need to clean up
		Objects.requireNonNull(model);
		balance.setPropertyDataSource(model.balance());
		income24h.setPropertyDataSource(model.income24h());
		cafes.setContainerDataSource(model.cafes());

		cafes.getColumn(SalesOverviewModel.COL_24INCOME).setConverter(new MoneyConverter());
	}

	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		// We're only setting this once and the model and the component have the same scope -> no need to clean up
		Objects.requireNonNull(model);
		cafes.addSelectionListener(event -> model.selection().setValue((Cafe) cafes.getSelectedRow()));
		model.selection().addValueChangeListener(event -> cafes.select(model.selection().getValue()));
	}
}
