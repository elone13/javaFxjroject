package com.eventmanager.controller;

import com.eventmanager.dao.InscriptionDAO;
import com.eventmanager.model.Inscription;
import java.sql.SQLException;
import java.util.List;

public class InscriptionController {
    private InscriptionDAO inscriptionDAO = new InscriptionDAO();

    public void ajouterInscription(Inscription inscription) throws SQLException {
        inscriptionDAO.ajouterInscription(inscription);
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