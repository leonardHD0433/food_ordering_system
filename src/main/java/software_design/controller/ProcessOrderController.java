package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import software_design.model.*;
import software_design.view.OrderProcessingPage.ProcessOrderView;
import software_design.App;

public class ProcessOrderController {

    @FXML
    private VBox ordersContainer;

    @FXML
    private void initialize() {
        loadPendingOrders();
    }

    private void loadPendingOrders() {
        ordersContainer.getChildren().clear();
        boolean hasPendingOrders = false;
        Table[] tables = TableManager.getInstance().getAllTables();
        for (Table table : tables) {
            Order order = table.getOrder();
            if (order != null) {
                for (int i = 0; i < order.getItems().size(); i++) {
                    String status = order.getItemStatus().get(i);
                    if ("Pending".equals(status)) {
                        hasPendingOrders = true;
                        MenuItem item = order.getItems().get(i);
                        int quantity = order.getQuantities().get(i);
                        String option = order.getOptions().get(i);
                        String remarks = order.getRemarks().get(i);
                        int tableId = table.getTableId();
    
                        final int itemIndex = i;
                        final int tableID = tableId;
                        Node itemNode = ProcessOrderView.createOrderProcessingItemView(
                            item, quantity, option, remarks, tableID, itemIndex, e -> handleCompleteItem(tableID, itemIndex)
                        );
                        ordersContainer.getChildren().add(itemNode);
                    }
                }
            }
        }
    
        if (!hasPendingOrders) {
            ordersContainer.getChildren().add(ProcessOrderView.createNoOrdersLabel());
        }
    }

    private void handleCompleteItem(int tableId, int itemIndex) {
        Table table = TableManager.getInstance().getTable(tableId);
        Order order = table.getOrder();
    
        if (order != null) {
            // Update the item status to "Complete"
            order.setItemStatus(itemIndex, "Complete");

            // Reload the pending orders view
            loadPendingOrders();
        }
    }

    @FXML
    private void handleBackClick() {
        App.setRoot("AdminPage/admin_homepage");
    }
}