package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.Employee;
import org.vaadin.samples.cafetycoon.domain.EmployeeRepository;
import org.vaadin.samples.cafetycoon.domain.ServiceProvider;
import org.vaadin.samples.cafetycoon.domain.events.StaffMessageEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

@SuppressWarnings("serial")
public class PersonnelModel extends AbstractModel implements CafeSelectionModel.Observer {
	
	private final ServiceProvider<EmployeeRepository> employeeRepository;
	private final ObjectProperty<StaffMessageEvent> lastMessageReceived = new ObjectProperty<>(null,
			StaffMessageEvent.class);
	private final ObjectProperty<List<Employee>> employees = new ObjectProperty<>(new ArrayList<>()); 
	private CafeSelectionModel selectionModel;
	
	public PersonnelModel(ServiceProvider<EmployeeRepository> employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public void setCafeSelectionModel(CafeSelectionModel model) {
		if (selectionModel != null) {
			selectionModel.selection().removeValueChangeListener(this::cafeSelectionChanged);
		}
		selectionModel = model;
		if (selectionModel != null) {
			selectionModel.selection().addValueChangeListener(this::cafeSelectionChanged);
		}
	}

	private void cafeSelectionChanged(Property.ValueChangeEvent event) {
		access(this::updateEmployees, () -> lastMessageReceived.setValue(null));
	}

	public ObjectProperty<StaffMessageEvent> lastMessageReceived() {
		return lastMessageReceived;
	}

	public ObjectProperty<List<Employee>> employees() {
		return employees;
	}
	
	private void updateEmployees() {		
		Cafe cafe = selectionModel.selection().getValue();
		if (cafe != null) {
			employees.setValue(employeeRepository.get().getEmployeesForCafe(cafe));
		} else {
			employees.setValue(Collections.emptyList());
		}
	}
		
	@Subscribe
	protected void onStaffMessageEvent(StaffMessageEvent event) {
		if (event.getCafe().equals(selectionModel.selection().getValue())) {
			access(() -> lastMessageReceived.setValue(event));
		}
	}

	public interface Observer {
		void setPersonnelModel(PersonnelModel model);
	}
}
