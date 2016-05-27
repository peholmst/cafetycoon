package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;

import org.vaadin.samples.cafetycoon.domain.Cafe;

import com.vaadin.data.util.ObjectProperty;

/**
 * A simple model that keeps track of the currently selected cafe. This model is
 * used because cafes can be selected in different ways and there are several
 * views that need to be updated when the selection changes.
 */
@SuppressWarnings("serial")
public class CafeSelectionModel implements Serializable {

	private final ObjectProperty<Cafe> selection = new ObjectProperty<>(null, Cafe.class);

	/**
	 * Convenience interface for model observers, not a requirement.
	 */
	public interface Observer {
		void setCafeSelectionModel(CafeSelectionModel model);
	}

	/**
	 * Returns a property containing the currently selected cafe.
	 */
	public ObjectProperty<Cafe> selection() {
		return selection;
	}
}
