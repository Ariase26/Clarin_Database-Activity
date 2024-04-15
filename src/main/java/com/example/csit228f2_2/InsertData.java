package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertData {
    public static void main(String[] args) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO users(name, email) VALUES(?, ?)")) {

            String name = "Kyzler Thomas";
            String email = "kyzler.thomas@gmail.com";

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Data inserted successfully");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

