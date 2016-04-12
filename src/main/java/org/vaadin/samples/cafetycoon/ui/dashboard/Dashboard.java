package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.ui.CssLayout;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.OverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalSplitPanel;

@Deprecated
public class Dashboard extends HorizontalSplitPanel implements View, TitledElement {

    public static final String VIEW_NAME = "dashboard";

    private OverviewModel model;
    private CafeSelectionModel selectionModel;
    private CafeModel cafeModel;
    private CafeTableView cafeTableView;
    private CafeMapView cafeMapView;
    private CafeDetailsView cafeDetailsView;

    public Dashboard() {
        model = new OverviewModel();
        selectionModel = new CafeSelectionModel();
        cafeModel = new CafeModel(selectionModel);

        setSizeFull();
        cafeTableView = new CafeTableView(model, selectionModel);

        cafeMapView = new CafeMapView(model, selectionModel);

        cafeDetailsView = new CafeDetailsView(cafeModel, selectionModel);

        setFirstComponent(cafeTableView);

        CssLayout mapAndDetailsLayout = new CssLayout();
        mapAndDetailsLayout.setSizeFull();
        mapAndDetailsLayout.addStyleName("map-and-details-layout");
        mapAndDetailsLayout.addComponent(cafeMapView);
        mapAndDetailsLayout.addComponent(cafeDetailsView);

        setSecondComponent(mapAndDetailsLayout);
        setSplitPosition(400, Unit.PIXELS);
    }

    @Override
    public void attach() {
        super.attach();
        model.attach(getUI());
        cafeModel.attach(getUI());
    }

    @Override
    public void detach() {
        cafeModel.detach();
        model.detach();
        super.detach();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }
}
