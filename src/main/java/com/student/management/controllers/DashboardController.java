package com.student.management.controllers;

import com.student.management.models.CourseDAO;
import com.student.management.models.EnrollmentDAO;
import com.student.management.models.StudentDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private AnchorPane ContentPane;

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalCoursesLabel;
    @FXML
    private Label totalEnrollmentsLabel;

    @FXML
    public void initialize() {
        updateStatistics();
    }

    // Show Students Section
    @FXML
    private void showStudentsSection() {
        loadSection("/views/StudentsSection.fxml");
        updateStatistics();

    }

    // Show Courses Section
    @FXML
    private void showCoursesSection() {
        loadSection("/views/CoursesSection.fxml");
        updateStatistics();

    }

    // Show Grades Section
    @FXML
    private void showGradesSection() {
        loadSection("/views/GradesSection.fxml");
        updateStatistics();

    }

    @FXML
    // Method to Load Sections into ContentPane
    private void loadSection(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane section = loader.load();
            ContentPane.getChildren().clear();
            ContentPane.getChildren().add(section);

            // Ensure the section fills the entire ContentPane
            AnchorPane.setTopAnchor(section, 0.0);
            AnchorPane.setBottomAnchor(section, 0.0);
            AnchorPane.setLeftAnchor(section, 0.0);
            AnchorPane.setRightAnchor(section, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    // Method to Update Statistics
    private void updateStatistics() {
        int totalStudents = StudentDAO.getTotalStudents();
        int totalCourses = CourseDAO.getTotalCourses();
        int totalEnrollments = EnrollmentDAO.getTotalEnrollments();

        totalStudentsLabel.setText(String.valueOf(totalStudents));
        totalCoursesLabel.setText(String.valueOf(totalCourses));
        totalEnrollmentsLabel.setText(String.valueOf(totalEnrollments));
    }

    // Method to Manually Refresh Statistics
    @FXML
    private void refreshStatistics() {
        updateStatistics();
    }
}
