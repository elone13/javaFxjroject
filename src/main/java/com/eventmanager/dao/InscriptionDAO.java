package com.eventmanager.dao;

import com.eventmanager.model.Inscription;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAO {
    public void ajouterInscription(Inscription inscription) throws SQLException {
        String sql = "INSERT INTO inscription (participant_id, evenement_id, statut, paiement_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscription.getParticipantId());
            stmt.setInt(2, inscription.getEvenementId());
            stmt.setString(3, inscription.getStatut());
            stmt.setInt(4, inscription.getPaiementId());
            stmt.executeUpdate();
        }
    }

    public List<Inscription> getAllInscriptions() throws SQLException {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscription";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                inscriptions.add(new Inscription(
                    rs.getInt("id"),
                    rs.getInt("participant_id"),
                    rs.getInt("evenement_id"),
                    rs.getString("statut"),
                    rs.getInt("paiement_id")
                ));
            }
        }
        return inscriptions;
    }

    public void supprimerInscription(int id) throws SQLException {
        String sql = "DELETE FROM inscription WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierInscription(Inscription inscription) throws SQLException {
        String sql = "UPDATE inscription SET participant_id = ?, evenement_id = ?, statut = ?, paiement_id = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscription.getParticipantId());
            stmt.setInt(2, inscription.getEvenementId());
            stmt.setString(3, inscription.getStatut());
            stmt.setInt(4, inscription.getPaiementId());
            stmt.setInt(5, inscription.getId());
            stmt.executeUpdate();
        }
    }
} 