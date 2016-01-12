package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.ui.Grid;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeModel;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyConverter;

import java.beans.PropertyChangeEvent;

public class CafeSalesGrid extends Grid {

    private final CafeModel model;
    private final MoneyConverter moneyConverter = new MoneyConverter();

    public CafeSalesGrid(CafeModel model) { // Model has the same scope as this grid, no need to unregister
        super("24 Hour Sales Data");
        this.model = model;
        addStyleName("sales");
        setSizeFull();
        setContainerDataSource(model.get24hSalesData());
        getColumn(CafeModel.COL_PRICE).setConverter(new MoneyConverter());
        getColumn(CafeModel.COL_INCOME).setConverter(new MoneyConverter());
        setCellStyleGenerator(ref -> {
            if (ref.getPropertyId().equals(CafeModel.COL_PRICE) || ref.getPropertyId().equals(CafeModel.COL_INCOME)
                || ref.getPropertyId().equals(CafeModel.COL_UNITS_SOLD)) {
                return "ralign";
            } else if (ref.getPropertyId().equals(CafeModel.COL_DRINK)) {
                return "boldtext";
            } else {
                return null;
            }
        });
        setFooterVisible(true);
        setSelectionMode(Grid.SelectionMode.NONE);
        getDefaultHeaderRow().getCell(CafeModel.COL_INCOME).setStyleName("ralign");
        getDefaultHeaderRow().getCell(CafeModel.COL_PRICE).setStyleName("ralign");
        getDefaultHeaderRow().getCell(CafeModel.COL_UNITS_SOLD).setStyleName("ralign");
        Grid.FooterRow footer = appendFooterRow();
        footer.getCell(CafeModel.COL_DRINK).setText("Total");
        footer.getCell(CafeModel.COL_INCOME).setStyleName("ralign");
        footer.getCell(CafeModel.COL_UNITS_SOLD).setStyleName("ralign");

        model.addPropertyChangeListener(CafeModel.PROP_TOTAL_INCOME, this::updateTotalIncome);
        model.addPropertyChangeListener(CafeModel.PROP_TOTAL_UNITS_SOLD, this::updateTotalUnitsSold);
    }

    private void updateTotalIncome(PropertyChangeEvent event) {
        FooterCell cell = getFooterRow(0).getCell(CafeModel.COL_INCOME);
        cell.setText(moneyConverter.convertToPresentation(model.getTotalIncome(), String.class, getLocale()));
    }

    private void updateTotalUnitsSold(PropertyChangeEvent event) {
        FooterCell cell = getFooterRow(0).getCell(CafeModel.COL_UNITS_SOLD);
        cell.setText(String.valueOf(model.getTotalUnitsSold()));
    }

}
