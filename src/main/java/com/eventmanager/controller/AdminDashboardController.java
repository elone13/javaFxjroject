package com.eventmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class AdminDashboardController {
    @FXML private Button eventButton;
    @FXML private Button clientButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        // Rien à initialiser pour l'instant
    }

    @FXML
    private void handleEvent(ActionEvent event) {
        switchScene("/fxml/events.fxml");
    }

    @FXML
    private void handleClient(ActionEvent event) {
        switchScene("/fxml/clients.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        com.eventmanager.util.SessionUtil.clear();
        switchScene("/fxml/login.fxml");
    }

    private void switchScene(String fxmlPath) {
        try {
            Stage stage = (Stage) eventButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 