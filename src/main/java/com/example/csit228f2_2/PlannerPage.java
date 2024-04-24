package com.example.csit228f2_2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

public class PlannerPage {
    @FXML
    public AnchorPane pnDashboard;
    @FXML
    public DatePicker eventDate;
    @FXML
    public TextField eventTitle;
    @FXML
    public TextArea eventDesc;
    @FXML
    public Button btnAddEvent;
    @FXML
    public ListView<String> eventList;
    @FXML
    public Button btnRefresh;
    @FXML
    public Button btnView;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;

    public int userID = LogInPage.id;

    public void initialize() {
        refreshEventList();
        eventDesc.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() % 30 == 0) {
                    eventDesc.appendText("\n");
                }
            }
        });
    }

    @FXML
    private void onAddEvent() {
        String title = eventTitle.getText();
        String desc = eventDesc.getText();
        LocalDate date = eventDate.getValue();

        try (Connection c = MySQLConnection.getConnection()) {
            String sql = "INSERT INTO tblevents (id, eventdate, eventtitle, eventdesc) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setDate(2, Date.valueOf(date));
            statement.setString(3, title);
            statement.setString(4, desc);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        refreshEventList();
    }

    @FXML
    private void onRefreshClick() {
        refreshEventList();
    }

    private void refreshEventList() {
        eventList.getItems().clear();
        List<String> eventData = fetchEventData();
        eventList.getItems().addAll(eventData);
    }

    private List<String> fetchEventData() {
        List<String> eventData = new ArrayList<>();

        try (Connection c = MySQLConnection.getConnection()) {
            String sql = "SELECT eventtitle FROM tblevents WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(sql);

            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("eventtitle");
                eventData.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventData;
    }

    @FXML
    private void onViewClick() {
        String selectedEventTitle = eventList.getSelectionModel().getSelectedItem();

        if (selectedEventTitle != null) {
            try (Connection c = MySQLConnection.getConnection()) {
                String sql = "SELECT * FROM tblevents WHERE id = ? AND eventtitle = ?";
                PreparedStatement statement = c.prepareStatement(sql);
                statement.setInt(1, userID);
                statement.setString(2, selectedEventTitle);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    eventDate.setValue(resultSet.getDate("eventdate").toLocalDate());
                    eventTitle.setText(resultSet.getString("eventtitle"));
                    eventDesc.setText(resultSet.getString("eventdesc"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Event Selected", "Please select an event to view.");
        }
    }

    @FXML
    private void onDeleteClick() {
        String selectedEventTitle = eventList.getSelectionModel().getSelectedItem();

        if (selectedEventTitle != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Event");
            alert.setContentText("Are you sure you want to delete this event?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection c = MySQLConnection.getConnection()) {
                        String sql = "DELETE FROM tblevents WHERE id = ? AND eventtitle = ?";
                        PreparedStatement statement = c.prepareStatement(sql);
                        statement.setInt(1, userID);
                        statement.setString(2, selectedEventTitle);
                        statement.executeUpdate();

                        showAlert(Alert.AlertType.INFORMATION, "Event Deletion", "The event is successfully deleted");
                        refreshEventList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Event Selected", "Please select an event to delete.");
        }
    }

    @FXML
    private void onUpdateClick() {
        String selectedEventTitle = eventList.getSelectionModel().getSelectedItem();

        if (selectedEventTitle != null) {
            try (Connection c = MySQLConnection.getConnection()) {
                String sql = "UPDATE tblevents SET eventdate = ?, eventtitle = ?, eventdesc = ? WHERE id = ? AND eventtitle = ?";
                PreparedStatement statement = c.prepareStatement(sql);
                statement.setDate(1, Date.valueOf(eventDate.getValue()));
                statement.setString(2, eventTitle.getText());
                statement.setString(3, eventDesc.getText());
                statement.setInt(4, userID);
                statement.setString(5, selectedEventTitle);
                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Event Update", "The event is successfully updated");
                refreshEventList();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Event Selected", "Please select an event to update.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBackClick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage currentStage = (Stage) pnDashboard.getScene().getWindow();
        currentStage.close();
    }

}
