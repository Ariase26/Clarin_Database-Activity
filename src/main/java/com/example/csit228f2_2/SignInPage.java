package com.example.csit228f2_2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignInPage {
    @FXML
    private AnchorPane pnMain;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtVisiblePassword;
    @FXML
    private Button btnShow;
    @FXML
    private Button btnRegister;
    @FXML
    private Label lblAnnounce;
    private final String[] homecss = {"home1.css", "home2.css", "home3.css"};

    @FXML
    protected void onRegisterClick(ActionEvent event) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("INSERT INTO tblusers (name, password) VALUES (?, ?)")) {

            String name = txtUserName.getText();
            String password = txtPassword.getText();

            if (!name.isEmpty() && !password.isEmpty()) {
                statement.setString(1, name);
                statement.setString(2, password);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    pnMain.setPrefHeight(700.0);
                    System.out.println("Data inserted successfully!");
                    lblAnnounce.setText("Registered successfully!");
                    lblAnnounce.setVisible(true);
                    lblAnnounce.setManaged(true);
                }
            } else {
                pnMain.setPrefHeight(700.0);
                lblAnnounce.setText("No username or password provided!");
                lblAnnounce.setVisible(true);
                lblAnnounce.setManaged(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onPasswordShow() {
        if (txtPassword.isVisible()) {
            btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent actionEvent) {
                    String password = txtPassword.getText();
                    txtVisiblePassword.setText(password);
                    txtPassword.setVisible(false);
                    txtPassword.setManaged(false);
                    txtVisiblePassword.setVisible(true);
                    txtVisiblePassword.setManaged(true);
                    txtVisiblePassword.toFront();
                }
            });
        } else {
            btnShow.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    String visiblePassword = txtVisiblePassword.getText();
                    txtPassword.setText(visiblePassword);
                    txtVisiblePassword.setVisible(false);
                    txtVisiblePassword.setManaged(false);
                    txtPassword.setVisible(true);
                    txtPassword.setManaged(true);
                    txtPassword.toFront();
                }
            });
        }
    }

    @FXML
    protected void onLoginRedirect(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) pnMain.getScene().getWindow();
        currentStage.close();
    }

}
