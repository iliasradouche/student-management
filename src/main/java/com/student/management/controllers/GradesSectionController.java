package com.student.management.controllers;

import com.student.management.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class GradesSectionController {

    @FXML
    private TableView<Grade> gradeTable;
    @FXML
    private TableColumn<Grade, Integer> gradeIdColumn;
    @FXML
    private TableColumn<Grade, String> gradeStudentColumn;
    @FXML
    private TableColumn<Grade, String> gradeCourseColumn;
    @FXML
    private TableColumn<Grade, String> gradeValueColumn;
    @FXML
    private TableColumn<Grade, String> gradeSemesterColumn;

    private ObservableList<Grade> gradeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        gradeIdColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        gradeStudentColumn.setCellValueFactory(data -> data.getValue().studentNameProperty());
        gradeCourseColumn.setCellValueFactory(data -> data.getValue().courseNameProperty());
        gradeValueColumn.setCellValueFactory(data -> data.getValue().gradeProperty());
        gradeSemesterColumn.setCellValueFactory(data -> data.getValue().semesterProperty());

        loadGrades();
    }

    private void loadGrades() {
        gradeList.clear();
        gradeList.addAll(GradeDAO.getAllGrades());
        gradeTable.setItems(gradeList);
    }

    @FXML
    private void handleAddGrade() {
        // Dialog for Adding Grade
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Grade");
        dialog.setHeaderText("Enter Grade Details");

        // ComboBox for Student Selection
        ComboBox<Student> studentComboBox = new ComboBox<>();
        studentComboBox.setPromptText("Select Student");
        studentComboBox.getItems().addAll(StudentDAO.getAllStudents());

        // ComboBox for Course Selection
        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Select Course");
        courseComboBox.getItems().addAll(CourseDAO.getAllCourses());

        // Grade and Semester Fields
        ComboBox<String> gradeField = new ComboBox<>();
        gradeField.setPromptText("Select Grade");
        gradeField.getItems().addAll("A", "B", "C", "D", "F");

        TextField semesterField = new TextField();
        semesterField.setPromptText("Semester (e.g., Fall 2025)");

        VBox vbox = new VBox(studentComboBox, courseComboBox, gradeField, semesterField);
        vbox.setSpacing(10);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
                Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
                String grade = gradeField.getSelectionModel().getSelectedItem();
                String semester = semesterField.getText();

                if (selectedStudent == null || selectedCourse == null || grade == null || semester.isEmpty()) {
                    showAlert("Invalid Input", "All fields are required.");
                    return;
                }

                boolean success = GradeDAO.addGrade(selectedStudent.getId(), selectedCourse.getId(), grade, semester);
                if (success) {
                    loadGrades();
                    showAlert("Grade Added", "The grade was successfully added.");
                } else {
                    showAlert("Error", "Failed to add grade.");
                }
            }
        });
    }

    @FXML
    private void handleEditGrade() {
        Grade selectedGrade = gradeTable.getSelectionModel().getSelectedItem();
        if (selectedGrade == null) {
            showAlert("No Selection", "Please select a grade to edit.");
            return;
        }

        // Dialog for Editing Grade
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Grade");
        dialog.setHeaderText("Edit Grade Details");

        // ComboBox for Grade Selection
        ComboBox<String> gradeField = new ComboBox<>();
        gradeField.setPromptText("Select Grade");
        gradeField.getItems().addAll("A", "B", "C", "D", "F");
        gradeField.setValue(selectedGrade.getGrade());

        TextField semesterField = new TextField(selectedGrade.getSemester());
        semesterField.setPromptText("Semester (e.g., Fall 2025)");

        VBox vbox = new VBox(gradeField, semesterField);
        vbox.setSpacing(10);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String grade = gradeField.getSelectionModel().getSelectedItem();
                String semester = semesterField.getText();

                if (grade == null || semester.isEmpty()) {
                    showAlert("Invalid Input", "All fields are required.");
                    return;
                }

                selectedGrade.setGrade(grade);
                selectedGrade.setSemester(semester);

                boolean success = GradeDAO.updateGrade(selectedGrade);
                if (success) {
                    loadGrades();
                    showAlert("Grade Updated", "The grade was successfully updated.");
                } else {
                    showAlert("Error", "Failed to update grade.");
                }
            }
        });
    }

    @FXML
    private void handleDeleteGrade() {
        Grade selectedGrade = gradeTable.getSelectionModel().getSelectedItem();
        if (selectedGrade == null) {
            showAlert("No Selection", "Please select a grade to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Grade");
        alert.setHeaderText("Are you sure you want to delete this grade?");
        alert.setContentText("Student: " + selectedGrade.getStudentName() +
                "\nCourse: " + selectedGrade.getCourseName() +
                "\nGrade: " + selectedGrade.getGrade());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = GradeDAO.deleteGrade(selectedGrade.getId());
            if (success) {
                loadGrades();
                showAlert("Grade Deleted", "The grade was successfully deleted.");
            } else {
                showAlert("Error", "Failed to delete grade.");
            }
        }
    }

    @FXML
    private void handleRefreshGrades() {
        loadGrades();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
