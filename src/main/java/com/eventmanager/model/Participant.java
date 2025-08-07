package com.eventmanager.model;

public class Participant {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String company;

    public Participant(int id, String name, String email, String phone, String type, String company) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.company = company;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
