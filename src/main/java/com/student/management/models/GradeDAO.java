package com.student.management.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    // Method to add a new grade
    public static boolean addGrade(int studentId, int courseId, String grade, String semester) {
        String query = "INSERT INTO grades (student_id, course_id, grade, semester) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setString(3, grade);
            stmt.setString(4, semester);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all grades
    public static List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT g.id, s.name AS student_name, c.course_name, g.grade, g.semester " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.id " +
                "JOIN courses c ON g.course_id = c.id";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                grades.add(new Grade(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getString("course_name"),
                        rs.getString("grade"),
                        rs.getString("semester")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    // Method to delete a grade
    public static boolean deleteGrade(int id) {
        String query = "DELETE FROM grades WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update a grade
    public static boolean updateGrade(Grade grade) {
        String query = "UPDATE grades SET grade = ?, semester = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, grade.getGrade());
            stmt.setString(2, grade.getSemester());
            stmt.setInt(3, grade.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
