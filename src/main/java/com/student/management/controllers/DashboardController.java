package com.student.management.controllers;

import com.student.management.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class DashboardController {
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
    @FXML
    private TableView<Enrollment> enrollmentTable;
    @FXML
    private TableColumn<Enrollment, Integer> enrolledStudentIdColumn;
    @FXML
    private TableColumn<Enrollment, String> enrolledStudentNameColumn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Student> filteredList = FXCollections.observableArrayList();
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Grade> gradeList = FXCollections.observableArrayList();
    private ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList();




    // Method to initialize the TableView
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        loadStudents();

        // Initialize Course Table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        courseCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        loadCourses();

        // Initialize Grade Table
        gradeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        gradeStudentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        gradeCourseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        gradeValueColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeSemesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        loadGrades();

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

    // Method to Load Students from Database
    private void loadStudents() {
        studentList.clear();
        studentList.addAll(StudentDAO.getAllStudents());
        studentTable.setItems(studentList);
    }

    // Method to Load Courses from Database
    private void loadCourses() {
        courseList.clear();
        courseList.addAll(CourseDAO.getAllCourses());
        courseTable.setItems(courseList);
    }

    // Handle Search - This is triggered by the TextField (searchField)
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            studentTable.setItems(studentList); // Show all students if search is empty
            return;
        }

        // Filtered List based on the search query
        filteredList.setAll(studentList.filtered(student ->
                student.getName().toLowerCase().contains(query) ||
                        student.getEmail().toLowerCase().contains(query) ||
                        student.getPhone().contains(query)
        ));

        studentTable.setItems(filteredList);
    }

    // Handle Clear Search - Clears the search field and shows all students
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        studentTable.setItems(studentList);
    }

    // Handle Refresh Button
    @FXML
    private void handleRefresh() {
        loadStudents();
    }

    // Handle Add Student Button
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

    // Handle Edit Student Button
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

    // Handle Delete Student Button
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
    private void handleRefreshCourses() {
        loadCourses();
    }

    // Method to Load Grades from Database
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
    private void handleRefreshGrades() {
        loadGrades();
    }


    // Method to Load Enrolled Students for Selected Course
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


    // Utility Method for Alert Messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
