package com.eventmanager.model;

public class Materiel {
    private int id;
    private String type;
    private String etat;
    private int quantite;

    public Materiel() {}

    public Materiel(int id, String type, String etat, int quantite) {
        this.id = id;
        this.type = type;
        this.etat = etat;
        this.quantite = quantite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
} 