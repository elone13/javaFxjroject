package com.eventmanager.dao;

import com.eventmanager.model.Prestataire;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestataireDAO {
    public void ajouterPrestataire(Prestataire prestataire) throws SQLException {
        String sql = "INSERT INTO prestataire (nom, service, contact) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prestataire.getNom());
            stmt.setString(2, prestataire.getService());
            stmt.setString(3, prestataire.getContact());
            stmt.executeUpdate();
        }
    }

    public List<Prestataire> getAllPrestataires() throws SQLException {
        List<Prestataire> prestataires = new ArrayList<>();
        String sql = "SELECT * FROM prestataire";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prestataires.add(new Prestataire(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("service"),
                    rs.getString("contact")
                ));
            }
        }
        return prestataires;
    }

    public void supprimerPrestataire(int id) throws SQLException {
        String sql = "DELETE FROM prestataire WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierPrestataire(Prestataire prestataire) throws SQLException {
        String sql = "UPDATE prestataire SET nom = ?, service = ?, contact = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prestataire.getNom());
            stmt.setString(2, prestataire.getService());
            stmt.setString(3, prestataire.getContact());
            stmt.setInt(4, prestataire.getId());
            stmt.executeUpdate();
        }
    }
} 