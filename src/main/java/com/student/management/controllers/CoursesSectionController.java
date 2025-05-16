package com.student.management.controllers;

import com.student.management.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class CoursesSectionController {

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, Integer> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> courseDescriptionColumn;
    @FXML
    private TableColumn<Course, Integer> courseCreditsColumn;

    @FXML
    private TableView<Enrollment> enrollmentTable;
    @FXML
    private TableColumn<Enrollment, Integer> enrolledStudentIdColumn;
    @FXML
    private TableColumn<Enrollment, String> enrolledStudentNameColumn;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {


        // Initialize Course Table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        courseCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        loadCourses();


        // Initialize Enrollment Table
        enrolledStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        enrolledStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        // Load Enrollments when Course is Selected
        courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadEnrollments();
            }
        });
    }

    private void loadCourses() {
        courseList.clear();
        courseList.addAll(CourseDAO.getAllCourses());
        courseTable.setItems(courseList);
    }

    @FXML
    private void handleAddCourse() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Course");
        dialog.setHeaderText("Enter Course Details");

        // Course Name
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Course Name");

        // Description
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        // Credits
        TextField creditsField = new TextField();
        creditsField.setPromptText("Credits");

        VBox vbox = new VBox(courseNameField, descriptionField, creditsField);
        vbox.setSpacing(10);
        dialog.getDialogPane().setContent(vbox);

        dialog.showAndWait().ifPresent(response -> {
            String courseName = courseNameField.getText();
            String description = descriptionField.getText();
            int credits;

            try {
                credits = Integer.parseInt(creditsField.getText());
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Credits must be a number.");
                return;
            }

            if (courseName.isEmpty() || description.isEmpty()) {
                showAlert("Invalid Input", "All fields are required.");
                return;
            }

            // Add Course using CourseDAO
            Course course = new Course(0, courseName, description, credits);
            boolean success = CourseDAO.addCourse(course);
            if (success) {
                loadCourses();
                showAlert("Course Added", "The course was successfully added.");
            } else {
                showAlert("Error", "Failed to add course.");
            }
        });
    }

    @FXML
    private void handleEditCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("No Selection", "Please select a course to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Course");
        dialog.setHeaderText("Edit Course Details");

        // Course Name
        TextField courseNameField = new TextField(selectedCourse.getCourseName());
        courseNameField.setPromptText("Course Name");

        // Description
        TextField descriptionField = new TextField(selectedCourse.getDescription());
        descriptionField.setPromptText("Description");

        // Credits
        TextField creditsField = new TextField(String.valueOf(selectedCourse.getCredits()));
        creditsField.setPromptText("Credits");

        VBox vbox = new VBox(courseNameField, descriptionField, creditsField);
        vbox.setSpacing(10);
        dialog.getDialogPane().setContent(vbox);

        dialog.showAndWait().ifPresent(response -> {
            String courseName = courseNameField.getText();
            String description = descriptionField.getText();
            int credits;

            try {
                credits = Integer.parseInt(creditsField.getText());
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Credits must be a number.");
                return;
            }

            if (courseName.isEmpty() || description.isEmpty()) {
                showAlert("Invalid Input", "All fields are required.");
                return;
            }

            // Update Course
            selectedCourse.setCourseName(courseName);
            selectedCourse.setDescription(description);
            selectedCourse.setCredits(credits);

            boolean success = CourseDAO.addCourse(selectedCourse);
            if (success) {
                loadCourses();
                showAlert("Course Updated", "The course was successfully updated.");
            } else {
                showAlert("Error", "Failed to update course.");
            }
        });
    }

    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("No Selection", "Please select a course to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Are you sure you want to delete this course?");
        alert.setContentText("Course: " + selectedCourse.getCourseName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = CourseDAO.deleteCourse(selectedCourse.getId());
            if (success) {
                loadCourses();
                showAlert("Course Deleted", "The course was successfully deleted.");
            } else {
                showAlert("Error", "Failed to delete course.");
            }
        }
    }

    @FXML
    private void handleEnrollStudent() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("No Course Selected", "Please select a course to enroll students.");
            return;
        }

        // Show Dialog for Selecting Student
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Enroll Student");
        dialog.setHeaderText("Select a Student to Enroll");

        // Student ComboBox (Only show students not enrolled)
        ComboBox<Student> studentComboBox = new ComboBox<>();
        studentComboBox.getItems().addAll(getAvailableStudentsForCourse(selectedCourse.getId()));
        studentComboBox.setPromptText("Select Student");

        VBox vbox = new VBox(studentComboBox);
        vbox.setSpacing(10);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
                if (selectedStudent == null) {
                    showAlert("No Student Selected", "Please select a student to enroll.");
                    return;
                }

                // Check if Student is Already Enrolled
                if (EnrollmentDAO.isStudentEnrolled(selectedStudent.getId(), selectedCourse.getId())) {
                    showAlert("Already Enrolled", selectedStudent.getName() + " is already enrolled in " + selectedCourse.getCourseName());
                    return;
                }

                // Enroll Student in Course
                boolean success = EnrollmentDAO.enrollStudent(selectedStudent.getId(), selectedCourse.getId());
                if (success) {
                    loadEnrollments();
                    showAlert("Student Enrolled", selectedStudent.getName() + " has been enrolled in " + selectedCourse.getCourseName());
                } else {
                    showAlert("Error", "Failed to enroll student.");
                }
            }
        });
    }

    private void loadEnrollments() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("No Course Selected", "Please select a course to view enrollments.");
            enrollmentList.clear();
            return;
        }

        enrollmentList.clear();
        enrollmentList.addAll(EnrollmentDAO.getEnrolledStudents(selectedCourse.getId()));
        enrollmentTable.setItems(enrollmentList);
    }


    // Method to Get Students Not Enrolled in Selected Course
    private List<Student> getAvailableStudentsForCourse(int courseId) {
        List<Student> allStudents = StudentDAO.getAllStudents();
        List<Enrollment> enrolledStudents = EnrollmentDAO.getEnrolledStudents(courseId);

        // Filter out students already enrolled
        List<Integer> enrolledStudentIds = enrolledStudents.stream()
                .map(Enrollment::getStudentId)
                .toList();

        // Return only students not in the enrolled list
        return allStudents.stream()
                .filter(student -> !enrolledStudentIds.contains(student.getId()))
                .toList();
    }

    @FXML
    private void handleRemoveEnrollment() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        Enrollment selectedEnrollment = enrollmentTable.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            showAlert("No Course Selected", "Please select a course.");
            return;
        }

        if (selectedEnrollment == null) {
            showAlert("No Enrollment Selected", "Please select an enrolled student to remove.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Enrollment");
        alert.setHeaderText("Are you sure you want to remove this enrollment?");
        alert.setContentText("Student: " + selectedEnrollment.getStudentName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = EnrollmentDAO.removeEnrollment(selectedEnrollment.getStudentId(), selectedCourse.getId());
            if (success) {
                loadEnrollments();
                showAlert("Enrollment Removed", "The student has been removed from the course.");
            } else {
                showAlert("Error", "Failed to remove enrollment.");
            }
        }
    }

    @FXML
    private void handleRefreshEnrollment() {
        loadEnrollments();
    }

    @FXML
    private void handleRefreshCourses() {
        loadCourses();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
