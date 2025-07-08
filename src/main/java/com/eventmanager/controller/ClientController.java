package com.eventmanager.controller;

import com.eventmanager.dao.ClientDAO;
import com.eventmanager.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClientController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Integer> idColumn;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, String> phoneColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private final ClientDAO clientDAO = new ClientDAO();
    private ObservableList<Client> clientList;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        loadClients();
    }

    private void loadClients() {
        clientList = FXCollections.observableArrayList(clientDAO.getAllClients());
        clientTable.setItems(clientList);
    }

    @FXML
    private void handleAdd() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            Client client = new Client(0, name, email, phone);
            if (clientDAO.addClient(client)) {
                loadClients();
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Erreur", "Vérifiez les champs du formulaire.");
        }
    }

    @FXML
    private void handleEdit() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(nameField.getText());
                selected.setEmail(emailField.getText());
                selected.setPhone(phoneField.getText());
                if (clientDAO.updateClient(selected)) {
                    loadClients();
                    clearForm();
                }
            } catch (Exception e) {
                showAlert("Erreur", "Vérifiez les champs du formulaire.");
            }
        } else {
            showAlert("Aucun client sélectionné", "Veuillez sélectionner un client à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (clientDAO.deleteClient(selected.getId())) {
                loadClients();
                clearForm();
            }
        } else {
            showAlert("Aucun client sélectionné", "Veuillez sélectionner un client à supprimer.");
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
            Stage stage = (Stage) clientTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(dashboard));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 