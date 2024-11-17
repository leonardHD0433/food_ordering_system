package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import software_design.model.Order;
import software_design.model.Table;
import software_design.model.TableManager;
import software_design.App;
import software_design.view.CartView;
import software_design.view.OrderView;
import javafx.scene.control.Button;

public class OrderController {
    @FXML private Label tableLabel;
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
            tableLabel.setText("Table " + currentOrder.getTableId());
            orderTimeLabel.setText("Order Time: " + currentOrder.getOrderTime());
            totalLabel.setText(String.format("%.2f", currentOrder.getTotal()));
            updateOrderView();
        }
    }
    
    @FXML
    private void handleBackClick() {
        App.setRoot("menu");
    }
    
    private void updateOrderView() {
        orderItemsContainer.getChildren().clear();
        for (int i = 0; i < currentOrder.getItems().size(); i++) {
            VBox itemView = OrderView.createOrderItemView(
                currentOrder.getItems().get(i),
                currentOrder.getQuantities().get(i),
                currentOrder.getOptions().get(i),
                currentOrder.getRemarks().get(i),
                currentOrder.getItemStatus().get(i)
            );
            orderItemsContainer.getChildren().add(itemView);
        }
    }
}
