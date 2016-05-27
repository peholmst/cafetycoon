package org.vaadin.samples.cafetycoon.ui.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

/**
 * This is a wrapper around a {@link EventContainer} that maps the events to
 * {@link DataSeriesItem}s and adds them to or removes them from a
 * {@link DataSeries} as they are added to or removed from the container.
 */
@SuppressWarnings("serial")
public class EventContainerDataSeriesFactory<T> implements Serializable {

	private final ItemFactory<T> itemFactory;
	private EventContainer<T> eventContainer;
	private Map<T, DataSeriesItem> eventToItemMap = new HashMap<>();
	private DataSeries dataSeries = new DataSeries();

	/**
	 * Creates a new {@code EventContainerDataSeriesFactory} with the specified
	 * {@code itemFactory}. The {@link EventContainer} must be explicitly set
	 * using {@link #setEventContainer(EventContainer)}.
	 */
	public EventContainerDataSeriesFactory(ItemFactory<T> itemFactory) {
		this.itemFactory = event -> {
			DataSeriesItem item = itemFactory.createDataSeriesItem(event);
			eventToItemMap.put(event, item);
			return item;
		};
	}

	/**
	 * Creates a new {@code EventContainerDataSeriesFactory} with the specified
	 * {@code itemFactory} and {@code container}.
	 */
	public EventContainerDataSeriesFactory(ItemFactory<T> itemFactory, EventContainer<T> container) {
		this(itemFactory);
		setEventContainer(container);
	}

	/**
	 * Sets the backing event container and subscribes to it. If an event
	 * container had already been set, it will be unsubscribed from.
	 */
	public void setEventContainer(EventContainer<T> eventContainer) {
		if (this.eventContainer != null) {
			this.eventContainer.removeEventAddedListener(this::onEventAdded);
			this.eventContainer.removeEventRemovedListener(this::onEventRemoved);
		}
		this.eventContainer = eventContainer;
		eventToItemMap.clear();
		if (this.eventContainer != null) {
			this.eventContainer.addEventAddedListener(this::onEventAdded);
			this.eventContainer.addEventRemovedListener(this::onEventRemoved);
			dataSeries.setData(this.eventContainer.getEvents().stream().map(itemFactory::createDataSeriesItem)
					.collect(Collectors.toList()));
		}
	}

	/**
	 * Returns the backing event container, or {@code null} if not set.
	 */
	public EventContainer<T> getEventContainer() {
		return eventContainer;
	}

	private void onEventAdded(EventContainer<T> container, Collection<T> events) {
		events.forEach(event -> {
			DataSeriesItem item = itemFactory.createDataSeriesItem(event);
			dataSeries.add(item);
		});
	}

	private void onEventRemoved(EventContainer<T> container, Collection<T> events) {
		events.forEach(event -> {
			DataSeriesItem item = eventToItemMap.remove(event);
			dataSeries.remove(item);
		});
	}

	/**
	 * Returns the {@link DataSeries} that contains the items of the backing
	 * container.
	 */
	public DataSeries getDataSeries() {
		return dataSeries;
	}

	/**
	 * Interface for a factory that is used to create {@link DataSeriesItem}s
	 * from the events in a {@link EventContainer}.
	 */
	@FunctionalInterface
	public interface ItemFactory<T> extends Serializable {
		DataSeriesItem createDataSeriesItem(T event);
	}
}
