package org.vaadin.samples.cafetycoon.ui.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The data views in Cafe Tycoon are basically showing events in different ways.
 * Events never change once they have happened and their order also remains the
 * same. This is a special container (non-Vaadin) that notifies listener
 * whenever events are added to or removed from it.
 */
@SuppressWarnings("serial")
public class EventContainer<T> implements Serializable {

	private List<T> events = new ArrayList<>();
	private Set<EventAddedListener<T>> eventAddedListeners = new HashSet<>();
	private Set<EventRemovedListener<T>> eventRemovedListeners = new HashSet<>();

	/**
	 * Returns the events of this container in an unmodifiable list.
	 */
	public List<T> getEvents() {
		return Collections.unmodifiableList(events);
	}

	/**
	 * Sets the events of this container, notifying the listeners about any
	 * added or removed events.
	 */
	public void setEvents(List<T> events) {
		Set<T> eventsToRemove = new HashSet<>(this.events);
		List<T> eventsToAdd = new ArrayList<>();
		events.forEach(e -> {
			if (!eventsToRemove.remove(e)) {
				eventsToAdd.add(e);
			}
		});
		if (eventsToRemove.size() > 0) {
			this.events.removeAll(eventsToRemove);
			notifyRemoved(eventsToRemove);
		}
		if (eventsToAdd.size() > 0) {
			this.events.addAll(eventsToAdd);
			notifyAdded(eventsToAdd);
		}
	}

	private void notifyAdded(Collection<T> events) {
		Collection<T> unmodifiable = Collections.unmodifiableCollection(events);
		new HashSet<>(eventAddedListeners).forEach(l -> l.onEventAdded(this, unmodifiable));
	}

	private void notifyRemoved(Collection<T> events) {
		Collection<T> unmodifiable = Collections.unmodifiableCollection(events);
		new HashSet<>(eventRemovedListeners).forEach(l -> l.onEventRemoved(this, unmodifiable));
	}

	public void addEventAddedListener(EventAddedListener<T> listener) {
		eventAddedListeners.add(listener);
	}

	public void removeEventAddedListener(EventAddedListener<T> listener) {
		eventAddedListeners.remove(listener);
	}

	public void addEventRemovedListener(EventRemovedListener<T> listener) {
		eventRemovedListeners.add(listener);
	}

	public void removeEventRemovedListener(EventRemovedListener<T> listener) {
		eventRemovedListeners.remove(listener);
	}

	/**
	 * Interface for listeners that are notified of added events.
	 */
	@FunctionalInterface
	public interface EventAddedListener<T> extends Serializable {
		/**
		 * @param container
		 *            the container to which events were added.
		 * @param events
		 *            all the events that were added.
		 */
		void onEventAdded(EventContainer<T> container, Collection<T> events);
	}

	/**
	 * Interface for listeners that are notified of removed events.
	 */
	@FunctionalInterface
	public interface EventRemovedListener<T> extends Serializable {
		/**
		 * @param container
		 *            the container from which events were removed.
		 * @param events
		 *            all the events that were removed.
		 */
		void onEventRemoved(EventContainer<T> container, Collection<T> events);
	}
}
