package com.eventmanager.controller;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.Event;
import com.eventmanager.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
 

public class EventController {
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, LocalDate> dateColumn;
    @FXML private TableColumn<Event, String> locationColumn;
    @FXML private TableColumn<Event, String> statusColumn;
    @FXML private TableColumn<Event, BigDecimal> priceColumn;

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField locationField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private TextField providerField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField maxParticipantsField;
    @FXML private TextField priceField;

    private final EventDAO eventDAO = new EventDAO();
    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
    setupColumns();
    // Skip setting up optional controls; dialogs handle their own combos
    loadEvents();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        dateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEventDate()));
        locationColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation()));
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        priceColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPrice()));
    }

    private void loadEvents() {
        eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
        eventTable.setItems(eventList);
    }

    @FXML
    private void handleAdd() {
        // Open modal for creation
        Event result = openEventModal(null);
        if (result != null) {
            if (eventDAO.addEvent(result)) {
                loadEvents();
                showSuccess("Événement ajouté avec succès!");
            } else {
                showError("Erreur lors de l'ajout de l'événement.");
            }
        }
    }

    @FXML
    private void handleEdit() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Veuillez sélectionner un événement à modifier."); return; }
        Event edited = openEventModal(selected);
        if (edited != null) {
            if (eventDAO.updateEvent(edited)) {
                loadEvents();
                showSuccess("Événement modifié avec succès!");
            } else {
                showError("Erreur lors de la modification de l'événement.");
            }
        }
    }

    @FXML
    private void handleDelete() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Veuillez sélectionner un événement à supprimer."); return; }
        Boolean confirmed = openDeleteModal("Supprimer l'événement", java.util.Collections.singletonList(selected.getTitle()));
        if (Boolean.TRUE.equals(confirmed)) {
            if (eventDAO.deleteEvent(selected.getId())) {
                loadEvents();
                clearForm();
                showSuccess("Événement supprimé avec succès!");
            } else {
                showError("Erreur lors de la suppression de l'événement.");
            }
        }
    }

    @FXML
    private void handleClear() {
    clearForm();
    eventTable.getSelectionModel().clearSelection();
    }

    private Event openEventModal(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/event_modal.fxml"));
            Parent root = loader.load();
            EventModalController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setEvent(event);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(event == null ? "Nouvel événement" : "Modifier l'événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            if (controller.isSaved()) {
                return controller.getResultEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la modale: " + e.getMessage());
        }
        return null;
    }

    private Boolean openDeleteModal(String message, java.util.List<String> items) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/delete_confirmation_modal.fxml"));
            Parent root = loader.load();
            DeleteConfirmationModalController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setItems(items, message);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            return controller.isConfirmed();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la confirmation: " + e.getMessage());
        }
        return false;
    }

    @FXML
    private void handleBack() {
        try {
            String role = com.eventmanager.util.SessionUtil.getUserRole();
            String dashboard = "/fxml/employee_dashboard.fxml";
            if ("ADMIN".equals(role)) {
                dashboard = "/fxml/admin_dashboard.fxml";
            }
            Stage stage = (Stage) eventTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(dashboard));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour au tableau de bord.");
        }
    }

    private void clearForm() {
    if (titleField != null) titleField.clear();
    if (descriptionArea != null) descriptionArea.clear();
    if (datePicker != null) datePicker.setValue(null);
    if (startTimeField != null) startTimeField.clear();
    if (endTimeField != null) endTimeField.clear();
    if (locationField != null) locationField.clear();
    if (clientComboBox != null) clientComboBox.setValue(null);
    if (providerField != null) providerField.clear();
    if (statusComboBox != null) statusComboBox.setValue("PLANIFIE");
    if (maxParticipantsField != null) maxParticipantsField.setText("100");
    if (priceField != null) priceField.setText("0.00");
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

    @FXML
    private void handleOpenFilters() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/filter_modal.fxml"));
            Parent root = loader.load();
            FilterModalController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Filtres avancés");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            // Optionally, apply filters when controller.isApplied()
        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la modale de filtres: " + e.getMessage());
        }
    }
} 