package com.eventmanager.model;

import java.math.BigDecimal;

public class Resource {
    private int id;
    private String name;
    private String type;
    private Integer capacity;
    private String description;
    private BigDecimal pricePerHour;
    private String status;
    private String location;

    public Resource(int id, String name, String type, Integer capacity, String description, 
                   BigDecimal pricePerHour, String status, String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.description = description;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
