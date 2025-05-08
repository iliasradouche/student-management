package com.student.management.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Method to add a new course
    public static boolean addCourse(Course course) {
        String query = "INSERT INTO courses (course_name, description, credits) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all courses
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("course_name"),
                        rs.getString("description"),
                        rs.getInt("credits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Method to delete a course
    public static boolean deleteCourse(int id) {
        String query = "DELETE FROM courses WHERE id = ?";
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
}
