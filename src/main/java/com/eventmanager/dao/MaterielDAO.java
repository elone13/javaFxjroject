package com.eventmanager.dao;

import com.eventmanager.model.Materiel;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielDAO {
    public void ajouterMateriel(Materiel materiel) throws SQLException {
        String sql = "INSERT INTO materiel (type, etat, quantite) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materiel.getType());
            stmt.setString(2, materiel.getEtat());
            stmt.setInt(3, materiel.getQuantite());
            stmt.executeUpdate();
        }
    }

    public List<Materiel> getAllMateriels() throws SQLException {
        List<Materiel> materiels = new ArrayList<>();
        String sql = "SELECT * FROM materiel";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                materiels.add(new Materiel(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("etat"),
                    rs.getInt("quantite")
                ));
            }
        }
        return materiels;
    }

    public void supprimerMateriel(int id) throws SQLException {
        String sql = "DELETE FROM materiel WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierMateriel(Materiel materiel) throws SQLException {
        String sql = "UPDATE materiel SET type = ?, etat = ?, quantite = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materiel.getType());
            stmt.setString(2, materiel.getEtat());
            stmt.setInt(3, materiel.getQuantite());
            stmt.setInt(4, materiel.getId());
            stmt.executeUpdate();
        }
    }
} 