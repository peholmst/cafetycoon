package org.vaadin.samples.cafetycoon.ui.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

@SuppressWarnings("serial")
public class EventContainerDataSeriesFactory<T> implements Serializable {

	private final ItemFactory<T> itemFactory;
	private EventContainer<T> eventContainer;
	private Map<T, DataSeriesItem> eventToItemMap = new HashMap<>();
	private DataSeries dataSeries = new DataSeries();
	
	public EventContainerDataSeriesFactory(ItemFactory<T> itemFactory) {
		this.itemFactory = event -> {
			DataSeriesItem item = itemFactory.createDataSeriesItem(event);
			eventToItemMap.put(event, item);
			return item;
		};
	}
	
	public EventContainerDataSeriesFactory(ItemFactory<T> itemFactory, EventContainer<T> container) {
		this(itemFactory);
		setEventContainer(container);
	}
	
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
			dataSeries.setData(this.eventContainer.getEvents().stream().map(itemFactory::createDataSeriesItem).collect(Collectors.toList()));
		}
	}
	
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
	
	public DataSeries getDataSeries() {
		return dataSeries;
	}
	
	@FunctionalInterface
	public interface ItemFactory<T> extends Serializable {
		DataSeriesItem createDataSeriesItem(T event);
	}
}
