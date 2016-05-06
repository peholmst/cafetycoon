package org.vaadin.samples.cafetycoon.domain;

public class StaffMessageGenerator {
	
	private final EmployeeRepository employeeRepository;
	private final StaffMessageService staffMessageService;
		
	public StaffMessageGenerator(EmployeeRepository employeeRepository, StaffMessageService staffMessageService) {
		this.employeeRepository = employeeRepository;
		this.staffMessageService = staffMessageService;
	}

	public synchronized void start() {
		// TODO Implement me
	}
	
	public synchronized void stop() {
		// TOOD Implement me
	}
}
