package com.student.management.models;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Method to add a new student
    public static boolean addStudent(Student student) {
        String query = "INSERT INTO students (name, email, phone, dob, address, parent_name, parent_phone, profile_picture_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getDob());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getParentName());
            stmt.setString(7, student.getParentPhone());
            stmt.setString(8, student.getProfileImagePath());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get the profile image path of a student by ID
    private static String getProfileImagePath(int studentId) {
        String query = "SELECT profile_picture_path FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("profile_picture_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get all students
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("dob"),
                        rs.getString("address"),
                        rs.getString("parent_name"),
                        rs.getString("parent_phone"),
                        rs.getString("profile_picture_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    // Method to update a student
    public static boolean updateStudent(Student student) {
        String query = "UPDATE students SET name = ?, email = ?, phone = ?, dob = ?, address = ?, parent_name = ?, parent_phone = ?, profile_picture_path = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Get the current profile image path before updating
            String oldImagePath = getProfileImagePath(student.getId());

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getDob());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getParentName());
            stmt.setString(7, student.getParentPhone());
            stmt.setString(8, student.getProfileImagePath());
            stmt.setInt(9, student.getId());
            stmt.executeUpdate();

            // If profile picture changed, delete the old image file
            if (oldImagePath != null && !oldImagePath.equals(student.getProfileImagePath())) {
                deleteProfileImageFile(oldImagePath);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to delete a student
    public static boolean deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Get profile image path before deleting
            String imagePath = getProfileImagePath(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Delete profile image file if it exists
            if (imagePath != null) {
                deleteProfileImageFile(imagePath);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility Method to Delete Profile Image File
    private static void deleteProfileImageFile(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
    }

    // Method to get total students
    public static int getTotalStudents() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM students";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
