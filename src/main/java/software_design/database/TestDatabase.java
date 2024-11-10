package software_design.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

public class TestDatabase {
    public static void main(String[] args) {
        try 
        {
            // Test creating the database if it does not exist
            Database.createDatabase();
            System.out.println("Database creation checked.");
            
            // Create tables using raw SQL statements
            Database.createTables();
            System.out.println("Tables created successfully.");

            Database.importMenuData();
            System.out.println("Menu data imported successfully.");

            // Test getting a connection to the database
            Connection connection = Database.getConnection();
            if (connection != null) {
                System.out.println("Connection successful!");
                connection.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
