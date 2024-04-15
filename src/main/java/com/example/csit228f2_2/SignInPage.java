package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignInPage {
    @FXML
    private AnchorPane pnMain;
    @FXML
    private AnchorPane pnHome;
    @FXML
    private VBox pnLogin;
    @FXML
    private VBox pnLogout;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ColorPicker cpPicker;

    private final String[] homecss = {"home1.css", "home2.css", "home3.css"};

    @FXML
    protected void onRegisterClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HelloApplication.java"));
        String enteredUsername = txtUserName.getText();
        String enteredPassword = txtPassword.getText();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO users(name, password) VALUES(?, ?)")) {

            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Data inserted successfully");
                pnMain.getChildren().remove(pnLogin);
                pnMain.getChildren().add(root);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogOutButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        pnHome.getScene().getStylesheets().clear();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(getClass().getResource("home1.css").getPath()));
            bw.write(".root { -fx-background-image: url(\"quartzW1.jpg\"); }");
            bw.newLine();
            bw.write(".button { -fx-background-color: #" + cpPicker.getValue().toString().substring(2, 8) + "; }");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane parent = (AnchorPane) pnHome.getParent();
        parent.getChildren().remove(pnHome);
        parent.getChildren().add(root);
    }
}
