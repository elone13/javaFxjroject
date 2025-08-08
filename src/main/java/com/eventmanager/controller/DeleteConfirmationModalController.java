package com.eventmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DeleteConfirmationModalController {
    @FXML private Label confirmationMessage;
    @FXML private Label itemCountLabel;
    @FXML private ListView<String> itemsList;

    private Stage dialogStage;
    private boolean confirmed = false;

    public void setDialogStage(Stage stage) { this.dialogStage = stage; }

    public void setItems(java.util.List<String> items, String message) {
        confirmationMessage.setText(message);
        itemCountLabel.setText("Cette action supprimera définitivement " + items.size() + " élément(s).");
        itemsList.getItems().setAll(items);
    }

    public boolean isConfirmed() { return confirmed; }

    @FXML private void handleConfirmDelete() { confirmed = true; close(); }
    @FXML private void handleCancel() { close(); }
    @FXML private void handleClose() { close(); }

    private void close() {
        if (dialogStage != null) {
            dialogStage.close();
        } else if (confirmationMessage != null && confirmationMessage.getScene() != null && confirmationMessage.getScene().getWindow() instanceof Stage) {
            ((Stage) confirmationMessage.getScene().getWindow()).close();
        }
    }
}
