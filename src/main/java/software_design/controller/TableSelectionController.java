package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import software_design.App;
import software_design.model.Cart;
import software_design.model.Order;
import software_design.model.Table;
import software_design.model.TableManager;
import software_design.view.TableSelectionPage.TableSelectionView;

public class TableSelectionController {
    @FXML private Label alert;
    @FXML private GridPane tableMapGrid;
    @FXML private Button adminButton;

    private TableSelectionView view;
    private TableManager tableManager;

    @FXML
    private void initialize() {
        view = new TableSelectionView(alert, tableMapGrid);
        tableManager = TableManager.getInstance();
        
        // Create the table map and set the handler for table selection
        view.createTableMap(this::handleTableSelection);
    }

    private void handleTableSelection(int tableId) {
        try {
            Table table = tableManager.getTable(tableId);
            
            // Create an empty order if one doesn't exist to mark table as "in use"
            if (table.getOrder() == null) {
                // Create a new empty order to mark the table as "in use"
                Order newOrder = new Order(String.valueOf(tableId), tableId, new Cart());
                table.setOrder(newOrder);
            }
            
            // Set this as the current table and proceed to menu
            tableManager.setTable(tableId, table); 
            tableManager.setCurrentTable(tableId);           
            App.setRoot("MenuPage/menu");
        } catch (Exception e) {
            view.showErrorWithFade("Error selecting table: " + e.getMessage());
        }
    }

    @FXML
    private void handleSwitchAdmin() {
        App.setRoot("AdminPage/admin_homepage");
    }
}