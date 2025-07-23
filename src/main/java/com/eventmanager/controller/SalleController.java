package com.eventmanager.controller;

import com.eventmanager.dao.SalleDAO;
import com.eventmanager.model.Salle;
import java.sql.SQLException;
import java.util.List;

public class SalleController {
    private SalleDAO salleDAO = new SalleDAO();

    public void ajouterSalle(Salle salle) throws SQLException {
        salleDAO.ajouterSalle(salle);
    }

    public List<Salle> getAllSalles() throws SQLException {
        return salleDAO.getAllSalles();
    }

    public void supprimerSalle(int id) throws SQLException {
        salleDAO.supprimerSalle(id);
    }

    public void modifierSalle(Salle salle) throws SQLException {
        salleDAO.modifierSalle(salle);
    }
} 