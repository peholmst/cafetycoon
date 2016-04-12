package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.SalesOverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyConverter;

@SuppressWarnings("serial")
public class SalesOverview extends SalesOverviewDesign {

	public SalesOverview() {
		balance.setValidationVisible(false); // Converter is one-way, we don't want any validation
		balance.setConverter(new MoneyConverter());
		income24h.setValidationVisible(false); // Converter is one-way, we don't want any validation
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
	
	public void setSalesOverviewModel(SalesOverviewModel salesOverviewModel) {
		if (salesOverviewModel != null) {
			balance.setPropertyDataSource(salesOverviewModel.balance());
			income24h.setPropertyDataSource(salesOverviewModel.income24h());
			cafes.setContainerDataSource(salesOverviewModel.cafes());
			
			cafes.getColumn(SalesOverviewModel.COL_24INCOME).setConverter(new MoneyConverter());
		} else {
			balance.setPropertyDataSource(null);
			income24h.setPropertyDataSource(null);
			cafes.setContainerDataSource(null);
		}
	}
}
