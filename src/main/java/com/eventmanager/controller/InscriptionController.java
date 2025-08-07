package com.eventmanager.controller;

import com.eventmanager.dao.InscriptionDAO;
import com.eventmanager.dao.EventDAO;
import com.eventmanager.dao.ParticipantDAO;
import com.eventmanager.model.Inscription;
import com.eventmanager.model.Event;
import com.eventmanager.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InscriptionController {
    @FXML private TableView<Inscription> inscriptionTable;
    @FXML private TableColumn<Inscription, Integer> idColumn;
    @FXML private TableColumn<Inscription, String> eventColumn;
    @FXML private TableColumn<Inscription, String> participantColumn;
    @FXML private TableColumn<Inscription, String> dateColumn;
    @FXML private TableColumn<Inscription, String> statusColumn;
    @FXML private TableColumn<Inscription, String> paymentColumn;
    @FXML private TableColumn<Inscription, BigDecimal> amountColumn;

    @FXML private ComboBox<Event> eventComboBox;
    @FXML private ComboBox<Participant> participantComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> paymentStatusComboBox;
    @FXML private TextField amountField;
    @FXML private TextArea notesArea;
    @FXML private Label statsLabel;

    private final InscriptionDAO inscriptionDAO = new InscriptionDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final ParticipantDAO participantDAO = new ParticipantDAO();
    private ObservableList<Inscription> inscriptionList;

    @FXML
    public void initialize() {
        setupColumns();
        setupComboBoxes();
        setupSelectionListener();
        loadData();
        updateStats();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        eventColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEventTitle()));
        participantColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getParticipantName()));
        dateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getInscriptionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        paymentColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaymentStatus()));
        amountColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmountPaid()));
    }

    private void setupComboBoxes() {
        statusComboBox.setItems(FXCollections.observableArrayList(
            "INSCRIT", "CONFIRME", "PRESENT", "ABSENT"));
        statusComboBox.setValue("INSCRIT");

        paymentStatusComboBox.setItems(FXCollections.observableArrayList(
            "PENDING", "PAID", "REFUNDED"));
        paymentStatusComboBox.setValue("PENDING");

        // Load events and participants
        eventComboBox.setItems(FXCollections.observableArrayList(eventDAO.getAllEvents()));
        participantComboBox.setItems(FXCollections.observableArrayList(participantDAO.getAllParticipants()));
    }

    private void setupSelectionListener() {
        inscriptionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadData() {
        inscriptionList = FXCollections.observableArrayList(inscriptionDAO.getAllInscriptions());
        inscriptionTable.setItems(inscriptionList);
    }

    private void populateForm(Inscription inscription) {
        // Find and select the corresponding event and participant
        for (Event event : eventComboBox.getItems()) {
            if (event.getId() == inscription.getEventId()) {
                eventComboBox.setValue(event);
                break;
            }
        }
        
        for (Participant participant : participantComboBox.getItems()) {
            if (participant.getId() == inscription.getParticipantId()) {
                participantComboBox.setValue(participant);
                break;
            }
        }

        statusComboBox.setValue(inscription.getStatus());
        paymentStatusComboBox.setValue(inscription.getPaymentStatus());
        amountField.setText(inscription.getAmountPaid() != null ? 
            inscription.getAmountPaid().toString() : "0.00");
        notesArea.setText(inscription.getNotes());
    }

    @FXML
    private void handleAdd() {
        if (validateForm()) {
            try {
                Inscription inscription = new Inscription(0,
                    eventComboBox.getValue().getId(),
                    participantComboBox.getValue().getId(),
                    LocalDateTime.now(),
                    statusComboBox.getValue(),
                    paymentStatusComboBox.getValue(),
                    new BigDecimal(amountField.getText().trim()),
                    notesArea.getText().trim()
                );

                if (inscriptionDAO.addInscription(inscription)) {
                    loadData();
                    updateStats();
                    clearForm();
                    showSuccess("Inscription ajoutée avec succès!");
                } else {
                    showError("Erreur lors de l'ajout de l'inscription.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEdit() {
        Inscription selected = inscriptionTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateForm()) {
            try {
                selected.setStatus(statusComboBox.getValue());
                selected.setPaymentStatus(paymentStatusComboBox.getValue());
                selected.setAmountPaid(new BigDecimal(amountField.getText().trim()));
                selected.setNotes(notesArea.getText().trim());

                if (inscriptionDAO.updateInscription(selected)) {
                    loadData();
                    updateStats();
                    clearForm();
                    showSuccess("Inscription modifiée avec succès!");
                } else {
                    showError("Erreur lors de la modification de l'inscription.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        } else if (selected == null) {
            showError("Veuillez sélectionner une inscription à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Inscription selected = inscriptionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer l'inscription");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette inscription ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (inscriptionDAO.deleteInscription(selected.getId())) {
                    loadData();
                    updateStats();
                    clearForm();
                    showSuccess("Inscription supprimée avec succès!");
                } else {
                    showError("Erreur lors de la suppression de l'inscription.");
                }
            }
        } else {
            showError("Veuillez sélectionner une inscription à supprimer.");
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
        inscriptionTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            String role = com.eventmanager.util.SessionUtil.getUserRole();
            String dashboard = "/fxml/employee_dashboard.fxml";
            if ("ADMIN".equals(role)) {
                dashboard = "/fxml/admin_dashboard.fxml";
            }
            Stage stage = (Stage) inscriptionTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(dashboard));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour au tableau de bord.");
        }
    }

    private boolean validateForm() {
        if (eventComboBox.getValue() == null) {
            showError("Veuillez sélectionner un événement.");
            return false;
        }
        if (participantComboBox.getValue() == null) {
            showError("Veuillez sélectionner un participant.");
            return false;
        }
        try {
            new BigDecimal(amountField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Le montant doit être un nombre valide.");
            return false;
        }
        return true;
    }

    private void updateStats() {
        int totalInscriptions = inscriptionList.size();
        long confirmedCount = inscriptionList.stream()
            .filter(i -> "CONFIRME".equals(i.getStatus()) || "PRESENT".equals(i.getStatus()))
            .count();
        
        BigDecimal totalRevenue = inscriptionList.stream()
            .filter(i -> "PAID".equals(i.getPaymentStatus()))
            .map(Inscription::getAmountPaid)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        statsLabel.setText(String.format(
            "Total: %d inscriptions | Confirmées: %d | Revenus: %.2f €",
            totalInscriptions, confirmedCount, totalRevenue));
    }

    private void clearForm() {
        eventComboBox.setValue(null);
        participantComboBox.setValue(null);
        statusComboBox.setValue("INSCRIT");
        paymentStatusComboBox.setValue("PENDING");
        amountField.setText("0.00");
        notesArea.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
