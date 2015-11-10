package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.samples.cafetycoon.domain.CafeStatus;

import java.math.BigDecimal;

public class CafeTableView extends VerticalLayout {

    private Label balance;
    private Label income;
    private Grid cafeGrid;

    public CafeTableView() {
        setWidth("300px");
        setHeight("100%");
        setSpacing(true);
        setMargin(true);

        HorizontalLayout financialLayout = new HorizontalLayout();
        financialLayout.setWidth("100%");
        addComponent(financialLayout);

        balance = new Label("$ 123,456.78");
        balance.setCaption("Balance");
        financialLayout.addComponent(balance);
        income = new Label("$ 123,456.78");
        income.setCaption("24h income");
        financialLayout.addComponent(income);

        cafeGrid = new Grid(createContainer());
        cafeGrid.setSizeFull();
        addComponent(cafeGrid);
        setExpandRatio(cafeGrid, 1.0f);
    }

    private IndexedContainer createContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("cafe", String.class, "");
        container.addContainerProperty("24income", BigDecimal.class, BigDecimal.ZERO);
        container.addContainerProperty("status", CafeStatus.class, null);
        return container;
    }
}
