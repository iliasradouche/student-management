package com.student.management;

import com.student.management.models.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Login Screen FXML
            Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("gestion des étudiants - Se Connecter");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Test Database Connection
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            System.out.println("✅ Database connected successfully!");
        } else {
            System.out.println("❌ Failed to connect to database.");
        }
        launch(args);
    }
}
