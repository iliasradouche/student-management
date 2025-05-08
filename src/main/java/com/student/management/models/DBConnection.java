package com.student.management.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentManagementSystem"; // Update with your DB name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = ""; // Replace with your MySQL password

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
