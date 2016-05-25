package org.vaadin.samples.cafetycoon.ui.dashboard.components;

import org.vaadin.samples.cafetycoon.domain.Employee;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.PersonnelModel;

import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class MessageBoard extends CustomComponent implements PersonnelModel.Observer {

	private PersonnelModel model;

	private CssLayout employeesLayout;

	public MessageBoard() {		
		employeesLayout = new CssLayout();
		employeesLayout.setWidth("100%");
		setCompositionRoot(employeesLayout);
	}

	@Override
	public void setPersonnelModel(PersonnelModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up
		this.model = model;
		this.model.employees().addValueChangeListener(this::employeesChanged);
	}

	private void employeesChanged(Property.ValueChangeEvent event) {
		employeesLayout.removeAllComponents();
		model.employees().getValue().forEach(this::addEmployee);
	}

	private void addEmployee(Employee employee) {
		CssLayout layout = new CssLayout();
		layout.addStyleName("employee");
		
		final Image image = new Image();
		image.addStyleName("employee-picture");
		layout.addComponent(image);

		final Label name = new Label(employee.getName());
		name.setSizeUndefined();
		name.addStyleName("employee-name");
		layout.addComponent(name);

		String pictureUrl = employee.getProfileImageUrl();
		if (pictureUrl != null) {
			image.setSource(new ExternalResource(pictureUrl));
		}

		employeesLayout.addComponent(layout);
	}
}
