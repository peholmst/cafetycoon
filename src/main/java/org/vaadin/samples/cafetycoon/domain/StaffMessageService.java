package org.vaadin.samples.cafetycoon.domain;

import org.vaadin.samples.cafetycoon.domain.events.StaffMessageEvent;

import com.google.common.eventbus.EventBus;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class StaffMessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffMessageService.class);
	private final EventBus eventBus;

	public StaffMessageService(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void sendMessage(Employee sender, String message) {
		LOGGER.info("Sending message from {}", sender);
		eventBus.post(new StaffMessageEvent(sender, message));
	}
}
