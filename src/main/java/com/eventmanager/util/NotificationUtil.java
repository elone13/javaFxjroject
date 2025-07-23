package com.eventmanager.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationUtil {
    public static void showNotification(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 