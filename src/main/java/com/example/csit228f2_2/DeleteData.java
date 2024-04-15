package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM users WHERE id=?")) {

            int idToDelete= 2;
            preparedStatement.setInt(1, idToDelete);

            int rowsDeleted = preparedStatement.executeUpdate();
            if(rowsDeleted > 0){
                System.out.println("Data deleted successfully");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
