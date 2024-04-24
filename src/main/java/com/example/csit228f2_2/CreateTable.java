package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) {
        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String create_tblusers = "CREATE TABLE IF NOT EXISTS tblusers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL)";

            String create_tblevents = "CREATE TABLE IF NOT EXISTS tblevents ("
                    + "id INT,"
                    + "eventid INT AUTO_INCREMENT PRIMARY KEY,"
                    + "eventdate DATE NOT NULL,"
                    + "eventtitle VARCHAR(50) NOT NULL,"
                    + "eventdesc MEDIUMTEXT NOT NULL,"
                    + "FOREIGN KEY (id) REFERENCES tblusers(id))";

            statement.execute(create_tblusers);
            System.out.println("Table (tblusers) created successfully!");
            statement.execute(create_tblevents);
            System.out.println("Table (tblevents) created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
