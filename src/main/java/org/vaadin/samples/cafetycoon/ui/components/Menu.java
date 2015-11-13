package org.vaadin.samples.cafetycoon.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import org.vaadin.samples.cafetycoon.ui.dashboard.Dashboard;
import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;
import org.vaadin.teemu.VaadinIcons;

public class Menu extends CssLayout {

    public Menu() {
        addStyleName("menu");
        setHeight("100%");
        setPrimaryStyleName(CafeTycoonTheme.MENU_ROOT);

        addMenuItem(VaadinIcons.DASHBOARD, "Dashboard", Dashboard.VIEW_NAME);
        addMenuItem(VaadinIcons.TABLE, "Cafés", null);
        addMenuItem(VaadinIcons.HOME, "Coffee Drinks", null);
        addMenuItem(VaadinIcons.PACKAGE, "Ingredients", null);
        addMenuItem(VaadinIcons.FACTORY, "Suppliers", null);
        addMenuItem(VaadinIcons.EXCHANGE, "Personnel", null);
    }

    private void addMenuItem(Resource icon, String caption, String viewToNavigateTo) {
        Button item = new Button(caption, icon);
        item.setPrimaryStyleName(CafeTycoonTheme.MENU_ITEM);
        if (viewToNavigateTo == null) {
            item.addClickListener(evt -> Notification.show("This feature is not implemented"));
        } else {
            item.addClickListener(evt -> getUI().getNavigator().navigateTo(viewToNavigateTo));
        }
        addComponent(item);
    }

}
