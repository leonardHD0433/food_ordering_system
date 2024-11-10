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
import software_design.model.Menu;
import software_design.model.MenuItem;

public class MenuController 
{
    @FXML private GridPane menuGrid;
    @FXML private Label tableNumberLabel;
    @FXML private HBox filterContainer;
    @FXML private ScrollPane filterScrollPane;
    @FXML private ScrollPane menuScrollPane;
    
    private MenuView menuView;
    private Menu menu;
    private List<MenuItem> menuItems;
    private List<String> categories;
    
    @FXML
    private void initialize() 
    {
        // Get data from model
        TableManager tableManager = TableManager.getInstance();
        Table currentTable = tableManager.getCurrentTable();
        
        // Initialize view with data
        menu = new Menu();
        menuView = new MenuView(menuGrid, tableNumberLabel, filterContainer, this::handleMenuItemClick);
        
        try 
        {
            menuItems = menu.getMenuItems();
            categories = menu.getDistinctCategories();

            menuView.setupMenuGrid(menuItems, this::handleMenuItemClick);
            menuView.setTableNumberLabel(currentTable.getTableId());
            menuView.setupFilterButtons(categories, this::handleFilterClick);
            menuView.styleScrollPanes(filterScrollPane, menuScrollPane);
        } catch (SQLException e) {
            // Handle error - maybe show an alert
            System.err.println("Error loading menu items: " + e.getMessage());
        }
    }

    private void handleFilterClick(String category) 
    {
        try 
        {
            List<MenuItem> filteredItems;
            if ("All".equals(category)) 
            {
                filteredItems = menu.getMenuItems();
            } 
            else 
            {
                filteredItems = menuItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .collect(Collectors.toList());
            }
            menuView.setupMenuGrid(filteredItems, this::handleMenuItemClick);
        } 
        catch (SQLException e) 
        {
            System.err.println("Error filtering menu items: " + e.getMessage());
        }
    }

    private void handleMenuItemClick(MenuItem item) 
    {
        // Handle the menu item click here
        System.out.println("Clicked: " + item.getName());
    }
}
