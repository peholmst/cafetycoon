package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import org.vaadin.samples.cafetycoon.domain.Cafe;

import com.vaadin.data.util.ObjectProperty;

/**
 * Created by petterwork on 12/11/15.
 */
public class CafeSelectionModel {

    private ObjectProperty<Cafe> selection = new ObjectProperty<>(null, Cafe.class);

    public ObjectProperty<Cafe> getSelection() {
        return selection;
    }
}
