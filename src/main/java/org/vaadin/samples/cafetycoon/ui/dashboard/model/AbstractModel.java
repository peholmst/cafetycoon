package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import org.vaadin.samples.cafetycoon.domain.ServiceProvider;

import com.google.common.eventbus.EventBus;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class AbstractModel implements Serializable {

	private UI ui;
	private ServiceProvider<EventBus> eventBus;

	public void attach(UI ui, ServiceProvider<EventBus> eventBus) {
		this.ui = ui;
		this.eventBus = eventBus;
		modelAttached();
		eventBus.get().register(this);
	}

	protected void modelAttached() {
	}

	protected void modelDetached() {
	}

	public void detach() {
		eventBus.get().unregister(this);
		modelDetached();
	}

	protected Optional<UI> getUI() {
		return Optional.ofNullable(ui);
	}

	protected void access(Runnable... runnables) {
		getUI().ifPresent(ui -> ui.access(() -> Arrays.asList(runnables).forEach(Runnable::run)));
	}
}
