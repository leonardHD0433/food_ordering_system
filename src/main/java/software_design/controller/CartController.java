package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import software_design.App;
import software_design.model.Cart;
import software_design.view.CartView;

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

    private void updateCartView() 
    {
        if (cart.isEmpty()) 
        {
            CartView.showEmptyCart(cartItemsContainer);
            CartView.hideBottomControls(bottomControls);
        } 
        else 
        {
            // Show cart items
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