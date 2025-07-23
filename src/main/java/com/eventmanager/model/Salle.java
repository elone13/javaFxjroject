package com.eventmanager.model;

public class Salle {
    private int id;
    private String nom;
    private int capacite;
    private String localisation;

    public Salle() {}

    public Salle(int id, String nom, int capacite, String localisation) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = localisation;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
} 