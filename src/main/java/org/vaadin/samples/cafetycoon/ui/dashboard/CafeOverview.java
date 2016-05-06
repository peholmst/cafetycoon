package org.vaadin.samples.cafetycoon.ui.dashboard;

import java.math.BigDecimal;
import java.sql.Date;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeOverviewModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.utils.EventContainerDataSeriesFactory;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyConverter;

import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class CafeOverview extends CafeOverviewDesign
		implements CafeSelectionModel.Observer, CafeOverviewModel.Observer {

	private CafeSelectionModel cafeSelectionModel;

	public CafeOverview() {
		restock50.addClickListener(this::restock50);
		restock100.addClickListener(this::restock100);
		restock200.addClickListener(this::restock200);
		
		statusChanged(null);
	}

	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up
		cafeSelectionModel = model;
		model.selection().addValueChangeListener(this::cafeSelected);
	}

	private void cafeSelected(Property.ValueChangeEvent event) {
		Cafe cafe = cafeSelectionModel.selection().getValue();
		// Not sure if the view should be responsible for showing and hiding
		// itself or if this should
		// be handled one level up in the component hierarchy.
		if (cafe == null) {
			hide();
		} else {
			show();
			cafeName.setValue(cafe.getName());
			cafeAddress.setValue(cafe.getAddress());
			//beanStock.drawChart();
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

	@Override
	public void setCafeOverviewModel(CafeOverviewModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up

		Configuration configuration = beanStock.getConfiguration();
		configuration.setTitle("");
		configuration.setSeries(new EventContainerDataSeriesFactory<>(
				e -> new DataSeriesItem(Date.from(e.getInstant()), e.getCurrentStock()), model.beanStockChanges24h())
						.getDataSeries());
		configuration.getyAxis().setTitle("");
		configuration.getxAxis().setType(AxisType.DATETIME);
		configuration.getLegend().setEnabled(false);

		salesData.setContainerDataSource(model.salesData24h());
		salesData.setSelectionMode(Grid.SelectionMode.NONE);

		salesData.getDefaultHeaderRow().getCell(CafeOverviewModel.COL_INCOME).setStyleName("ralign");
		salesData.getDefaultHeaderRow().getCell(CafeOverviewModel.COL_PRICE).setStyleName("ralign");
		salesData.getDefaultHeaderRow().getCell(CafeOverviewModel.COL_UNITS_SOLD).setStyleName("ralign");

		salesData.getColumn(CafeOverviewModel.COL_PRICE).setConverter(new MoneyConverter());
		salesData.getColumn(CafeOverviewModel.COL_INCOME).setConverter(new MoneyConverter());

		salesData.setCellStyleGenerator(ref -> {
			if (ref.getPropertyId().equals(CafeOverviewModel.COL_PRICE)
					|| ref.getPropertyId().equals(CafeOverviewModel.COL_INCOME)
					|| ref.getPropertyId().equals(CafeOverviewModel.COL_UNITS_SOLD)) {
				return "ralign";
			} else if (ref.getPropertyId().equals(CafeOverviewModel.COL_DRINK)) {
				return "boldtext";
			} else {
				return null;
			}
		});
		
		model.currentStatus().addValueChangeListener(this::statusChanged);
		
	}

	private void statusChanged(Property.ValueChangeEvent event) {
		CafeStatus newStatus = event == null ? CafeStatus.UNKNOWN :  (CafeStatus) event.getProperty().getValue();
		if (newStatus.equals(CafeStatus.OUT_OF_STOCK)) {
			status.setValue("OUT OF STOCK");
			status.addStyleName("out-of-stock");
			status.setVisible(true);
		} else if (newStatus.equals(CafeStatus.SERVING_COFFEE)) {
			status.setValue("SERVING COFFEE");
			status.addStyleName("serving-coffee");
			status.setVisible(true);
		} else {
			status.setVisible(false);
		}
	}
	
	private void restock50(Button.ClickEvent event) {
		restock(50);
	}

	private void restock100(Button.ClickEvent event) {
		restock(100);
	}

	private void restock200(Button.ClickEvent event) {
		restock(200);
	}

	private void restock(int amount) {
		Cafe cafe = cafeSelectionModel.selection().getValue();
		if (cafe != null) {
			Services.getInstance().getStockService().restock(cafe, new BigDecimal(amount));
		}
	}
}
