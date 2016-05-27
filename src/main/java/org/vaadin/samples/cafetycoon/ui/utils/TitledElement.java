package org.vaadin.samples.cafetycoon.ui.utils;

/**
 * This interface is currently only used by views so that their titles can be
 * displayed in the main UI. It could be used by any element that has a title.
 */
public interface TitledElement {

	/**
	 * Returns the title of this element.
	 */
	String getTitle();
}
