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

/**
 * Application model that contains the employees and the last received message
 * of the currently selected cafe. This model is an observer of
 * {@link CafeSelectionModel}.
 */
@SuppressWarnings("serial")
public class PersonnelModel extends AbstractModel implements CafeSelectionModel.Observer {

	private final ServiceProvider<EmployeeRepository> employeeRepository;
	private final ObjectProperty<StaffMessageEvent> lastMessageReceived = new ObjectProperty<>(null,
			StaffMessageEvent.class);
	private final ObjectProperty<List<Employee>> employees = new ObjectProperty<>(new ArrayList<>());
	private CafeSelectionModel selectionModel;

	/**
	 * Creates a new {@code PersonnelModel}. The constructor parameters are all
	 * required backend services.
	 */
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

	/**
	 * Returns a property containing the last received message from the
	 * currently selected cafe.
	 */
	public ObjectProperty<StaffMessageEvent> lastMessageReceived() {
		return lastMessageReceived;
	}

	/**
	 * Returns a property containing a list of the employees in the currently
	 * selected cafe. A container is not used since this list is not bound to
	 * any components that use containers.
	 */
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

	/**
	 * Updates the {@code lastMessageReceived} property if the message was sent
	 * from the currently selected cafe.
	 */
	@Subscribe
	protected void onStaffMessageEvent(StaffMessageEvent event) {
		if (event.getCafe().equals(selectionModel.selection().getValue())) {
			access(() -> lastMessageReceived.setValue(event));
		}
	}

	/**
	 * Convenience interface for model observers, not a requirement.
	 */
	public interface Observer {
		void setPersonnelModel(PersonnelModel model);
	}
}
