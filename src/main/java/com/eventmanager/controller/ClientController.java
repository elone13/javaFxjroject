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
import javafx.stage.Modality;
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
        Client result = openClientModal(null);
        if (result != null) {
            if (clientDAO.addClient(result)) {
                loadClients();
            } else {
                showAlert("Erreur", "Échec de l'ajout du client.");
            }
        }
    }

    @FXML
    private void handleEdit() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Aucun client sélectionné", "Veuillez sélectionner un client à modifier."); return; }
        Client edited = openClientModal(selected);
        if (edited != null) {
            if (clientDAO.updateClient(edited)) {
                loadClients();
            } else {
                showAlert("Erreur", "Échec de la modification du client.");
            }
        }
    }

    @FXML
    private void handleDelete() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Aucun client sélectionné", "Veuillez sélectionner un client à supprimer."); return; }
        Boolean confirmed = openDeleteModal("Supprimer le client", java.util.Collections.singletonList(selected.getName()));
        if (Boolean.TRUE.equals(confirmed)) {
            if (clientDAO.deleteClient(selected.getId())) {
                loadClients();
                clearForm();
            } else {
                showAlert("Erreur", "Échec de la suppression du client.");
            }
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

    private Client openClientModal(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client_modal.fxml"));
            Parent root = loader.load();
            ClientModalController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setClient(client);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(client == null ? "Nouveau client" : "Modifier le client");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            if (controller.isSaved()) {
                return controller.getResultClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la modale: " + e.getMessage());
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
            showAlert("Erreur", "Impossible d'ouvrir la confirmation: " + e.getMessage());
        }
        return false;
    }
} 