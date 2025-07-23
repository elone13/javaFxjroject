package com.eventmanager.controller;

import com.eventmanager.dao.PrestataireDAO;
import com.eventmanager.model.Prestataire;
import java.sql.SQLException;
import java.util.List;

public class PrestataireController {
    private PrestataireDAO prestataireDAO = new PrestataireDAO();

    public void ajouterPrestataire(Prestataire prestataire) throws SQLException {
        prestataireDAO.ajouterPrestataire(prestataire);
    }

    public List<Prestataire> getAllPrestataires() throws SQLException {
        return prestataireDAO.getAllPrestataires();
    }

    public void supprimerPrestataire(int id) throws SQLException {
        prestataireDAO.supprimerPrestataire(id);
    }

    public void modifierPrestataire(Prestataire prestataire) throws SQLException {
        prestataireDAO.modifierPrestataire(prestataire);
    }
} 