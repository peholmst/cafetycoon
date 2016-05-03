package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;

import org.vaadin.samples.cafetycoon.domain.Cafe;

import com.vaadin.data.util.ObjectProperty;

@SuppressWarnings("serial")
public class CafeSelectionModel implements Serializable {

    private final ObjectProperty<Cafe> selection = new ObjectProperty<>(null, Cafe.class);

    public interface Observer {
    	void setCafeSelectionModel(CafeSelectionModel model);
    }
    
    public ObjectProperty<Cafe> selection() {
        return selection;
    }
}
