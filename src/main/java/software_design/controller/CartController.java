package software_design.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import software_design.App;
import software_design.model.Cart;
import software_design.view.CartView;
import software_design.model.MenuItem;

public class CartController 
{
    @FXML private VBox cartItemsContainer;
    @FXML private Label totalLabel;
    @FXML private Button backButton;
    @FXML private Button checkoutButton;
    @FXML private VBox bottomControls;
    
    private Cart cart;

    @FXML
    private void initialize() 
    {
        cart = new Cart();
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
                VBox itemBox = CartView.createCartItemView(
                    items.get(i),
                    quantities.get(i),
                    options.get(i),
                    remarks.get(i)
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