package org.vaadin.samples.cafetycoon.domain;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class EmployeeRepository {

    private final List<Employee> employees;
    private final Client client = ClientBuilder.newClient();

    public EmployeeRepository(CafeRepository cafeRepository) {
        List<Employee> employees = new ArrayList<>();
        employees
            .addAll(cafeRepository.getCafes().stream().map(this::createEmployeeFromRest).collect(Collectors.toList()));
        this.employees = Collections.unmodifiableList(employees);
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    
    public List<Employee> getEmployeesForCafe(Cafe cafe) {
    	return Collections.emptyList(); // TODO Implement me!
    }

    private Employee createEmployeeFromRest(Cafe cafe) {
        String jsonResponse = client.target("http://uifaces.com/api/v1/random").request(MediaType.APPLICATION_JSON)
            .get(String.class);
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject json = reader.readObject();
            String username = json.getString("username");
            String image = json.getJsonObject("image_urls").getString("normal");
            return new Employee(username, image, cafe);
        }
    }
}
