package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import software_design.App;
import software_design.model.*;
import software_design.model.MenuItem;
import software_design.view.ItemDetailsView;

public class ItemDetailsController {
    @FXML private Label tableNumberLabel;
    @FXML private ImageView itemImage;
    @FXML private Label itemNameLabel;
    @FXML private Label itemDescLabel;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private VBox optionsBox;
    @FXML private TextArea remarksArea;
    @FXML private ScrollPane detailsScrollPane;
    private MenuItem currentItem;
    private ItemDetailsView view;

    @FXML
    public void initialize() {
        view = new ItemDetailsView(tableNumberLabel, itemImage, itemNameLabel, 
                                itemDescLabel, quantitySpinner, optionsBox, 
                                remarksArea, detailsScrollPane);
        
        // Get current table number
        Table currentTable = TableManager.getInstance().getCurrentTable();
        view.setTableNumber(currentTable.getTableId());
        
        // Get and display the selected menu item
        MenuItem selectedItem = MenuController.getSelectedMenuItem();
        if (selectedItem != null) {
            setMenuItem(selectedItem);
        }
    }

    public void setMenuItem(MenuItem item) {
        this.currentItem = item;
        view.displayItem(item);
    }

    @FXML
    private void handleClose() {
        view.reset();
        App.setRoot("menu");
    }

    @FXML
    private void handleCartClick() {
        App.setRoot("cart");
    }

    @FXML
    private void handleAddToCart() {
        if (currentItem == null) {
            return;
        }
        
        // Get user inputs from view
        String selectedOption = view.getSelectedOption();
        int quantity = view.getQuantity();
        String remarks = view.getRemarks();
        
        // Add item to cart
        
        // Reset and return to menu
        view.reset();
        App.setRoot("menu");
    }
}