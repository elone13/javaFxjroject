package com.eventmanager.model;

import java.time.LocalDate;

public class Paiement {
    private int id;
    private double montant;
    private LocalDate date;
    private int participantId;
    private int evenementId;

    public Paiement() {}

    public Paiement(int id, double montant, LocalDate date, int participantId, int evenementId) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.participantId = participantId;
        this.evenementId = evenementId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }
    public int getEvenementId() { return evenementId; }
    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }
} 