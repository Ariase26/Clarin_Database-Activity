package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    public AnchorPane pnHomePage;
    @FXML
    public ToggleButton tbNight;
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnLogout;
    @FXML
    private ToggleButton tbGoGreen;
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
    private Button btnPlanner;

    @FXML
    public void initialize() {
        displayWelcomeMessage();
    }

    private void displayWelcomeMessage() {
        String username = getUsernameFromDatabase();
        if (username != null) {

            lblWelcome.setText("Hello, " + username + "!");
        } else {
            lblWelcome.setText("Failed to retrieve username");
        }
    }

    private String getUsernameFromDatabase() {
        String name = txtname.getText();
        int idd=LogInPage.id;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT  * FROM tblusers WHERE id = ?")) {

            System.out.println(idd);
            statement.setString(1, String.valueOf(idd));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
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
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
            updateTextColors(Color.WHITE);
        } else {
            tbNight.getScene().getStylesheets().clear();
            updateTextColors(Color.BLACK);
        }
    }

    @FXML
    private void onGreenClick() {
        if (tbGoGreen.isSelected()) {
            tbGoGreen.getScene().getStylesheets().add(
                    getClass().getResource("green.css").toExternalForm());
            updateTextColors(Color.WHITE);
        } else {
            tbGoGreen.getScene().getStylesheets().clear();
            updateTextColors(Color.BLACK);
        }
    }

    private void updateTextColors(Color color) {
        lblContent1.setTextFill(color);
        lblContent2.setTextFill(color);
    }

    @FXML
    private void onDeleteAccountClick() {
        int userId = LogInPage.id;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE FROM tblusers WHERE id = ?")) {

            statement.setInt(1, userId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                displayAlert("Success", "Account Deleted", "Your account has been deleted successfully.");
                onLogoutClick();
            } else {
                displayAlert("Error", "Deletion Failed", "Failed to delete your account. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onLogoutClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("log-in-page.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage currentStage = (Stage) pnHomePage.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void onRenameClick() {
        String name = txtname.getText();
        int idd = LogInPage.id;

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("Update tblusers set name=? WHERE id = ?")) {

            System.out.println(idd);
            statement.setString(1, String.valueOf(name));
            statement.setString(2, String.valueOf(idd));
            try {
                int res = statement.executeUpdate();
                displayWelcomeMessage();
                System.out.println("Name has been changed successfully");

            } catch (SQLException e) {
                System.out.println("Changing name failed");
                e.printStackTrace();
            }
        }catch (Exception e){

        }
    }

    @FXML
    private void onRedirectPlanner() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planner-page.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("green.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage currentStage = (Stage) pnHomePage.getScene().getWindow();
        currentStage.close();
    }
}