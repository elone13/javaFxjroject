package com.eventmanager.dao;

import com.eventmanager.model.Salle;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {
    public void ajouterSalle(Salle salle) throws SQLException {
        String sql = "INSERT INTO salle (nom, capacite, localisation) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, salle.getNom());
            stmt.setInt(2, salle.getCapacite());
            stmt.setString(3, salle.getLocalisation());
            stmt.executeUpdate();
        }
    }

    public List<Salle> getAllSalles() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                salles.add(new Salle(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getInt("capacite"),
                    rs.getString("localisation")
                ));
            }
        }
        return salles;
    }

    public void supprimerSalle(int id) throws SQLException {
        String sql = "DELETE FROM salle WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierSalle(Salle salle) throws SQLException {
        String sql = "UPDATE salle SET nom = ?, capacite = ?, localisation = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, salle.getNom());
            stmt.setInt(2, salle.getCapacite());
            stmt.setString(3, salle.getLocalisation());
            stmt.setInt(4, salle.getId());
            stmt.executeUpdate();
        }
    }
} 