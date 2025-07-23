package com.eventmanager.model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String type;
    private String destinataire;
    private String contenu;
    private LocalDateTime date;

    public Notification() {}

    public Notification(int id, String type, String destinataire, String contenu, LocalDateTime date) {
        this.id = id;
        this.type = type;
        this.destinataire = destinataire;
        this.contenu = contenu;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
} 