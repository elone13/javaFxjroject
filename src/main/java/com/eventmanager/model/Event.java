package com.eventmanager.model;

import java.time.LocalDate;

public class Event {
    private int id;
    private String title;
    private LocalDate eventDate;
    private int clientId;
    private String provider;
    private String status;

    public Event(int id, String title, LocalDate eventDate, int clientId, String provider, String status) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.clientId = clientId;
        this.provider = provider;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 