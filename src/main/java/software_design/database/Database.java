package software_design.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;


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
                "ItemOptions VARCHAR(100), " +
                "ItemPrice DOUBLE NOT NULL, " +
                "ItemAvailability BOOLEAN NOT NULL," +
                "ItemImage MEDIUMBLOB NOT NULL" +
                ")";

        String createTableStatusSQL = "CREATE TABLE IF NOT EXISTS table_status (" +
                "TableId INT PRIMARY KEY" +
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

    public static void importMenuData() throws SQLException, IOException {
        String insertSQL = "INSERT INTO menu_table (ItemCategory, ItemName, ItemDescription, ItemOptions, ItemPrice, ItemAvailability, ItemImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        String csvFile = "src/main/java/software_design/database/data/menu.csv";
        String line;
        boolean firstLine = true;
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
    
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
    
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                pstmt.setString(1, data[0].trim());
                pstmt.setString(2, data[1].trim());
                pstmt.setString(3, data[2].trim().replace("\"", ""));
                pstmt.setString(4, data[3].trim());
                pstmt.setDouble(5, Double.parseDouble(data[4].trim()));
                pstmt.setBoolean(6, "Available".equals(data[5].trim()));
    
                // Improved image loading with proper resource management
                String imagePath = "src/main/java/software_design/database/data/item_img/" + data[6].trim();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) 
                {
                    try (FileInputStream fis = new FileInputStream(imageFile)) 
                    {
                        pstmt.setBinaryStream(7, fis, imageFile.length());
                        pstmt.executeUpdate();
                    }
                } 
                else 
                {
                    System.out.println("Image not found: " + imagePath);
                    pstmt.setBytes(7, new byte[0]);
                    pstmt.executeUpdate();
                }
            }
        }
    }
}
