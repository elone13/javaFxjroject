package com.eventmanager.controller;

import com.eventmanager.dao.NotificationDAO;
import com.eventmanager.model.Notification;
import java.sql.SQLException;
import java.util.List;

public class NotificationController {
    private NotificationDAO notificationDAO = new NotificationDAO();

    public void ajouterNotification(Notification notification) throws SQLException {
        notificationDAO.ajouterNotification(notification);
    }

    public List<Notification> getAllNotifications() throws SQLException {
        return notificationDAO.getAllNotifications();
    }

    public void supprimerNotification(int id) throws SQLException {
        notificationDAO.supprimerNotification(id);
    }

    public void modifierNotification(Notification notification) throws SQLException {
        notificationDAO.modifierNotification(notification);
    }
} 