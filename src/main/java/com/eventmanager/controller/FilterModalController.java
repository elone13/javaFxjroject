package com.eventmanager.controller;

import com.eventmanager.dao.ClientDAO;
import com.eventmanager.model.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FilterModalController {
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Client> clientFilterComboBox;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private TextField searchField;
    @FXML private CheckBox searchInTitleCheckBox;
    @FXML private CheckBox searchInLocationCheckBox;
    @FXML private CheckBox searchInDescriptionCheckBox;
    @FXML private CheckBox caseSensitiveCheckBox;
    @FXML private CheckBox statusPlannedCheckBox;
    @FXML private CheckBox statusInProgressCheckBox;
    @FXML private CheckBox statusCompletedCheckBox;
    @FXML private CheckBox statusCancelledCheckBox;
    @FXML private Label resultCountLabel;

    private Stage dialogStage;
    private boolean applied = false;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        clientFilterComboBox.setItems(FXCollections.observableArrayList(clientDAO.getAllClients()));
    }

    public void setDialogStage(Stage stage) { this.dialogStage = stage; }

    public boolean isApplied() { return applied; }

    @FXML private void handleToday() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
    }
    @FXML private void handleThisWeek() {
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now.minusDays(now.getDayOfWeek().getValue()-1));
        endDatePicker.setValue(startDatePicker.getValue().plusDays(6));
    }
    @FXML private void handleThisMonth() {
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now.withDayOfMonth(1));
        endDatePicker.setValue(now.withDayOfMonth(now.lengthOfMonth()));
    }
    @FXML private void handleThisYear() {
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now.withDayOfYear(1));
        endDatePicker.setValue(now.withDayOfYear(now.lengthOfYear()));
    }

    @FXML private void handleReset() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        clientFilterComboBox.setValue(null);
        minPriceField.clear();
        maxPriceField.clear();
        searchField.clear();
        searchInTitleCheckBox.setSelected(true);
        searchInLocationCheckBox.setSelected(true);
        searchInDescriptionCheckBox.setSelected(false);
        caseSensitiveCheckBox.setSelected(false);
        statusPlannedCheckBox.setSelected(false);
        statusInProgressCheckBox.setSelected(false);
        statusCompletedCheckBox.setSelected(false);
        statusCancelledCheckBox.setSelected(false);
        resultCountLabel.setText("0 résultat(s) trouvé(s)");
    }

    @FXML private void handleApplyFilters() {
        applied = true;
        close();
    }

    @FXML private void handleCancel() { close(); }
    @FXML private void handleClose() { close(); }

    private void close() {
        if (dialogStage != null) {
            dialogStage.close();
        } else if (searchField != null && searchField.getScene() != null && searchField.getScene().getWindow() instanceof Stage) {
            ((Stage) searchField.getScene().getWindow()).close();
        }
    }
}
