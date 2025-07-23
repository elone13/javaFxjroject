package com.eventmanager.controller;

import com.eventmanager.dao.MaterielDAO;
import com.eventmanager.model.Materiel;
import java.sql.SQLException;
import java.util.List;

public class MaterielController {
    private MaterielDAO materielDAO = new MaterielDAO();

    public void ajouterMateriel(Materiel materiel) throws SQLException {
        materielDAO.ajouterMateriel(materiel);
    }

    public List<Materiel> getAllMateriels() throws SQLException {
        return materielDAO.getAllMateriels();
    }

    public void supprimerMateriel(int id) throws SQLException {
        materielDAO.supprimerMateriel(id);
    }

    public void modifierMateriel(Materiel materiel) throws SQLException {
        materielDAO.modifierMateriel(materiel);
    }
} 