package com.student.management.controllers;

import com.student.management.models.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Username and Password are required!");
            errorMessage.setVisible(true);
            return;
        }

        // Database Verification
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Login Successful - Load Dashboard
                loadDashboard();
            } else {
                errorMessage.setText("Invalid username or password!");
                errorMessage.setVisible(true);
            }
        } catch (Exception e) {
            errorMessage.setText("An error occurred. Try again.");
            errorMessage.setVisible(true);
            e.printStackTrace();
        }
    }

    // Method to load the Dashboard and maximize it
    private void loadDashboard() {
        try {
            // Get current stage (Login window)
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close(); // Close the login window

            // Load Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
            Parent root = loader.load();
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(new Scene(root));
            dashboardStage.setTitle("Student Management System - Dashboard");
            dashboardStage.setMaximized(true); // Set the dashboard to be maximized
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
