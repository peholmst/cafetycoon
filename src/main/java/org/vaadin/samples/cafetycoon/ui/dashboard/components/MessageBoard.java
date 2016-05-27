package org.vaadin.samples.cafetycoon.ui.dashboard.components;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.samples.cafetycoon.domain.Employee;
import org.vaadin.samples.cafetycoon.domain.events.StaffMessageEvent;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.PersonnelModel;

import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Audio;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * A custom component that shows the employees of a cafe and displays any
 * messages that they send. This component observes a {@link PersonnelModel}.
 */
@SuppressWarnings("serial")
public class MessageBoard extends CustomComponent implements PersonnelModel.Observer {

	private PersonnelModel model;

	private CssLayout employeesLayout;

	private Map<Employee, MessageLayout> messageLayouts = new HashMap<>();

	private StaffMessageEvent lastShownMessage;

	private Audio notificationSound;

	public MessageBoard() {
		employeesLayout = new CssLayout();
		employeesLayout.setWidth("100%");
		employeesLayout.addStyleName("employees");
		setCompositionRoot(employeesLayout);

		notificationSound = new Audio();
		notificationSound.setCaption(null);
		notificationSound.setShowControls(false);
		notificationSound.setSource(new ThemeResource("airhorn.mp3"));
	}

	@Override
	public void setPersonnelModel(PersonnelModel model) {
		// We're only setting this once and the model and the component have the
		// same scope -> no need to clean up
		this.model = model;
		this.model.employees().addValueChangeListener(this::employeesChanged);
		this.model.lastMessageReceived().addValueChangeListener(this::messageChanged);
	}

	private void employeesChanged(Property.ValueChangeEvent event) {
		employeesLayout.removeAllComponents();
		employeesLayout.addComponent(notificationSound);
		messageLayouts.clear();
		model.employees().getValue().forEach(this::addEmployee);
	}

	private void messageChanged(Property.ValueChangeEvent event) {
		showMessage(model.lastMessageReceived().getValue());
	}

	private void showMessage(StaffMessageEvent message) {
		if (lastShownMessage != null) {
			messageLayouts.get(lastShownMessage.getEmployee()).setVisible(false);
		}
		if (message != null) {
			messageLayouts.get(message.getEmployee()).setMessage(message);
			notificationSound.play();
		}
		lastShownMessage = message;
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

		MessageLayout message = new MessageLayout();
		message.setVisible(false);
		layout.addComponent(message);
		messageLayouts.put(employee, message);

		employeesLayout.addComponent(layout);
	}

	private class MessageLayout extends CssLayout {
		private Label timestamp;
		private Label content;

		MessageLayout() {
			addStyleName("employee-message-layout");
			content = new Label();
			content.setSizeUndefined();
			content.addStyleName("employee-message-content");
			addComponent(content);

			timestamp = new Label();
			timestamp.setSizeUndefined();
			timestamp.addStyleName("employee-message-ts");
			addComponent(timestamp);
			addLayoutClickListener(evt -> setVisible(false));
		}

		void setMessage(StaffMessageEvent message) {
			setVisible(message != null);
			if (message != null) {
				timestamp.setValue(ZonedDateTime.ofInstant(message.getInstant(), ZoneId.systemDefault()).toLocalTime()
						.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				content.setValue(message.getMessage());
			}
		}
	}
}
