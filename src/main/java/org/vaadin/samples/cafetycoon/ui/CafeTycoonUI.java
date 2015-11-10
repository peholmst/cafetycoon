package org.vaadin.samples.cafetycoon.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.samples.cafetycoon.ui.components.Header;
import org.vaadin.samples.cafetycoon.ui.components.Menu;
import org.vaadin.samples.cafetycoon.ui.dashboard.Dashboard;
import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;

import javax.servlet.annotation.WebServlet;

@Push
@Widgetset("org.vaadin.samples.cafetycoon.CafeTycoonWidgetset")
@Theme(CafeTycoonTheme.THEME_NAME)
public class CafeTycoonUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Café Tycoon");

        final VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();

        final Header header = new Header();
        rootLayout.addComponent(header);

        final HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        rootLayout.addComponent(contentLayout);
        rootLayout.setExpandRatio(contentLayout, 1.0f);

        final Menu menu = new Menu();
        header.setMenu(menu);
        contentLayout.addComponent(menu);

        final Panel viewContainer = new Panel();
        viewContainer.addStyleName(CafeTycoonTheme.PANEL_BORDERLESS);
        viewContainer.setSizeFull();
        contentLayout.addComponent(viewContainer);
        contentLayout.setExpandRatio(viewContainer, 1.0f);

        setContent(rootLayout);

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.addView(Dashboard.VIEW_NAME, Dashboard.class);
        navigator.navigateTo(Dashboard.VIEW_NAME);
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = CafeTycoonUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }
}
