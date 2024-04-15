package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogInPage extends Application {
    @FXML
    private AnchorPane pnLoginMain;
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

    @FXML
    protected void onLoginClick(ActionEvent event) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM tblusers WHERE name = ? AND password = ?")) {

            String name = txtUserName.getText();
            String password = txtPassword.getText();

            statement.setString(1, name);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                    try {
                        Scene scene = new Scene(loader.load());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Data retrieved successfully!");
                    lblAnnounce.setText("Login successfully!");
                    lblAnnounce.setVisible(true);
                    lblAnnounce.setManaged(true);

                    Stage currentStage = (Stage) pnLoginMain.getScene().getWindow();
                    currentStage.close();
                } else {
                    lblAnnounce.setText("Invalid username or password. Try again!");
                    lblAnnounce.setVisible(true);
                    lblAnnounce.setManaged(true);
                }
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
    protected void onRegisterRedirect(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignInPage.class.getResource("sign-in-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 413, 310);

        Stage stage = new Stage();
        stage.setTitle("User Registration");
        stage.setScene(scene);
        stage.show();

        Stage currentStage = (Stage) pnLoginMain.getScene().getWindow();
        currentStage.close();
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInPage.class.getResource("log-in-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 413, 310);
        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}