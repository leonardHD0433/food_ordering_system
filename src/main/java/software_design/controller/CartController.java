package software_design.controller;

import java.util.List;
import java.util.Optional;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import software_design.App;
import software_design.model.Cart;
import software_design.model.MenuItem;
import software_design.model.TableManager;
import software_design.view.CartPage.CartView;
import software_design.model.Table;
import software_design.model.Order;
import javafx.event.EventHandler;

public class CartController 
{
    @FXML private VBox cartItemsContainer;
    @FXML private Label totalLabel;
    @FXML private Button backButton;
    @FXML private Button checkoutButton;
    @FXML private VBox bottomControls;
    
    private TableManager tableManager;
    private Table currentTable;
    private Cart cart;
    private VBox itemBox;
    private Order existingOrder;
    private Order newOrder;
    private CartView cartView;


    @FXML
    private void initialize() 
    {
        cartView = new CartView();
        tableManager = TableManager.getInstance();
        currentTable = tableManager.getCurrentTable();
        this.cart = currentTable.getCart();
        cartView.styleBackButton(backButton);
        updateCartView();
    }

    private void updateCartView() {
        if (cart.isEmpty()) {
            cartView.showEmptyCart(cartItemsContainer);
            cartView.hideBottomControls(bottomControls);
        } else {
            cartItemsContainer.getChildren().clear();
            List<MenuItem> items = cart.getItems();
            List<Integer> quantities = cart.getQuantities();
            List<String> options = cart.getOptions();
            List<String> remarks = cart.getRemarks();
            
            for (int i = 0; i < items.size(); i++) {
                int index = i; // For lambda expression

                EventHandler<ActionEvent> minusHandler = e -> {
                    int newQuantity = quantities.get(index) - 1;
                    if (newQuantity == 0) {
                        // Remove item from all lists
                        cart.getItems().remove(index);
                        cart.getQuantities().remove(index);
                        cart.getOptions().remove(index);
                        cart.getRemarks().remove(index);
                        cart.updateTotal();
                        updateCartView(); // Refresh view
                    } 
                    else if (newQuantity >= 1) {
                        cart.getQuantities().set(index, newQuantity);
                        cart.updateTotal();
                        updateCartView(); // Refresh view
                    }
                };

                EventHandler<ActionEvent> plusHandler = e -> {
                    int newQuantity = quantities.get(index) + 1;
                    if (newQuantity <= 20) {
                        cart.getQuantities().set(index, newQuantity);
                        cart.updateTotal();
                        updateCartView(); // Refresh view
                    }
                };

                itemBox = cartView.createCartItemView(
                    items.get(i),
                    quantities.get(i),
                    options.get(i),
                    remarks.get(i),
                    minusHandler,
                    plusHandler
                );
                cartItemsContainer.getChildren().add(itemBox);
            }
            
            totalLabel.setText(String.format("%.2f", cart.getTotal()));
            cartView.showBottomControls(bottomControls);
        }
    }

    @FXML
    private void handleBackClick() 
    {
        App.setRoot("MenuPage/menu");
    }

    @FXML
    private void handleCheckout() {
        if (cart.isEmpty()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText("Are you sure you want to place this order?");
        alert.setContentText(String.format("Total Amount: RM%.2f", cart.getTotal()));
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(App.getScene().getWindow());

        // Position alert
        Window mainWindow = App.getScene().getWindow();
        alert.setX(mainWindow.getX() + (mainWindow.getWidth() - alert.getWidth()) / 2);
        alert.setY(mainWindow.getY() + (mainWindow.getHeight() - alert.getHeight()) / 2);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                existingOrder = currentTable.getOrder();
                if (existingOrder == null) {
                    // Create new order
                    newOrder = new Order(String.valueOf(currentTable.getTableId()), 
                                            currentTable.getTableId(), cart);
                    currentTable.setOrder(newOrder);
                } else {
                    // Add items to existing order
                    existingOrder.addItems(cart);
                }
                currentTable.clearCart();

                showTemporaryAlert("Order placed successfully!", "#4CAF50");
                App.setRoot("MenuPage/menu");
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Failed to create order");
                error.showAndWait();
            }
        }
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