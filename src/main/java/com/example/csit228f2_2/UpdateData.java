package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("UPDATE users SET name=?, email=? WHERE id=?")) {

            String name = "Sherlynn Clarin";
            String email = "clarin.sherlynn@gmail.com";
            int idToUpdate = 2;

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, idToUpdate);

            int rowsUpdated = preparedStatement.executeUpdate();
            if(rowsUpdated > 0){
                System.out.println("Data updated successfully");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

