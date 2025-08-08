package com.eventmanager.controller;

import com.eventmanager.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClientModalController {
    @FXML private Label modalTitle;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField companyField;
    @FXML private TextField positionField;
    @FXML private TextArea addressArea;
    @FXML private TextArea notesArea;

    private Stage dialogStage;
    private boolean saved = false;
    private Client originalClient;
    private Client resultClient;

    @FXML
    public void initialize() {
        // Nothing yet
    }

    public void setDialogStage(Stage stage) { this.dialogStage = stage; }

    public void setClient(Client client) {
        this.originalClient = client;
        if (client != null) {
            modalTitle.setText("✎ Modifier le client");
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            phoneField.setText(client.getPhone());
        } else {
            modalTitle.setText("👥 Nouveau Client");
        }
    }

    public boolean isSaved() { return saved; }

    public Client getResultClient() { return resultClient; }

    @FXML
    private void handleSave() {
        if (!validateForm()) return;
        try {
            int id = (originalClient != null) ? originalClient.getId() : 0;
            resultClient = new Client(
                    id,
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim()
            );
            saved = true;
            close();
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }

    @FXML private void handleCancel() { close(); }
    @FXML private void handleClose() { close(); }

    private void close() {
        if (dialogStage != null) {
            dialogStage.close();
        } else if (nameField != null && nameField.getScene() != null && nameField.getScene().getWindow() instanceof Stage) {
            ((Stage) nameField.getScene().getWindow()).close();
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Le nom est obligatoire.");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showError("L'email est obligatoire.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
