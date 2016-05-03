package org.vaadin.samples.cafetycoon.ui.dashboard;

import java.math.BigDecimal;
import java.util.Optional;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.Services;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeModel;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.CafeSelectionModel;

import com.vaadin.data.Property;
import com.vaadin.ui.*;

public class CafeDetailsView extends VerticalLayout {

    private CafeSelectionModel selectionModel;
    private CafeModel model;

    private Label name;
    private Label address;

    // TODO Personnel

    private CafeSalesGrid salesData;
    private CafeBeanStockChart beanStock;

    private Button restock50;
    private Button restock100;
    private Button restock200;

    public CafeDetailsView(CafeModel model, CafeSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
        this.model = model;
        addStyleName("cafe-details-view");
        setWidth("400px");
        setHeight("100%");
        setMargin(true);
        setSpacing(true);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.addStyleName("cafe-details-header");
        addComponent(header);

        name = new Label();
        name.addStyleName("cafe-name");
        name.setSizeUndefined();
        header.addComponent(name);

        address = new Label();
        address.addStyleName("cafe-address");
        address.setSizeUndefined();
        header.addComponent(address);

        // TODO Status

        // TODO Personnel

        salesData = new CafeSalesGrid(model);
        addComponent(salesData);
        setExpandRatio(salesData, 1);

        beanStock = new CafeBeanStockChart(model);
        addComponent(beanStock);
        setExpandRatio(beanStock, 1);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("restock-buttons");
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        addComponent(buttons);

        buttons.addComponent(restock50 = new Button("Restock 50", this::restock50));
        buttons.addComponent(restock100 = new Button("Restock 100", this::restock100));
        buttons.addComponent(restock200 = new Button("Restock 200", this::restock200));
    }

    @Override
    public void attach() {
        super.attach();
        selectionModel.selection().addValueChangeListener(this::cafeSelectionChanged);
        cafeSelectionChanged(null);
    }

    @Override
    public void detach() {
        selectionModel.selection().removeValueChangeListener(this::cafeSelectionChanged);
        super.detach();
    }

    private void cafeSelectionChanged(Property.ValueChangeEvent event) {
        setCafe(Optional.ofNullable(selectionModel.selection().getValue()));
    }

    private void setCafe(Optional<Cafe> cafe) {
        setVisible(cafe.isPresent());
        cafe.ifPresent(c -> {
            name.setValue(c.getName());
            address.setValue(c.getAddress());
        });
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
        Cafe cafe = selectionModel.selection().getValue();
        if (cafe != null) {
            Services.getInstance().getStockService().restock(cafe, new BigDecimal(amount));
        }
    }

}
