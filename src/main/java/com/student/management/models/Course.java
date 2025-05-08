package com.student.management.models;

public class Course {
    private int id;
    private String courseName;
    private String description;
    private int credits;

    // Constructor
    public Course(int id, String courseName, String description, int credits) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    // Enhanced toString() method for ComboBox
    @Override
    public String toString() {
        return courseName + " (ID: " + id + ")";
    }
}
