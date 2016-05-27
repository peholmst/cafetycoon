package org.vaadin.samples.cafetycoon.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import org.vaadin.samples.cafetycoon.ui.dashboard.Dashboard;
import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;
import org.vaadin.teemu.VaadinIcons;

/**
 * Main menu of the application, built using the Valo menu styles. In this
 * example application, the menu is statically populated. In a real application,
 * you would probably want to use some kind of dynamic registration mechanism so
 * that new items can be automatically added to the menu when new modules are
 * added to the application.
 */
@SuppressWarnings("serial")
public class Menu extends CssLayout {

	private boolean hidden = true;

	public Menu() {
		addStyleName("menu");
		setPrimaryStyleName(CafeTycoonTheme.MENU_ROOT);

		addMenuItem(VaadinIcons.DASHBOARD, "Dashboard", Dashboard.VIEW_NAME);
		addMenuItem(VaadinIcons.TABLE, "Cafés", null);
		addMenuItem(VaadinIcons.HOME, "Coffee Drinks", null);
		addMenuItem(VaadinIcons.PACKAGE, "Ingredients", null);
		addMenuItem(VaadinIcons.FACTORY, "Suppliers", null);
		addMenuItem(VaadinIcons.EXCHANGE, "Personnel", null);
		hide();
	}

	private void addMenuItem(Resource icon, String caption, String viewToNavigateTo) {
		Button item = new Button(caption, icon);
		item.setPrimaryStyleName(CafeTycoonTheme.MENU_ITEM);
		if (viewToNavigateTo == null) {
			item.addClickListener(evt -> Notification.show("This feature is not implemented"));
		} else {
			item.addClickListener(evt -> {
				getUI().getNavigator().navigateTo(viewToNavigateTo);
				hide();
			});
		}
		addComponent(item);
	}

	public void show() {
		removeStyleName("hidden");
		addStyleName("visible");
		hidden = false;
	}

	public void hide() {
		removeStyleName("visible");
		addStyleName("hidden");
		hidden = true;
	}

	public boolean isHidden() {
		return hidden;
	}
}
