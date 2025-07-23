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
    @FXML private Button salleButton;
    @FXML private Button materielButton;
    @FXML private Button prestataireButton;
    @FXML private Button paiementButton;
    @FXML private Button inscriptionButton;
    @FXML private Button notificationButton;

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

    @FXML
    private void handleSalle(ActionEvent event) {
        switchScene("/fxml/salles.fxml");
    }

    @FXML
    private void handleMateriel(ActionEvent event) {
        switchScene("/fxml/materiels.fxml");
    }

    @FXML
    private void handlePrestataire(ActionEvent event) {
        switchScene("/fxml/prestataires.fxml");
    }

    @FXML
    private void handlePaiement(ActionEvent event) {
        switchScene("/fxml/paiements.fxml");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        switchScene("/fxml/inscriptions.fxml");
    }

    @FXML
    private void handleNotification(ActionEvent event) {
        switchScene("/fxml/notifications.fxml");
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