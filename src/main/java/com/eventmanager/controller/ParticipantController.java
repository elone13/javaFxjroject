package com.eventmanager.controller;

import com.eventmanager.dao.ParticipantDAO;
import com.eventmanager.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ParticipantController {
    @FXML private TableView<Participant> participantTable;
    @FXML private TableColumn<Participant, Integer> idColumn;
    @FXML private TableColumn<Participant, String> nameColumn;
    @FXML private TableColumn<Participant, String> emailColumn;
    @FXML private TableColumn<Participant, String> phoneColumn;
    @FXML private TableColumn<Participant, String> typeColumn;
    @FXML private TableColumn<Participant, String> companyColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField companyField;

    private final ParticipantDAO participantDAO = new ParticipantDAO();
    private ObservableList<Participant> participantList;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        typeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        companyColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCompany()));

        // Configuration du ComboBox
        typeComboBox.setItems(FXCollections.observableArrayList("PARTICIPANT", "INTERVENANT"));
        typeComboBox.setValue("PARTICIPANT");

        // Selection listener
        participantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });

        loadParticipants();
    }

    private void loadParticipants() {
        participantList = FXCollections.observableArrayList(participantDAO.getAllParticipants());
        participantTable.setItems(participantList);
    }

    private void populateForm(Participant participant) {
        nameField.setText(participant.getName());
        emailField.setText(participant.getEmail());
        phoneField.setText(participant.getPhone());
        typeComboBox.setValue(participant.getType());
        companyField.setText(participant.getCompany());
    }

    @FXML
    private void handleAdd() {
        if (validateForm()) {
            try {
                Participant participant = new Participant(0, 
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    typeComboBox.getValue(),
                    companyField.getText().trim()
                );
                
                if (participantDAO.addParticipant(participant)) {
                    loadParticipants();
                    clearForm();
                    showSuccess("Participant ajouté avec succès!");
                } else {
                    showError("Erreur lors de l'ajout du participant.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEdit() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateForm()) {
            try {
                selected.setName(nameField.getText().trim());
                selected.setEmail(emailField.getText().trim());
                selected.setPhone(phoneField.getText().trim());
                selected.setType(typeComboBox.getValue());
                selected.setCompany(companyField.getText().trim());
                
                if (participantDAO.updateParticipant(selected)) {
                    loadParticipants();
                    clearForm();
                    showSuccess("Participant modifié avec succès!");
                } else {
                    showError("Erreur lors de la modification du participant.");
                }
            } catch (Exception e) {
                showError("Erreur: " + e.getMessage());
            }
        } else if (selected == null) {
            showError("Veuillez sélectionner un participant à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer le participant");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.getName() + " ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (participantDAO.deleteParticipant(selected.getId())) {
                    loadParticipants();
                    clearForm();
                    showSuccess("Participant supprimé avec succès!");
                } else {
                    showError("Erreur lors de la suppression du participant.");
                }
            }
        } else {
            showError("Veuillez sélectionner un participant à supprimer.");
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
        participantTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            String role = com.eventmanager.util.SessionUtil.getUserRole();
            String dashboard = "/fxml/employee_dashboard.fxml";
            if ("ADMIN".equals(role)) {
                dashboard = "/fxml/admin_dashboard.fxml";
            }
            Stage stage = (Stage) participantTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(dashboard));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour au tableau de bord.");
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
        if (!isValidEmail(emailField.getText().trim())) {
            showError("Format d'email invalide.");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        typeComboBox.setValue("PARTICIPANT");
        companyField.clear();
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
