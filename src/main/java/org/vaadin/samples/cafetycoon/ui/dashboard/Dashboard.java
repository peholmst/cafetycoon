package org.vaadin.samples.cafetycoon.ui.dashboard;

import org.vaadin.samples.cafetycoon.ui.dashboard.model.OverviewModel;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalSplitPanel;

public class Dashboard extends HorizontalSplitPanel implements View {

    public static final String VIEW_NAME = "dashboard";

    private OverviewModel model;
    private CafeTableView cafeTableView;
    private CafeMapView cafeMapView;
    private CafeDetailsView cafeDetailsView;

    public Dashboard() {
        model = new OverviewModel();

        setSizeFull();
        cafeTableView = new CafeTableView(model);
        setFirstComponent(cafeTableView);

        cafeMapView = new CafeMapView(model);
        setSecondComponent(cafeMapView);

        setSplitPosition(400, Unit.PIXELS);

        // cafeDetailsView = new CafeDetailsView();
        // addComponent(cafeDetailsView);
    }

    @Override
    public void attach() {
        super.attach();
        model.attach(getUI());
    }

    @Override
    public void detach() {
        model.detach();
        super.detach();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
