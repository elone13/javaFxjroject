package com.eventmanager.controller;

import com.eventmanager.dao.ClientDAO;
import com.eventmanager.model.Client;
import com.eventmanager.model.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalTime;

public class EventModalController {
    @FXML private Label modalTitle;
    @FXML private TextField titleField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField locationField;
    @FXML private TextField providerField;
    @FXML private TextField priceField;
    @FXML private TextField maxParticipantsField;
    @FXML private TextArea descriptionArea;

    private Stage dialogStage;
    private boolean saved = false;
    private Event originalEvent;
    private Event resultEvent;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        // Setup status options
        statusComboBox.setItems(FXCollections.observableArrayList(
                "PLANIFIE", "EN_COURS", "TERMINE", "ANNULE"));
        statusComboBox.setValue("PLANIFIE");

        // Load clients
        clientComboBox.setItems(FXCollections.observableArrayList(clientDAO.getAllClients()));

        // Defaults
        maxParticipantsField.setText("100");
        priceField.setText("0.00");
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setEvent(Event event) {
        this.originalEvent = event;
        if (event != null) {
            modalTitle.setText("✎ Modifier l'événement");
            titleField.setText(event.getTitle());
            descriptionArea.setText(event.getDescription());
            datePicker.setValue(event.getEventDate());
            startTimeField.setText(event.getStartTime() != null ? event.getStartTime().toString() : "");
            endTimeField.setText(event.getEndTime() != null ? event.getEndTime().toString() : "");
            locationField.setText(event.getLocation());
            providerField.setText(event.getProvider());
            statusComboBox.setValue(event.getStatus());
            maxParticipantsField.setText(String.valueOf(event.getMaxParticipants()));
            priceField.setText(event.getPrice() != null ? event.getPrice().toString() : "0.00");

            // select client by id
            if (clientComboBox.getItems() != null) {
                for (Client c : clientComboBox.getItems()) {
                    if (c.getId() == event.getClientId()) {
                        clientComboBox.setValue(c);
                        break;
                    }
                }
            }
        } else {
            modalTitle.setText("📅 Nouvel Événement");
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Event getResultEvent() {
        return resultEvent;
    }

    @FXML
    private void handleSave() {
        if (!validateForm()) return;
        try {
            LocalTime startTime = null;
            LocalTime endTime = null;
            if (!startTimeField.getText().trim().isEmpty()) {
                startTime = LocalTime.parse(startTimeField.getText().trim());
            }
            if (!endTimeField.getText().trim().isEmpty()) {
                endTime = LocalTime.parse(endTimeField.getText().trim());
            }

            int id = (originalEvent != null) ? originalEvent.getId() : 0;

            resultEvent = new Event(
                    id,
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    datePicker.getValue(),
                    startTime,
                    endTime,
                    locationField.getText().trim(),
                    clientComboBox.getValue().getId(),
                    providerField.getText().trim(),
                    statusComboBox.getValue(),
                    Integer.parseInt(maxParticipantsField.getText().trim()),
                    new BigDecimal(priceField.getText().trim())
            );

            saved = true;
            close();
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() { close(); }

    @FXML
    private void handleClose() { close(); }

    private void close() {
        if (dialogStage != null) {
            dialogStage.close();
        } else {
            // Fallback
            if (titleField != null && titleField.getScene() != null && titleField.getScene().getWindow() instanceof Stage) {
                ((Stage) titleField.getScene().getWindow()).close();
            }
        }
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Le titre est obligatoire.");
            return false;
        }
        if (datePicker.getValue() == null) {
            showError("La date est obligatoire.");
            return false;
        }
        if (clientComboBox.getValue() == null) {
            showError("Veuillez sélectionner un client.");
            return false;
        }
        try {
            if (!startTimeField.getText().trim().isEmpty()) {
                LocalTime.parse(startTimeField.getText().trim());
            }
            if (!endTimeField.getText().trim().isEmpty()) {
                LocalTime.parse(endTimeField.getText().trim());
            }
        } catch (Exception e) {
            showError("Format d'heure invalide (HH:MM).");
            return false;
        }
        try {
            Integer.parseInt(maxParticipantsField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Le nombre maximum de participants doit être un entier.");
            return false;
        }
        try {
            new BigDecimal(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Le prix doit être un nombre valide.");
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
