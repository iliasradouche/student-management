package com.student.management.models;

public class Grade {
    private int id;
    private String studentName;
    private String courseName;
    private String grade;
    private String semester;

    // Constructor
    public Grade(int id, String studentName, String courseName, String grade, String semester) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
        this.grade = grade;
        this.semester = semester;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    // Enhanced toString() method for better display
    @Override
    public String toString() {
        return studentName + " - " + courseName + " (" + grade + ")";
    }
}
