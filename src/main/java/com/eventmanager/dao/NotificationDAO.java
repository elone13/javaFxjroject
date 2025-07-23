package com.eventmanager.dao;

import com.eventmanager.model.Notification;
import com.eventmanager.util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    public void ajouterNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notification (type, destinataire, contenu, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notification.getType());
            stmt.setString(2, notification.getDestinataire());
            stmt.setString(3, notification.getContenu());
            stmt.setTimestamp(4, Timestamp.valueOf(notification.getDate()));
            stmt.executeUpdate();
        }
    }

    public List<Notification> getAllNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(new Notification(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("destinataire"),
                    rs.getString("contenu"),
                    rs.getTimestamp("date").toLocalDateTime()
                ));
            }
        }
        return notifications;
    }

    public void supprimerNotification(int id) throws SQLException {
        String sql = "DELETE FROM notification WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierNotification(Notification notification) throws SQLException {
        String sql = "UPDATE notification SET type = ?, destinataire = ?, contenu = ?, date = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notification.getType());
            stmt.setString(2, notification.getDestinataire());
            stmt.setString(3, notification.getContenu());
            stmt.setTimestamp(4, Timestamp.valueOf(notification.getDate()));
            stmt.setInt(5, notification.getId());
            stmt.executeUpdate();
        }
    }
} 