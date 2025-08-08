package com.eventmanager.dao;

import com.eventmanager.model.Event;
import com.eventmanager.util.DBUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(createEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public boolean addEvent(Event event) {
        String sql = "INSERT INTO events (title, description, event_date, start_time, end_time, location, client_id, provider, status, max_participants, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setEventParameters(stmt, event);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET title=?, description=?, event_date=?, start_time=?, end_time=?, location=?, client_id=?, provider=?, status=?, max_participants=?, price=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setEventParameters(stmt, event);
            stmt.setInt(12, event.getId());
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

    public Event getEventById(int id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return createEventFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> getEventsByStatus(String status) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE status = ? ORDER BY event_date";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(createEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    private Event createEventFromResultSet(ResultSet rs) throws SQLException {
        LocalTime startTime = null;
        LocalTime endTime = null;
        
        Time startTimeSQL = rs.getTime("start_time");
        Time endTimeSQL = rs.getTime("end_time");
        
        if (startTimeSQL != null) startTime = startTimeSQL.toLocalTime();
        if (endTimeSQL != null) endTime = endTimeSQL.toLocalTime();
        
        return new Event(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("event_date").toLocalDate(),
            startTime,
            endTime,
            rs.getString("location"),
            rs.getInt("client_id"),
            rs.getString("provider"),
            rs.getString("status"),
            rs.getInt("max_participants"),
            rs.getBigDecimal("price")
        );
    }

    private void setEventParameters(PreparedStatement stmt, Event event) throws SQLException {
        stmt.setString(1, event.getTitle());
        stmt.setString(2, event.getDescription());
        stmt.setDate(3, Date.valueOf(event.getEventDate()));
        
        if (event.getStartTime() != null) {
            stmt.setTime(4, Time.valueOf(event.getStartTime()));
        } else {
            stmt.setNull(4, Types.TIME);
        }
        
        if (event.getEndTime() != null) {
            stmt.setTime(5, Time.valueOf(event.getEndTime()));
        } else {
            stmt.setNull(5, Types.TIME);
        }
        
        stmt.setString(6, event.getLocation());
        stmt.setInt(7, event.getClientId());
        stmt.setString(8, event.getProvider());
        stmt.setString(9, event.getStatus());
        stmt.setInt(10, event.getMaxParticipants());
        stmt.setBigDecimal(11, event.getPrice());
    }
}