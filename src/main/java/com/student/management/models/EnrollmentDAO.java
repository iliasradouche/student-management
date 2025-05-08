package com.student.management.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // Method to enroll a student in a course
    public static boolean enrollStudent(int studentId, int courseId) {
        String query = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all enrolled students for a course
    public static List<Enrollment> getEnrolledStudents(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT s.id, s.name FROM enrollments e " +
                "JOIN students s ON e.student_id = s.id " +
                "WHERE e.course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                enrollments.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    // Method to remove a student's enrollment
    public static boolean removeEnrollment(int studentId, int courseId) {
        String query = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to Check if Student is Already Enrolled
    public static boolean isStudentEnrolled(int studentId, int courseId) {
        String query = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
