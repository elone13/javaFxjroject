package com.eventmanager.controller;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.dao.ClientDAO;
import com.eventmanager.model.Event;
import com.eventmanager.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private final ClientDAO clientDAO = new ClientDAO();
    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
        setupColumns();
        setupComboBoxes();
        setupSelectionListener();
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

    private void setupComboBoxes() {
        statusComboBox.setItems(FXCollections.observableArrayList(
            "PLANIFIE", "EN_COURS", "TERMINE", "ANNULE"));
        statusComboBox.setValue("PLANIFIE");

        // Load clients
        clientComboBox.setItems(FXCollections.observableArrayList(clientDAO.getAllClients()));
    }

    private void setupSelectionListener() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadEvents() {
        eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
        eventTable.setItems(eventList);
    }

    private void populateForm(Event event) {
        titleField.setText(event.getTitle());
        descriptionArea.setText(event.getDescription());
        datePicker.setValue(event.getEventDate());
        startTimeField.setText(event.getStartTime() != null ? event.getStartTime().toString() : "");
        endTimeField.setText(event.getEndTime() != null ? event.getEndTime().toString() : "");
        locationField.setText(event.getLocation());
        
        // Find and select the corresponding client
        for (Client client : clientComboBox.getItems()) {
            if (client.getId() == event.getClientId()) {
                clientComboBox.setValue(client);
                break;
            }
        }
        
        providerField.setText(event.getProvider());
        statusComboBox.setValue(event.getStatus());
        maxParticipantsField.setText(String.valueOf(event.getMaxParticipants()));
        priceField.setText(event.getPrice() != null ? event.getPrice().toString() : "0.00");
    }

    @FXML
    private void handleAdd() {
        if (validateForm()) {
            try {
                LocalTime startTime = null;
                LocalTime endTime = null;
                
                if (!startTimeField.getText().trim().isEmpty()) {
                    startTime = LocalTime.parse(startTimeField.getText().trim());
                }
                if (!endTimeField.getText().trim().isEmpty()) {
                    endTime = LocalTime.parse(endTimeField.getText().trim());
                }

                Event event = new Event(0,
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

                if (eventDAO.addEvent(event)) {
                    loadEvents();
                    clearForm();
                    showSuccess("Événement ajouté avec succès!");
                } else {
                    showError("Erreur lors de l'ajout de l'événement.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEdit() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateForm()) {
            try {
                LocalTime startTime = null;
                LocalTime endTime = null;
                
                if (!startTimeField.getText().trim().isEmpty()) {
                    startTime = LocalTime.parse(startTimeField.getText().trim());
                }
                if (!endTimeField.getText().trim().isEmpty()) {
                    endTime = LocalTime.parse(endTimeField.getText().trim());
                }

                selected.setTitle(titleField.getText().trim());
                selected.setDescription(descriptionArea.getText().trim());
                selected.setEventDate(datePicker.getValue());
                selected.setStartTime(startTime);
                selected.setEndTime(endTime);
                selected.setLocation(locationField.getText().trim());
                selected.setClientId(clientComboBox.getValue().getId());
                selected.setProvider(providerField.getText().trim());
                selected.setStatus(statusComboBox.getValue());
                selected.setMaxParticipants(Integer.parseInt(maxParticipantsField.getText().trim()));
                selected.setPrice(new BigDecimal(priceField.getText().trim()));

                if (eventDAO.updateEvent(selected)) {
                    loadEvents();
                    clearForm();
                    showSuccess("Événement modifié avec succès!");
                } else {
                    showError("Erreur lors de la modification de l'événement.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        } else if (selected == null) {
            showError("Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer l'événement");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + selected.getTitle() + "\" ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (eventDAO.deleteEvent(selected.getId())) {
                    loadEvents();
                    clearForm();
                    showSuccess("Événement supprimé avec succès!");
                } else {
                    showError("Erreur lors de la suppression de l'événement.");
                }
            }
        } else {
            showError("Veuillez sélectionner un événement à supprimer.");
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
        eventTable.getSelectionModel().clearSelection();
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

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        datePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        locationField.clear();
        clientComboBox.setValue(null);
        providerField.clear();
        statusComboBox.setValue("PLANIFIE");
        maxParticipantsField.setText("100");
        priceField.setText("0.00");
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