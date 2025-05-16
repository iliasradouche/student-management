package com.student.management.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML
    private AnchorPane ContentPane;

    // Show Students Section
    @FXML
    private void showStudentsSection() {
        loadSection("/views/StudentsSection.fxml");
    }

    // Show Courses Section
    @FXML
    private void showCoursesSection() {
        loadSection("/views/CoursesSection.fxml");
    }

    // Show Grades Section
    @FXML
    private void showGradesSection() {
        loadSection("/views/GradesSection.fxml");
    }

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
}
