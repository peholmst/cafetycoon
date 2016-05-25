package org.vaadin.samples.cafetycoon.domain;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class EmployeeRepository {

	private final Map<Cafe, List<Employee>> employees = new HashMap<>();
	private final List<Employee> allEmployees = new ArrayList<>();
    private final Client client = ClientBuilder.newClient();

    public EmployeeRepository(CafeRepository cafeRepository) {
    	cafeRepository.getCafes().forEach(cafe -> {
    		List<Employee> list = new ArrayList<>();
    		employees.put(cafe, list);
    		for (int i = 0; i < 3; ++i) {
    			Employee employee = createEmployeeFromRest(cafe);
    			list.add(employee);
    			allEmployees.add(employee);
    		}
    	});
    }

    public List<Employee> getEmployees() {
    	return Collections.unmodifiableList(allEmployees);
    }
    
    public List<Employee> getEmployeesForCafe(Cafe cafe) {
    	return Collections.unmodifiableList(employees.getOrDefault(cafe, Collections.emptyList()));
    }

    private Employee createEmployeeFromRest(Cafe cafe) {
        String jsonResponse = client.target("http://uifaces.com/api/v1/random").request(MediaType.APPLICATION_JSON)
            .get(String.class);
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject json = reader.readObject();
            String username = json.getString("username");
            String image = json.getJsonObject("image_urls").getString("normal");
            System.out.println(image);
            return new Employee(username, image, cafe);
        }
    }
}
