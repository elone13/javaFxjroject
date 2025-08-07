package com.eventmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;

public class EmployeeDashboardController {
    @FXML private Button eventButton;
    @FXML private Button clientButton;
    @FXML private Button participantButton;
    @FXML private Button inscriptionButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        // Initialization if needed
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
    private void handleParticipant(ActionEvent event) {
        switchScene("/fxml/participants.fxml");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        switchScene("/fxml/inscriptions.fxml");
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
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger la page demandée.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 