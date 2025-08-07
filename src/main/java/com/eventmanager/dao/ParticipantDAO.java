package com.eventmanager.dao;

import com.eventmanager.model.Participant;
import com.eventmanager.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAO {
    
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participants ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                participants.add(new Participant(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("type"),
                    rs.getString("company")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    public boolean addParticipant(Participant participant) {
        String sql = "INSERT INTO participants (name, email, phone, type, company) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, participant.getName());
            stmt.setString(2, participant.getEmail());
            stmt.setString(3, participant.getPhone());
            stmt.setString(4, participant.getType());
            stmt.setString(5, participant.getCompany());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateParticipant(Participant participant) {
        String sql = "UPDATE participants SET name=?, email=?, phone=?, type=?, company=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, participant.getName());
            stmt.setString(2, participant.getEmail());
            stmt.setString(3, participant.getPhone());
            stmt.setString(4, participant.getType());
            stmt.setString(5, participant.getCompany());
            stmt.setInt(6, participant.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteParticipant(int id) {
        String sql = "DELETE FROM participants WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Participant getParticipantById(int id) {
        String sql = "SELECT * FROM participants WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Participant(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("type"),
                    rs.getString("company")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
