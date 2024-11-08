package software_design.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASSWORD");
    private static final String DB_URL_NO_DB = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT;
    private static final String DB_URL = DB_URL_NO_DB + "/" + DB_NAME;

    public static void createDatabase() throws SQLException 
    {
        // Create the database if it does not exist
        try (Connection conn = DriverManager.getConnection(DB_URL_NO_DB, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } 
        catch (SQLException e) 
        {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void createTables() throws SQLException 
    {
        String createMenuTableSQL = "CREATE TABLE IF NOT EXISTS menu_table (" +
                "ItemId INT AUTO_INCREMENT PRIMARY KEY, " +
                "ItemCategory VARCHAR(20) NOT NULL, " +
                "ItemName VARCHAR(50) NOT NULL, " +
                "ItemDescription VARCHAR(150) NOT NULL, " +
                "ItemPrice DOUBLE NOT NULL, " +
                "ItemAvailability BOOLEAN NOT NULL" +
                "ItemImage MEDIUMBLOB NOT NULL" +
                ")";

        String createTableStatusSQL = "CREATE TABLE IF NOT EXISTS table_status (" +
                "TableId INT PRIMARY KEY, " +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createMenuTableSQL);
            stmt.executeUpdate(createTableStatusSQL);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
