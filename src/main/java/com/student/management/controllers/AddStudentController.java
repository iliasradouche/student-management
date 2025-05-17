package com.student.management.controllers;

import com.student.management.models.Student;
import com.student.management.models.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class AddStudentController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField parentNameField;
    @FXML
    private TextField parentPhoneField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label errorMessage;

    private String profileImagePath = null;

    // Handle Save Student Button
    @FXML
    private void handleSaveStudent(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String address = addressField.getText().trim();
        String parentName = parentNameField.getText().trim();
        String parentPhone = parentPhoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob == null
                || address.isEmpty() || parentName.isEmpty() || parentPhone.isEmpty()) {
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
        if (!parentPhone.matches("\\d{8,15}")) {
            showError("Parent Phone must contain only digits (8-15).");
            return;
        }

        // Create Student Object
        Student student = new Student(0, name, email, phone, dob.toString(), address, parentName, parentPhone, profileImagePath);

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

    @FXML
    private void handleUploadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Ensure the directory for profile images exists
                File directory = new File("src/main/resources/images/student_profiles");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Generate a unique file name to avoid overwriting
                String uniqueFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(directory, uniqueFileName);

                // Copy the selected file to the directory
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save the relative path for database storage
                profileImagePath = "/images/student_profiles/" + uniqueFileName;

                // Display the image in the ImageView
                profileImage.setImage(new Image(destinationFile.toURI().toString()));
            } catch (IOException e) {
                showError("Failed to upload image.");
                e.printStackTrace();
            }
        }
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
