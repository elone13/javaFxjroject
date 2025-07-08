package com.eventmanager.dao;

import com.eventmanager.model.Event;
import com.eventmanager.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDate("event_date").toLocalDate(),
                    rs.getInt("client_id"),
                    rs.getString("provider"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public boolean addEvent(Event event) {
        String sql = "INSERT INTO events (title, event_date, client_id, provider, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setDate(2, Date.valueOf(event.getEventDate()));
            stmt.setInt(3, event.getClientId());
            stmt.setString(4, event.getProvider());
            stmt.setString(5, event.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET title=?, event_date=?, client_id=?, provider=?, status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setDate(2, Date.valueOf(event.getEventDate()));
            stmt.setInt(3, event.getClientId());
            stmt.setString(4, event.getProvider());
            stmt.setString(5, event.getStatus());
            stmt.setInt(6, event.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEvent(int id) {
        String sql = "DELETE FROM events WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
} 