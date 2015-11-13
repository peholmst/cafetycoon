package org.vaadin.samples.cafetycoon.ui.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;
import org.vaadin.teemu.VaadinIcons;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class Header extends CssLayout implements ViewChangeListener {

    private Component menu;
    private Label viewTitle;

    public Header() {
        addStyleName("header");
        setWidth("100%");

        Button toggleMenu = new Button(VaadinIcons.MENU);
        toggleMenu.addStyleName("menu-button");
        toggleMenu.addClickListener(this::toggleMenu);
        addComponent(toggleMenu);

        viewTitle = new Label();
        viewTitle.setSizeUndefined();
        viewTitle.addStyleName("view-title");
        addComponent(viewTitle);
    }

    private void toggleMenu(Button.ClickEvent event) {
        if (menu != null) {
            menu.setVisible(!menu.isVisible());
        }
    }

    public void setMenu(Component menu) {
        this.menu = menu;
    }

    public void setNavigator(Navigator navigator) {
        navigator.addViewChangeListener(this);
    }

    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        View view = event.getNewView();
        if (view instanceof TitledElement) {
            viewTitle.setValue(((TitledElement) view).getTitle());
        }
    }
}
