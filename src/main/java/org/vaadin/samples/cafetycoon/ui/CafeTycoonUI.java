package org.vaadin.samples.cafetycoon.ui;

import javax.servlet.annotation.WebServlet;

import org.vaadin.samples.cafetycoon.ui.dashboard.Dashboard;
import org.vaadin.samples.cafetycoon.ui.theme.CafeTycoonTheme;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;

/**
 * Main UI for the Cafe Tycoon application.
 */
@SuppressWarnings("serial")
@Push(transport = Transport.WEBSOCKET_XHR)
@Widgetset("org.vaadin.samples.cafetycoon.CafeTycoonWidgetset")
@Theme(CafeTycoonTheme.THEME_NAME)
public class CafeTycoonUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Café Tycoon");

        MainUIContent uiContent = new MainUIContent();
        setContent(uiContent);

        final Navigator navigator = new Navigator(this, (ViewDisplay) uiContent);
        navigator.addView(Dashboard.VIEW_NAME, Dashboard.class);
        navigator.addViewChangeListener(uiContent);
        navigator.navigateTo(Dashboard.VIEW_NAME);
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = CafeTycoonUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }
}
