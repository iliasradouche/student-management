package com.student.management.models;

public class Enrollment {
    private int studentId;
    private String studentName;

    // Constructor
    public Enrollment(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
