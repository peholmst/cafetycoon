package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;

public class Dashboard extends HorizontalLayout implements View {

    public static final String VIEW_NAME = "dashboard";

    private CafeTableView cafeTableView;
    private CafeMapView cafeMapView;
    private CafeDetailsView cafeDetailsView;

    public Dashboard() {
        setSizeFull();
        cafeTableView = new CafeTableView();
        addComponent(cafeTableView);

        cafeMapView = new CafeMapView();
        addComponent(cafeMapView);
        setExpandRatio(cafeMapView, 1.0f);

        cafeDetailsView = new CafeDetailsView();
        addComponent(cafeDetailsView);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
