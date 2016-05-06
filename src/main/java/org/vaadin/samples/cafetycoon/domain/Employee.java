package org.vaadin.samples.cafetycoon.domain;

@SuppressWarnings("serial")
public class Employee extends BaseEntity {

    private final String name;
    private final String profileImageUrl;
    private final Cafe cafe;

    public Employee(String name, String profileImageUrl, Cafe cafe) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.cafe = cafe;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Cafe getCafe() {
        return cafe;
    }
}
