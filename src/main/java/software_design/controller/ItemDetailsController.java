package software_design.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import software_design.App;
import software_design.model.*;
import software_design.model.MenuItem;
import software_design.view.ItemDetailsPage.ItemDetailsView;
import javafx.util.Duration;

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
        App.setRoot("MenuPage/menu");
    }

    @FXML
    private void handleCartClick() {
        App.setRoot("CartPage/cart");
    }

    @FXML
    private void handleAddToCart() {
        if (currentItem == null) {
            return;
        }
        
        // Get user inputs from view
        String selectedOption = view.getSelectedOption();
        if(selectedOption == "must select") {
            showTemporaryAlert("Please select an option!", "#F44336");
            return;
        }

        int quantity = view.getQuantity();
        String remarks = view.getRemarks();
        if (!isValidRemarks(remarks)) {
            showTemporaryAlert("Remarks can only contain letters, numbers and basic punctuation (.,!?)", "#F44336");
            return;
        }
        
        // Add item to cart
        Table currentTable = TableManager.getInstance().getCurrentTable();
        Cart cart = currentTable.getCart();
        cart.addItem(currentItem, quantity, selectedOption, remarks);
        
        // Reset and return to menu
        view.reset();
        showTemporaryAlert("Item added to cart successfully!", "#4CAF50");
        App.setRoot("MenuPage/menu");
    }

    private boolean isValidRemarks(String remarks) {
        if (remarks == null || remarks.isEmpty()) {
            return true; // Empty remarks are valid
        }
        // Allow letters, numbers, spaces and basic punctuation
        return remarks.matches("^[a-zA-Z0-9\\s.,!?]+$");
    }

    private void showTemporaryAlert(String message, String color) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.getDialogPane().setStyle("-fx-background-color: " + color + ";");

        // Remove window decorations
        alert.initStyle(StageStyle.UNDECORATED);
 
        Window mainWindow = App.getScene().getWindow();
    
        // Make alert follow the main window
        alert.initOwner(mainWindow);
        
        // Show first for dimensions calculation
        alert.show();
        
        // Center the alert on the main window
        double centerX = mainWindow.getX() + (mainWindow.getWidth() - alert.getWidth()) / 2;
        double centerY = mainWindow.getY() + (mainWindow.getHeight() - alert.getHeight()) / 2;
        
        alert.setX(centerX);
        alert.setY(centerY);

        // Auto close after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> alert.close());
        delay.play();
    }
}