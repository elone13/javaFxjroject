package com.eventmanager.controller;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EventController {
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, LocalDate> dateColumn;
    @FXML private TableColumn<Event, Integer> clientColumn;
    @FXML private TableColumn<Event, String> providerColumn;
    @FXML private TableColumn<Event, String> statusColumn;

    @FXML private TextField titleField;
    @FXML private DatePicker datePicker;
    @FXML private TextField clientField;
    @FXML private TextField providerField;
    @FXML private TextField statusField;

    private final EventDAO eventDAO = new EventDAO();
    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEventDate()));
        clientColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getClientId()).asObject());
        providerColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProvider()));
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        loadEvents();
    }

    private void loadEvents() {
        eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
        eventTable.setItems(eventList);
    }

    @FXML
    private void handleAdd() {
        try {
            String title = titleField.getText();
            LocalDate date = datePicker.getValue();
            int clientId = Integer.parseInt(clientField.getText());
            String provider = providerField.getText();
            String status = statusField.getText();
            Event event = new Event(0, title, date, clientId, provider, status);
            if (eventDAO.addEvent(event)) {
                loadEvents();
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Erreur", "Vérifiez les champs du formulaire.");
        }
    }

    @FXML
    private void handleEdit() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setTitle(titleField.getText());
                selected.setEventDate(datePicker.getValue());
                selected.setClientId(Integer.parseInt(clientField.getText()));
                selected.setProvider(providerField.getText());
                selected.setStatus(statusField.getText());
                if (eventDAO.updateEvent(selected)) {
                    loadEvents();
                    clearForm();
                }
            } catch (Exception e) {
                showAlert("Erreur", "Vérifiez les champs du formulaire.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (eventDAO.deleteEvent(selected.getId())) {
                loadEvents();
                clearForm();
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
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
        }
    }

    private void clearForm() {
        titleField.clear();
        datePicker.setValue(null);
        clientField.clear();
        providerField.clear();
        statusField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 