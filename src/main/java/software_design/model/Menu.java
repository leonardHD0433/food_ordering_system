package software_design.model;

import software_design.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {

    private List<MenuItem> menuItems;
    private List<String> distinct_categories;

    public List<MenuItem> getMenuItems() throws SQLException 
    {
        menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu_table WHERE ItemAvailability = true";
        
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

    public List<String> getDistinctCategories() throws SQLException 
    {
        distinct_categories = new ArrayList<>();
        distinct_categories.add("All"); // Add default option
    
        List<MenuItem> menuItems = getMenuItems();
    
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
