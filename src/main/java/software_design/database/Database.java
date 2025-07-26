package software_design.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database instance; //Add a private static field to the class for storing the singleton instance.
    private final String DB_PATH = System.getProperty("user.dir") + 
                                File.separator + "foodorderingsystem.db";
    private final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    // Make the constructor of the class private. 
    // The static method of the class will still be able to call the constructor, but not the other objects.
    private Database()
    {
        File dbFile = new File(DB_PATH);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

            // Load SQLite driver explicitly
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading SQLite JDBC driver: " + e.getMessage());
        }
    }

    // Implement “lazy initialization” inside the static method. 
    // It should create a new object on its first call and put it into the static field. 
    // The method should always return that instance on all subsequent calls.
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createDatabase() throws SQLException 
    {
        // Create the database if it does not exist
        try (Connection conn = getConnection()) {
            System.out.println("SQLite database created or connected successfully");
        }
        catch (SQLException e) {
            System.out.println("Error creating or connecting to SQLite database: " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the calling method
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void createTables() throws SQLException 
    {
        String createMenuTableSQL = "CREATE TABLE IF NOT EXISTS menu_table (" +
                "ItemId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ItemCategory TEXT NOT NULL, " +
                "ItemName TEXT NOT NULL, " +
                "ItemDescription TEXT NOT NULL, " +
                "ItemOptions TEXT, " +
                "ItemPrice REAL NOT NULL, " +
                "ItemAvailability INTEGER NOT NULL," + 
                "ItemImage BLOB NOT NULL" +
                ")";

        String createTableStatusSQL = "CREATE TABLE IF NOT EXISTS table_status (" +
                                      "TableId INTEGER PRIMARY KEY" +
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
                    try {
                        // Read the entire file as a byte array
                        byte[] imageData = Files.readAllBytes(imageFile.toPath());
                        // Set the bytes directly instead of using setBinaryStream
                        pstmt.setBytes(7, imageData);
                        pstmt.executeUpdate();
                    } catch (IOException e) {
                        System.out.println("Error reading image file: " + e.getMessage());
                        pstmt.setBytes(7, new byte[0]);
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

    public void removeMenuItem(int id) throws SQLException {
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
