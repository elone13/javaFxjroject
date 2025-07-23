package com.eventmanager.dao;

import com.eventmanager.model.Paiement;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {
    public void ajouterPaiement(Paiement paiement) throws SQLException {
        String sql = "INSERT INTO paiement (montant, date, participant_id, evenement_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setInt(3, paiement.getParticipantId());
            stmt.setInt(4, paiement.getEvenementId());
            stmt.executeUpdate();
        }
    }

    public List<Paiement> getAllPaiements() throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                paiements.add(new Paiement(
                    rs.getInt("id"),
                    rs.getDouble("montant"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("participant_id"),
                    rs.getInt("evenement_id")
                ));
            }
        }
        return paiements;
    }

    public void supprimerPaiement(int id) throws SQLException {
        String sql = "DELETE FROM paiement WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierPaiement(Paiement paiement) throws SQLException {
        String sql = "UPDATE paiement SET montant = ?, date = ?, participant_id = ?, evenement_id = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setInt(3, paiement.getParticipantId());
            stmt.setInt(4, paiement.getEvenementId());
            stmt.setInt(5, paiement.getId());
            stmt.executeUpdate();
        }
    }
} 