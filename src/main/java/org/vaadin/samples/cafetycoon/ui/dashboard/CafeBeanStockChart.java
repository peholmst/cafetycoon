package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeModel;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;

public class CafeBeanStockChart extends Chart {

    public CafeBeanStockChart(CafeModel model) { // Model has the same scope as this chart, no need to unregister
        super(ChartType.LINE);
        setCaption("24 Hour Bean Stock");
        setSizeFull();
        addStyleName("bean-stock");
        Configuration configuration = new Configuration();
        configuration.setTitle("");
        configuration.addSeries(model.get24hBeanStockChanges());
        configuration.getyAxis().setTitle("");
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getLegend().setEnabled(false);
        setConfiguration(configuration);
    }

}
