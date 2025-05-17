package com.student.management.controllers;

import com.student.management.models.Student;
import com.student.management.models.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class StudentProfileController {
    @FXML
    private TextField nameField, emailField, phoneField, addressField, parentNameField, parentPhoneField;
    @FXML
    private ImageView profileImage;

    @FXML
    private Button saveButton;
    @FXML
    private Label errorMessage;

    private Student currentStudent;
    private String profileImagePath = null;

    // Method to set the student data
    public void setStudent(Student student) {
        this.currentStudent = student;

        if (student == null) {
            System.out.println("Student is null!");
            return;
        }

        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        addressField.setText(student.getAddress());
        parentNameField.setText(student.getParentName());
        parentPhoneField.setText(student.getParentPhone());

        // Load Profile Image
        loadProfileImage(student.getProfileImagePath());
    }

    private void loadProfileImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                // Load the image using the absolute file path (directly from the file system)
                File file = new File("src/main/resources" + imagePath);
                if (file.exists()) {
                    profileImage.setImage(new Image(file.toURI().toString()));
                } else {
                    // Default profile image if not found
                    profileImage.setImage(new Image(getClass().getResource("/images/default_profile.png").toExternalForm()));
                    System.out.println("Profile image not found, loading default.");
                }
            } else {
                // Load Default Profile Image from Resources
                profileImage.setImage(new Image(getClass().getResource("/images/default_profile.png").toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    private void handleSaveChanges() {
        if (currentStudent == null) {
            showAlert("Error", "No student selected.");
            return;
        }

        currentStudent.setName(nameField.getText());
        currentStudent.setEmail(emailField.getText());
        currentStudent.setPhone(phoneField.getText());
        currentStudent.setAddress(addressField.getText());
        currentStudent.setParentName(parentNameField.getText());
        currentStudent.setParentPhone(parentPhoneField.getText());
        currentStudent.setProfileImagePath(profileImagePath);

        boolean success = StudentDAO.updateStudent(currentStudent);
        if (success) {
            showAlert("Success", "Student profile updated successfully.");
        } else {
            showAlert("Error", "Failed to update student profile.");
        }
    }


    @FXML
    private void handleUploadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Ensure the "student_profiles" directory exists in the images folder
                File directory = new File("src/main/resources/images/student_profiles");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Generate a unique file name
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(directory, fileName);

                // Copy the selected file to the student_profiles directory
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save the relative path to the profile image
                profileImagePath = "/images/student_profiles/" + fileName;
                profileImage.setImage(new Image(destinationFile.toURI().toString()));
                showAlert("Success", "Profile picture uploaded and saved successfully.");
            } catch (Exception e) {
                showError("Failed to upload image.");
                e.printStackTrace();
            }
        }
    }



    // Handle Close
    @FXML
    private void handleClose() {
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

    public void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
