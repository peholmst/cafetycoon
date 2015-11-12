package org.vaadin.samples.cafetycoon.ui.components;

import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;
import org.vaadin.teemu.VaadinIcons;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * Created by petterwork on 10/11/15.
 */
public class Header extends CssLayout {

    private Component menu;

    public Header() {
        setId("header");

        Button toggleMenu = new Button(VaadinIcons.MENU);
        toggleMenu.addClickListener(this::toggleMenu);
        addComponent(toggleMenu);

        Label title = new Label("Cafe Tycoon");
        title.addStyleName(CafeTycoonTheme.LABEL_H1);
        addComponent(title);
    }

    private void toggleMenu(Button.ClickEvent event) {
        if (menu != null) {
            menu.setVisible(!menu.isVisible());
        }
    }

    public void setMenu(Component menu) {
        this.menu = menu;
    }
}
