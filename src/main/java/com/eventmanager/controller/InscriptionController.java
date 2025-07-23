package com.eventmanager.controller;

import com.eventmanager.dao.InscriptionDAO;
import com.eventmanager.model.Inscription;
import com.eventmanager.util.EmailUtil;
import com.eventmanager.util.NotificationUtil;
import jakarta.mail.MessagingException;
import java.sql.SQLException;
import java.util.List;

public class InscriptionController {
    private InscriptionDAO inscriptionDAO = new InscriptionDAO();

    public void ajouterInscription(Inscription inscription) throws SQLException {
        inscriptionDAO.ajouterInscription(inscription);
        // Envoi d'email et notification
        try {
            // À adapter : récupérer l'email du participant selon participantId
            String email = "participant@email.com";
            EmailUtil.sendEmail(email, "Confirmation d'inscription", "Votre inscription à l'événement a bien été prise en compte.");
        } catch (MessagingException e) {
            NotificationUtil.showNotification("Inscription ajoutée, mais l'email n'a pas pu être envoyé.");
            return;
        }
        NotificationUtil.showNotification("Inscription ajoutée et email envoyé au participant.");
    }

    public List<Inscription> getAllInscriptions() throws SQLException {
        return inscriptionDAO.getAllInscriptions();
    }

    public void supprimerInscription(int id) throws SQLException {
        inscriptionDAO.supprimerInscription(id);
    }

    public void modifierInscription(Inscription inscription) throws SQLException {
        inscriptionDAO.modifierInscription(inscription);
    }
} 