package software_design.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import software_design.model.Order;
import software_design.model.ConsolidatedOrder;
import software_design.model.Table;
import software_design.model.TableManager;
import software_design.App;
import software_design.view.CartPage.CartView;
import software_design.view.OrderPage.OrderView;
import javafx.scene.control.Button;

public class OrderController {
    @FXML private Label orderTimeLabel;
    @FXML private Label totalLabel;
    @FXML private VBox orderItemsContainer;
    @FXML private Button backButton;
    
    private Table currentTable;
    private Order currentOrder;
    
    @FXML
    private void initialize() {
        currentTable = TableManager.getInstance().getCurrentTable();
        currentOrder = currentTable.getOrder();
        CartView.styleBackButton(backButton);
        
        if (currentOrder != null) {
            orderTimeLabel.setText("Last Updated: " + currentOrder.getOrderTime());
            totalLabel.setText(String.format("%.2f", currentOrder.getTotal()));
            
            // Only update view if there are items
            if (!currentOrder.getItems().isEmpty()) {
                updateOrderView();
            }
        }
    }
    
    @FXML
    private void handleBackClick() {
        App.setRoot("MenuPage/menu");
    }
    
    private void updateOrderView() {
        orderItemsContainer.getChildren().clear();
        List<ConsolidatedOrder> consolidatedItems = currentOrder.getConsolidatedView();
        for (ConsolidatedOrder consolidatedItem : consolidatedItems) {
            VBox itemView = OrderView.createOrderItemView(
                consolidatedItem.getItem(),
                consolidatedItem.getQuantity(),
                consolidatedItem.getOption(),
                consolidatedItem.getRemark(),
                consolidatedItem.getStatus()
            );
            orderItemsContainer.getChildren().add(itemView);
        }
    }
}
