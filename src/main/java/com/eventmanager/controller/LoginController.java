package com.eventmanager.controller;

import com.eventmanager.dao.UserDAO;
import com.eventmanager.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDAO.getUserByUsernameAndPassword(username, password);
        if (user != null) {
            // Stocker le rôle dans la session
            com.eventmanager.util.SessionUtil.setUserRole(user.getRole());
            // Redirection selon le rôle
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;
                if ("ADMIN".equals(user.getRole())) {
                    root = FXMLLoader.load(getClass().getResource("/fxml/admin_dashboard.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/employee_dashboard.fxml"));
                }
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Erreur lors du chargement de l'interface.");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Identifiants invalides.");
            errorLabel.setVisible(true);
        }
    }
} 