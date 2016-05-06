package org.vaadin.samples.cafetycoon.domain.events;

import org.vaadin.samples.cafetycoon.domain.BaseDomainEvent;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.Employee;

@SuppressWarnings("serial")
public class StaffMessageEvent extends BaseDomainEvent {
	
	private final Employee employee;
	
	private final String message;

	public StaffMessageEvent(Employee employee, String message) {
		super();
		this.employee = employee;
		this.message = message;
	}

	public Cafe getCafe() {
		return employee.getCafe();
	}

	public Employee getEmployee() {
		return employee;
	}

	public String getMessage() {
		return message;
	}
}
