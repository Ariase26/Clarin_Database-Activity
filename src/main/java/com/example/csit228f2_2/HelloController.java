package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    public ToggleButton tbNight;
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnRename;
    @FXML
    private Button btnDeleteAccount;
    @FXML
    private Label lblWelcome;
    @FXML
    private Label lblContent1;
    @FXML
    private Label lblContent2;
    @FXML
    private TextField txtname;

    @FXML
    private void initialize() {
        displayWelcomeMessage();
    }

    private void displayWelcomeMessage() {
        String username = getUsernameFromDatabase();
        if (username != null) {
            lblWelcome.setText("Hello, " + username);
        } else {
            lblWelcome.setText("Failed to retrieve username");
        }
    }

    private String getUsernameFromDatabase() {
        String name = txtname.getText();
        String URL = "jdbc:mysql://localhost:3306/dbclarinoop2";
        String USERNAME = "jikjik";
        String PASSWORD = "bahalana";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT name FROM tblusers WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @FXML
    private void onNightModeClick() {
        if (tbNight.isSelected()) {
            // night mode
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
            lblContent1.setTextFill(Color.WHITE);
            lblContent2.setTextFill(Color.WHITE);
        } else {
            tbNight.getScene().getStylesheets().clear();
            lblContent1.setTextFill(Color.BLACK);
            lblContent2.setTextFill(Color.BLACK);
        }
    }

    @FXML
    private void onDeleteAccountClick() {
        if (tbNight.isSelected()) {
            // night mode
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
        } else {
            tbNight.getScene().getStylesheets().clear();
        }
    }

    @FXML
    private void onLogoutClick() {
        if (tbNight.isSelected()) {
            // night mode
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
        } else {
            tbNight.getScene().getStylesheets().clear();
        }
    }

    @FXML
    private void onRenameClick() {
        if (tbNight.isSelected()) {
            // night mode
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
        } else {
            tbNight.getScene().getStylesheets().clear();
        }
    }

}