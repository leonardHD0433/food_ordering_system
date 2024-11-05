package software_design.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            // Test creating the database if it does not exist
            Database.createDatabase();
            System.out.println("Database creation checked.");

            // Initialize Hibernate (this will handle table creation)
            Database.initializeHibernate();
            System.out.println("Hibernate initialized and schema updated.");

            // Test getting a connection to the database
            Connection connection = Database.getConnection();
            if (connection != null) {
                System.out.println("Connection successful!");
                connection.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
