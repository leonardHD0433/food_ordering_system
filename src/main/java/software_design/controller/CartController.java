package software_design.controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import software_design.App;
import software_design.model.Cart;
import software_design.view.CartView;
import software_design.model.MenuItem;
import software_design.model.TableManager;
import software_design.model.Table;
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

    @FXML
    private void initialize() 
    {
        tableManager = TableManager.getInstance();
        currentTable = tableManager.getCurrentTable();
        this.cart = currentTable.getCart();
        updateCartView();
    }

    private void updateCartView() {
        if (cart.isEmpty()) {
            CartView.showEmptyCart(cartItemsContainer);
            CartView.hideBottomControls(bottomControls);
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

                itemBox = CartView.createCartItemView(
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
            CartView.showBottomControls(bottomControls);
        }
    }

    @FXML
    private void handleBackClick() 
    {
        App.setRoot("menu");
    }

    @FXML
    private void handleCheckout() 
    {
        // Handle checkout logic
    }
}