package software_design.model;

import software_design.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {

    private List<MenuItem> menuItems;
    private List<String> distinct_categories;

    public List<MenuItem> getMenuItems(String role) throws SQLException 
    {
        String query = "";
        menuItems = new ArrayList<>();
        if (role.equals("admin")) {
            query = "SELECT * FROM menu_table";
        } else {
            query = "SELECT * FROM menu_table WHERE ItemAvailability = true";
        }
        
        try (Connection conn = Database.getConnection();
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

        try (Connection conn = Database.getConnection();
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
        distinct_categories = new ArrayList<>();
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
