package com.eventmanager.dao;

import com.eventmanager.model.Inscription;
import com.eventmanager.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAO {
    
    public List<Inscription> getAllInscriptions() {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = """
            SELECT i.*, p.name as participant_name, e.title as event_title 
            FROM inscriptions i 
            JOIN participants p ON i.participant_id = p.id 
            JOIN events e ON i.event_id = e.id 
            ORDER BY i.inscription_date DESC
        """;
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Inscription inscription = new Inscription(
                    rs.getInt("id"),
                    rs.getInt("event_id"),
                    rs.getInt("participant_id"),
                    rs.getTimestamp("inscription_date").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getString("payment_status"),
                    rs.getBigDecimal("amount_paid"),
                    rs.getString("notes")
                );
                inscription.setParticipantName(rs.getString("participant_name"));
                inscription.setEventTitle(rs.getString("event_title"));
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }

    public List<Inscription> getInscriptionsByEvent(int eventId) {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = """
            SELECT i.*, p.name as participant_name, e.title as event_title 
            FROM inscriptions i 
            JOIN participants p ON i.participant_id = p.id 
            JOIN events e ON i.event_id = e.id 
            WHERE i.event_id = ? 
            ORDER BY i.inscription_date DESC
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Inscription inscription = new Inscription(
                    rs.getInt("id"),
                    rs.getInt("event_id"),
                    rs.getInt("participant_id"),
                    rs.getTimestamp("inscription_date").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getString("payment_status"),
                    rs.getBigDecimal("amount_paid"),
                    rs.getString("notes")
                );
                inscription.setParticipantName(rs.getString("participant_name"));
                inscription.setEventTitle(rs.getString("event_title"));
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }

    public boolean addInscription(Inscription inscription) {
        String sql = "INSERT INTO inscriptions (event_id, participant_id, status, payment_status, amount_paid, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscription.getEventId());
            stmt.setInt(2, inscription.getParticipantId());
            stmt.setString(3, inscription.getStatus());
            stmt.setString(4, inscription.getPaymentStatus());
            stmt.setBigDecimal(5, inscription.getAmountPaid());
            stmt.setString(6, inscription.getNotes());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInscription(Inscription inscription) {
        String sql = "UPDATE inscriptions SET status=?, payment_status=?, amount_paid=?, notes=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inscription.getStatus());
            stmt.setString(2, inscription.getPaymentStatus());
            stmt.setBigDecimal(3, inscription.getAmountPaid());
            stmt.setString(4, inscription.getNotes());
            stmt.setInt(5, inscription.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteInscription(int id) {
        String sql = "DELETE FROM inscriptions WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getParticipantCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM inscriptions WHERE event_id = ? AND status != 'ANNULE'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal getTotalRevenue(int eventId) {
        String sql = "SELECT SUM(amount_paid) FROM inscriptions WHERE event_id = ? AND payment_status = 'PAID'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
