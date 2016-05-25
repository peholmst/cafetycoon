package org.vaadin.samples.cafetycoon.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import org.vaadin.samples.cafetycoon.ui.dashboard.Dashboard;
import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;
import org.vaadin.teemu.VaadinIcons;

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
