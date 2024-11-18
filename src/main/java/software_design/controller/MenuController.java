package software_design.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import software_design.model.TableManager;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import software_design.model.Table;
import software_design.view.MenuView;
import software_design.App;
import software_design.model.Menu;
import software_design.model.MenuItem;
import javafx.scene.control.Button;

public class MenuController {
    @FXML private GridPane menuGrid;
    @FXML private Label tableNumberLabel;
    @FXML private HBox filterContainer;
    @FXML private ScrollPane filterScrollPane;
    @FXML private ScrollPane menuScrollPane;
    @FXML private Button cartButton;
    @FXML private Button orderButton;
    
    private MenuView menuView;
    private Menu menu;
    private List<MenuItem> menuItems;
    private List<String> categories;
    private static MenuItem selectedMenuItem;
    
    @FXML
    private void initialize() {
        TableManager tableManager = TableManager.getInstance();
        Table currentTable = tableManager.getCurrentTable();
        
        menu = new Menu();
        menuView = new MenuView(menuGrid, tableNumberLabel, filterContainer, this::handleMenuItemClick);
        
        try {
            menuItems = menu.getMenuItems();
            categories = menu.getDistinctCategories();

            menuView.setupMenuGrid(menuItems);
            menuView.setTableNumberLabel(currentTable.getTableId());
            menuView.setupFilterButtons(categories, this::handleFilterClick);
            menuView.styleScrollPanes(filterScrollPane, menuScrollPane);

            // Hide/show order button based on order existence
            orderButton.setVisible(currentTable.getOrder() != null);
            orderButton.setManaged(currentTable.getOrder() != null);
            menuView.styleOrderButton(orderButton);
        } catch (SQLException e) {
            System.err.println("Error loading menu items: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        // Return to table selection page
        App.setRoot("table_selection");
    }

    @FXML
    private void handleCartClick() {
        App.setRoot("cart");
    }

    @FXML
    private void handleOrderClick() {
        App.setRoot("order");
    }

    private void handleFilterClick(String category) {
        try {
            List<MenuItem> filteredItems = "All".equals(category) ? 
                menu.getMenuItems() : 
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
        App.setRoot("item_details");
    }
    
    // Getter for selected menu item
    public static MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }
}