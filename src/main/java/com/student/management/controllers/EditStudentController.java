package com.student.management.controllers;

import com.student.management.models.Student;
import com.student.management.models.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditStudentController {
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

    private Student selectedStudent;

    // Method to Set Student Data (Will be called from Dashboard)
    public void setStudent(Student student) {
        this.selectedStudent = student;
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        dobPicker.setValue(LocalDate.parse(student.getDob()));
    }

    // Handle Save Changes Button
    @FXML
    private void handleSaveChanges(ActionEvent event) {
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

        // Update Student Object
        selectedStudent.setName(name);
        selectedStudent.setEmail(email);
        selectedStudent.setPhone(phone);
        selectedStudent.setDob(dob.toString());

        // Save Changes using StudentDAO
        boolean success = StudentDAO.updateStudent(selectedStudent);
        if (success) {
            closeWindow();
            showAlert("Student Updated", "The student details were successfully updated.");
        } else {
            showError("Failed to update student. Try again.");
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
