package com.student.management.controllers;

import com.student.management.models.Student;
import com.student.management.models.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddStudentController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private Label errorMessage;

    // Handle Save Student Button
    @FXML
    private void handleSaveStudent(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        LocalDate dob = dobPicker.getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob == null) {
            showError("All fields are required!");
            return;
        }

        if (!name.matches("[a-zA-Z\s]+")) {
            showError("Name must contain only letters.");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            showError("Invalid email format.");
            return;
        }

        if (!phone.matches("\\d{8,15}")) {
            showError("Phone must contain only digits (8-15). ");
            return;
        }

        // Create Student Object
        Student student = new Student(0, name, email, phone, dob.toString());

        // Save Student using StudentDAO
        boolean success = StudentDAO.addStudent(student);
        if (success) {
            closeWindow();
            showAlert("Student Added", "The student was successfully added.");
        } else {
            showError("Failed to add student. Try again.");
        }
    }

    // Handle Cancel Button
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    // Utility Method to Close Window
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    // Utility Method for Error Messages
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        errorMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }

    // Utility Method for Alert Messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
