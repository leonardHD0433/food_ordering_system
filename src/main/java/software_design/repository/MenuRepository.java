package software_design.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Statement;
import java.util.ArrayList;

import software_design.database.Database;
import software_design.model.MenuItem;

public class MenuRepository {
    private final Database database;

    public MenuRepository() {
        this.database = Database.getInstance();
    }

    public List<MenuItem> getMenuItems(String role) throws SQLException 
    {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "";
        if (role.equals("admin")) {
            query = "SELECT * FROM menu_table";
        } else {
            query = "SELECT * FROM menu_table WHERE ItemAvailability = true";
        }
        
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getInt("ItemId"),
                    rs.getString("ItemCategory"),
                    rs.getString("ItemName"),
                    rs.getString("ItemDescription"),
                    rs.getString("ItemOptions"),
                    rs.getDouble("ItemPrice"),
                    rs.getBoolean("ItemAvailability"),
                    rs.getBytes("ItemImage")
                );
                menuItems.add(item);
            }
        }
        return menuItems;
    }

    public void createItem(MenuItem item) throws SQLException 
    {
        String query = "INSERT INTO menu_table (ItemCategory, ItemName, ItemDescription, ItemOptions, ItemPrice, ItemAvailability, ItemImage) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, item.getCategory());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setString(4, item.getOptions());
            stmt.setDouble(5, item.getPrice());
            stmt.setBoolean(6, item.isAvailable());
            stmt.setBytes(7, item.getImage());
            stmt.executeUpdate();
        }
    }

    public List<String> getDistinctCategories(String role) throws SQLException 
    {
        List<String> distinct_categories = new ArrayList<>();
        distinct_categories.add("All"); // Add default option
    
        List<MenuItem> menuItems = getMenuItems(role);
    
        // Use stream to get distinct distinct_categories
        distinct_categories.addAll(
            menuItems.stream()
                .map(MenuItem::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList())
        );
    
        return distinct_categories;
    }
}
