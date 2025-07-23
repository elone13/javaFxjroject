package com.eventmanager.controller;

import com.eventmanager.dao.PaiementDAO;
import com.eventmanager.model.Paiement;
import java.sql.SQLException;
import java.util.List;

public class PaiementController {
    private PaiementDAO paiementDAO = new PaiementDAO();

    public void ajouterPaiement(Paiement paiement) throws SQLException {
        paiementDAO.ajouterPaiement(paiement);
    }

    public List<Paiement> getAllPaiements() throws SQLException {
        return paiementDAO.getAllPaiements();
    }

    public void supprimerPaiement(int id) throws SQLException {
        paiementDAO.supprimerPaiement(id);
    }

    public void modifierPaiement(Paiement paiement) throws SQLException {
        paiementDAO.modifierPaiement(paiement);
    }
} 