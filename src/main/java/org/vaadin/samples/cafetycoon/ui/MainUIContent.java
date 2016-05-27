package org.vaadin.samples.cafetycoon.ui;

import org.vaadin.samples.cafetycoon.ui.utils.TitledElement;
import org.vaadin.teemu.VaadinIcons;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * Content of the {@link CafeTycoonUI}, designed using Vaadin Designer.
 */
@SuppressWarnings("serial")
public class MainUIContent extends MainDesign implements ViewDisplay, ViewChangeListener {

	public MainUIContent() {
		super();
		toggleMenu.setIcon(VaadinIcons.MENU);
		toggleMenu.addClickListener(this::toggleMenu);
	}

	@Override
	public void showView(View view) {
		viewContainer.setContent((Component) view);
	}

	private void toggleMenu(Button.ClickEvent event) {
		if (menu.isHidden()) {
			menu.show();
		} else {
			menu.hide();
		}
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
