package software_design.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import software_design.App;
import software_design.model.Menu;
import software_design.model.MenuItem;
import software_design.view.MenuPage.MenuView;

public class MenuAdminController {
    @FXML private GridPane menuGrid;
    @FXML private Label tableNumberLabel;
    @FXML private HBox filterContainer;
    @FXML private ScrollPane filterScrollPane;
    @FXML private ScrollPane menuScrollPane;
    
    private MenuView menuView;
    private Menu menu;
    private List<MenuItem> menuItems;
    private List<String> categories;
    private static MenuItem selectedMenuItem;
    
    @FXML
    private void initialize() {
        menu = new Menu();
        menuView = new MenuView(menuGrid, tableNumberLabel, filterContainer, this::handleMenuItemClick);
        
        try {
            menuItems = menu.getMenuItems("admin");
            categories = menu.getDistinctCategories("admin");

            menuView.setupMenuGrid(menuItems);
            menuView.setupFilterButtons(categories, this::handleFilterClick);
            menuView.styleScrollPanes(filterScrollPane, menuScrollPane);
        } catch (SQLException e) {
            System.err.println("Error loading menu items: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        App.setRoot("MenuModificationPage/menu_modification");
    }

    private void handleFilterClick(String category) {
        try {
            List<MenuItem> filteredItems = "All".equals(category) ? 
                menu.getMenuItems("admin") : 
                menuItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .collect(Collectors.toList());
            menuView.setupMenuGrid(filteredItems);
        } catch (SQLException e) {
            System.err.println("Error filtering menu items: " + e.getMessage());
        }
    }

    private void handleMenuItemClick(MenuItem item) {
        // Store selected item
        selectedMenuItem = item;
            
        // Switch to item details view (now properly throws IOException)
        App.setRoot("AdminItemDetailsPage/item_details_admin");
    }
    
    // Getter for selected menu item
    public static MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }
}