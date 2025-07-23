package com.eventmanager.model;

public class Prestataire {
    private int id;
    private String nom;
    private String service;
    private String contact;

    public Prestataire() {}

    public Prestataire(int id, String nom, String service, String contact) {
        this.id = id;
        this.nom = nom;
        this.service = service;
        this.contact = contact;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
} 