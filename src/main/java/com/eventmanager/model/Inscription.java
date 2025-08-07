package com.eventmanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Inscription {
    private int id;
    private int eventId;
    private int participantId;
    private LocalDateTime inscriptionDate;
    private String status;
    private String paymentStatus;
    private BigDecimal amountPaid;
    private String notes;
    private String participantName; // For display purposes
    private String eventTitle; // For display purposes

    public Inscription(int id, int eventId, int participantId, LocalDateTime inscriptionDate, 
                      String status, String paymentStatus, BigDecimal amountPaid, String notes) {
        this.id = id;
        this.eventId = eventId;
        this.participantId = participantId;
        this.inscriptionDate = inscriptionDate;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.amountPaid = amountPaid;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }

    public LocalDateTime getInscriptionDate() { return inscriptionDate; }
    public void setInscriptionDate(LocalDateTime inscriptionDate) { this.inscriptionDate = inscriptionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
}
