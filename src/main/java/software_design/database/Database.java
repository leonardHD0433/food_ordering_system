package software_design.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    private static Database instance;
    private Dotenv dotenv = Dotenv.load();
    private String DB_HOST;
    private String DB_PORT;
    private String DB_NAME;
    private String DB_USER;
    private String DB_PASS;
    private String DB_URL_NO_DB;
    private String DB_URL;

    Database()
    {
        DB_HOST = dotenv.get("DB_HOST");
        DB_PORT = dotenv.get("DB_PORT");
        DB_USER = dotenv.get("DB_USER");
        DB_PASS = dotenv.get("DB_PASSWORD");
        DB_NAME = dotenv.get("DB_NAME");
        DB_URL_NO_DB = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT;
        DB_URL = DB_URL_NO_DB + "/" + DB_NAME;
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createDatabase() throws SQLException 
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

    public Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void createTables() throws SQLException 
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

    public void importMenuData() throws SQLException, IOException {
        String insertSQL = "INSERT INTO menu_table (ItemCategory, ItemName, ItemDescription, ItemOptions, ItemPrice, ItemAvailability, ItemImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String checkSQL = "SELECT COUNT(*) FROM menu_table";
        String csvFile = "src/main/java/software_design/database/data/menu.csv";
        String line;
        boolean firstLine = true;
    
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

                        // Check if table is empty
                ResultSet rs = stmt.executeQuery(checkSQL);
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("Menu table already has data. Skipping import.");
                    return;
                }
    
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

    public void updateMenuItem(String category, String name, String description, String options, double price, boolean isAvailable, byte[] image , int id) throws SQLException {
        String updateSQL = "UPDATE menu_table SET " +
                          "ItemCategory = ?, " +
                          "ItemName = ?, " +
                          "ItemDescription = ?, " +
                          "ItemOptions = ?, " +
                          "ItemPrice = ?, " +
                          "ItemAvailability = ?, " +
                          "ItemImage = ? " +
                          "WHERE ItemId = ?";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            // Set parameters
            pstmt.setString(1, category);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setString(4, options);
            pstmt.setDouble(5, price);
            pstmt.setBoolean(6, isAvailable);
            pstmt.setBytes(7, image);
            pstmt.setInt(8, id);
            
            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update menu item, no rows affected.");
            }
        }
    }

    public void removeMenuITem(int id) throws SQLException {
        String deleteSQL = "DELETE FROM menu_table WHERE ItemId = ?";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            
            // Set parameter
            pstmt.setInt(1, id);
            
            // Execute delete
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete menu item, no rows affected.");
            }
        }
    }
}
