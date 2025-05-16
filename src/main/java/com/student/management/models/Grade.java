package com.student.management.models;

import javafx.beans.property.*;

public class Grade {
    private IntegerProperty id;
    private StringProperty studentName;
    private StringProperty courseName;
    private StringProperty grade;
    private StringProperty semester;

    // Constructor
    public Grade(int id, String studentName, String courseName, String grade, String semester) {
        this.id = new SimpleIntegerProperty(id);
        this.studentName = new SimpleStringProperty(studentName);
        this.courseName = new SimpleStringProperty(courseName);
        this.grade = new SimpleStringProperty(grade);
        this.semester = new SimpleStringProperty(semester);
    }

    // Getters and Setters using JavaFX Properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public StringProperty semesterProperty() {
        return semester;
    }
}
