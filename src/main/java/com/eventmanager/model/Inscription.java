package com.eventmanager.model;

public class Inscription {
    private int id;
    private int participantId;
    private int evenementId;
    private String statut;
    private int paiementId;

    public Inscription() {}

    public Inscription(int id, int participantId, int evenementId, String statut, int paiementId) {
        this.id = id;
        this.participantId = participantId;
        this.evenementId = evenementId;
        this.statut = statut;
        this.paiementId = paiementId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }
    public int getEvenementId() { return evenementId; }
    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public int getPaiementId() { return paiementId; }
    public void setPaiementId(int paiementId) { this.paiementId = paiementId; }
} 