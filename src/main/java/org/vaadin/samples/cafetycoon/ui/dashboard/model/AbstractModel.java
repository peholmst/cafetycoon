package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Optional;

import org.vaadin.samples.cafetycoon.domain.Services;

import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class AbstractModel implements Serializable {

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private UI ui;

    public void attach(UI ui) {
        this.ui = ui;
        modelAttached();
        Services.getInstance().getEventBus().register(this);
    }

    protected void modelAttached() {
    }

    public void detach() {
        Services.getInstance().getEventBus().unregister(this);
    }

    protected Optional<UI> getUI() {
        return Optional.ofNullable(ui);
    }

    protected void access(Runnable runnable) {
        getUI().ifPresent(ui -> ui.access(runnable));
    }

    @Deprecated
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    @Deprecated
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Deprecated
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    @Deprecated
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Deprecated
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Deprecated
    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Deprecated
    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Deprecated
    protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    @Deprecated
    protected void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    @Deprecated
    protected void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
        changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }
}
