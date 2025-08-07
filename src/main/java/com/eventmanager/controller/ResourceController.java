package com.eventmanager.controller;

import com.eventmanager.dao.ResourceDAO;
import com.eventmanager.model.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class ResourceController {
    @FXML private TableView<Resource> resourceTable;
    @FXML private TableColumn<Resource, Integer> idColumn;
    @FXML private TableColumn<Resource, String> nameColumn;
    @FXML private TableColumn<Resource, String> typeColumn;
    @FXML private TableColumn<Resource, Integer> capacityColumn;
    @FXML private TableColumn<Resource, String> statusColumn;
    @FXML private TableColumn<Resource, BigDecimal> priceColumn;
    @FXML private TableColumn<Resource, String> locationColumn;

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField capacityField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField locationField;

    private final ResourceDAO resourceDAO = new ResourceDAO();
    private ObservableList<Resource> resourceList;

    @FXML
    public void initialize() {
        setupColumns();
        setupComboBoxes();
        setupSelectionListener();
        loadResources();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        typeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        capacityColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        priceColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPricePerHour()));
        locationColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation()));
    }

    private void setupComboBoxes() {
        typeComboBox.setItems(FXCollections.observableArrayList("SALLE", "MATERIEL", "VEHICULE"));
        typeComboBox.setValue("SALLE");

        statusComboBox.setItems(FXCollections.observableArrayList("DISPONIBLE", "RESERVE", "MAINTENANCE"));
        statusComboBox.setValue("DISPONIBLE");
    }

    private void setupSelectionListener() {
        resourceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadResources() {
        resourceList = FXCollections.observableArrayList(resourceDAO.getAllResources());
        resourceTable.setItems(resourceList);
    }

    private void populateForm(Resource resource) {
        nameField.setText(resource.getName());
        typeComboBox.setValue(resource.getType());
        capacityField.setText(resource.getCapacity() != null ? resource.getCapacity().toString() : "");
        descriptionArea.setText(resource.getDescription());
        priceField.setText(resource.getPricePerHour() != null ? resource.getPricePerHour().toString() : "0.00");
        statusComboBox.setValue(resource.getStatus());
        locationField.setText(resource.getLocation());
    }

    @FXML
    private void handleAdd() {
        if (validateForm()) {
            try {
                Integer capacity = null;
                if (!capacityField.getText().trim().isEmpty()) {
                    capacity = Integer.parseInt(capacityField.getText().trim());
                }

                Resource resource = new Resource(0,
                    nameField.getText().trim(),
                    typeComboBox.getValue(),
                    capacity,
                    descriptionArea.getText().trim(),
                    new BigDecimal(priceField.getText().trim()),
                    statusComboBox.getValue(),
                    locationField.getText().trim()
                );

                if (resourceDAO.addResource(resource)) {
                    loadResources();
                    clearForm();
                    showSuccess("Ressource ajoutée avec succès!");
                } else {
                    showError("Erreur lors de l'ajout de la ressource.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEdit() {
        Resource selected = resourceTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateForm()) {
            try {
                Integer capacity = null;
                if (!capacityField.getText().trim().isEmpty()) {
                    capacity = Integer.parseInt(capacityField.getText().trim());
                }

                selected.setName(nameField.getText().trim());
                selected.setType(typeComboBox.getValue());
                selected.setCapacity(capacity);
                selected.setDescription(descriptionArea.getText().trim());
                selected.setPricePerHour(new BigDecimal(priceField.getText().trim()));
                selected.setStatus(statusComboBox.getValue());
                selected.setLocation(locationField.getText().trim());

                if (resourceDAO.updateResource(selected)) {
                    loadResources();
                    clearForm();
                    showSuccess("Ressource modifiée avec succès!");
                } else {
                    showError("Erreur lors de la modification de la ressource.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        } else if (selected == null) {
            showError("Veuillez sélectionner une ressource à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Resource selected = resourceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer la ressource");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + selected.getName() + "\" ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (resourceDAO.deleteResource(selected.getId())) {
                    loadResources();
                    clearForm();
                    showSuccess("Ressource supprimée avec succès!");
                } else {
                    showError("Erreur lors de la suppression de la ressource.");
                }
            }
        } else {
            showError("Veuillez sélectionner une ressource à supprimer.");
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
        resourceTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            String role = com.eventmanager.util.SessionUtil.getUserRole();
            String dashboard = "/fxml/employee_dashboard.fxml";
            if ("ADMIN".equals(role)) {
                dashboard = "/fxml/admin_dashboard.fxml";
            }
            Stage stage = (Stage) resourceTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(dashboard));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour au tableau de bord.");
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Le nom de la ressource est obligatoire.");
            return false;
        }
        
        if (!capacityField.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(capacityField.getText().trim());
            } catch (NumberFormatException e) {
                showError("La capacité doit être un nombre entier.");
                return false;
            }
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
        nameField.clear();
        typeComboBox.setValue("SALLE");
        capacityField.clear();
        descriptionArea.clear();
        priceField.setText("0.00");
        statusComboBox.setValue("DISPONIBLE");
        locationField.clear();
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
