package com.student.management.controllers;

import com.student.management.models.Student;
import com.student.management.models.StudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class StudentsSectionController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> phoneColumn;
    @FXML
    private TableColumn<Student, String> dobColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        loadStudents();
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(StudentDAO.getAllStudents());
        studentTable.setItems(studentList);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            studentTable.setItems(studentList);
            return;
        }

        studentTable.setItems(studentList.filtered(student ->
                student.getName().toLowerCase().contains(query) ||
                        student.getEmail().toLowerCase().contains(query) ||
                        student.getPhone().contains(query)));
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        studentTable.setItems(studentList);
    }

    @FXML
    private void handleAddStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddStudent.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add New Student");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();

            // Refresh student list after adding
            loadStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("No Selection", "Please select a student to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditStudent.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Student");
            stage.setScene(new Scene(loader.load()));

            // Pass selected student to EditStudentController
            EditStudentController controller = loader.getController();
            controller.setStudent(selectedStudent);

            stage.showAndWait();

            // Refresh the student list after editing
            loadStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Are you sure you want to delete this student?");
            alert.setContentText("Student: " + selectedStudent.getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = StudentDAO.deleteStudent(selectedStudent.getId());
                if (success) {
                    loadStudents();
                    showAlert("Student Deleted", "The student was successfully deleted.");
                } else {
                    showAlert("Error", "Failed to delete the student.");
                }
            }
        } else {
            showAlert("No Selection", "Please select a student to delete.");
        }
    }

    @FXML
    private void handleViewProfile() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("No Student Selected", "Please select a student to view their profile.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/StudentProfile.fxml"));
            Parent profileRoot = loader.load();

            // Passing the selected student to the profile controller
            StudentProfileController profileController = loader.getController();
            profileController.setStudent(selectedStudent);

            Stage profileStage = new Stage();
            profileStage.setTitle("Student Profile");
            profileStage.setScene(new Scene(profileRoot));
            profileStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the profile view.");
        }
    }
    @FXML
    private void handleRefresh() {
        loadStudents();
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
