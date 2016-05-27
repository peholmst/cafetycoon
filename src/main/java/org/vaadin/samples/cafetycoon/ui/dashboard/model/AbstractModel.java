package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.vaadin.samples.cafetycoon.domain.ServiceProvider;

import com.google.common.eventbus.EventBus;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;

/**
 * Base class for application models. An application model is aware of both the
 * backend and the frontend to some extent. For example, the model can use
 * {@link Container}s and {@link Property}s to expose data that can be directly
 * bound to Vaadin UI components. The application model is also aware of
 * Vaadin's session locking mechanism and server push, see
 * {@link #access(Runnable...)}. Finally, the application model is subscribed to
 * an {@link EventBus} and can react to application events.
 */
@SuppressWarnings("serial")
public abstract class AbstractModel implements Serializable {

	private UI ui;
	private ServiceProvider<EventBus> eventBus;

	// One thing that is missing from this implementation is the handling of
	// event bus registration/unregistration when the model is
	// deserialized/serialized.
	// You only need this if you want to support session replication.

	/**
	 * Clients should call this method whenever this model is attached to a UI.
	 * This will also register the model with the specified event bus.
	 * 
	 * @param ui
	 *            the UI, or {@code null} if no UI is available (e.g. if the
	 *            model is being tested}.
	 * @param eventBus
	 *            the event bus to subscribe to, must not be {@code null}.
	 */
	public final void attach(UI ui, ServiceProvider<EventBus> eventBus) {
		if (this.eventBus == null) {
			this.ui = ui;
			this.eventBus = Objects.requireNonNull(eventBus);
			modelAttached();
			eventBus.get().register(this);
		}
	}

	/**
	 * This method is called by {@link #attach(UI, ServiceProvider)}. The
	 * default implementation does nothing, subclasses can override.
	 */
	protected void modelAttached() {
	}

	/**
	 * This method is called by {@link #detach()}. The default implementation
	 * does nothing, subclasses can override.
	 */
	protected void modelDetached() {
	}

	/**
	 * Clients should call this method whenever this model is detached from the
	 * UI.
	 */
	public final void detach() {
		if (eventBus != null) {
			eventBus.get().unregister(this);
			eventBus = null;
			modelDetached();
		}
	}

	/**
	 * Returns the UI, if the model has been attached to one.
	 */
	protected Optional<UI> getUI() {
		return Optional.ofNullable(ui);
	}

	/**
	 * If the model has been attached to a UI, this method will invoke all the
	 * {@code runnables} inside a call to {@link UI#access(Runnable)}.
	 * Otherwise, it will just run them immediately. This method exists because
	 * the model uses {@link Container}s and {@link Property}s to bind to the UI
	 * and whenever these are updated, the UI should be locked.
	 */
	protected void access(Runnable... runnables) {
		Runnable job = () -> Arrays.asList(runnables).forEach(Runnable::run);
		if (ui == null) {
			job.run();
		} else {
			ui.access(job);
		}
	}
}
