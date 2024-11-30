package software_design.controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import software_design.model.*;
import software_design.view.OrderProcessingPage.ProcessOrderView;
import software_design.App;

public class ProcessOrderController {

    @FXML
    private VBox ordersContainer;

    @FXML
    private ComboBox<String> tableFilterComboBox;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        loadTableIds();
        loadPendingOrders();
    }

    private void loadTableIds() {
        ObservableList<String> tableIds = FXCollections.observableArrayList();
        tableIds.add("All Tables"); // Default option

        Table[] tables = TableManager.getInstance().getAllTables();
        for (Table table : tables) {
            Order order = table.getOrder();
            if (order != null && order.hasPendingItems()) {
                String tableIdString = "Table " + table.getTableId();
                if (!tableIds.contains(tableIdString)) {
                    tableIds.add(tableIdString);
                }
            }
        }

        tableFilterComboBox.setItems(tableIds);
        tableFilterComboBox.setValue("All Tables"); // Set default value
    }

    @FXML
    private void handleTableFilterChange() {
        loadPendingOrders();
    }

    private void loadPendingOrders() {
        ordersContainer.getChildren().clear();
        boolean hasPendingOrders = false;
        String selectedTable = tableFilterComboBox.getValue();

        Table[] tables = TableManager.getInstance().getAllTables();
        for (Table table : tables) {
            String tableIdString = "Table " + table.getTableId();
            if (!"All Tables".equals(selectedTable) && !tableIdString.equals(selectedTable)) {
                continue;
            }

            Order order = table.getOrder();
            if (order != null) {
                for (int i = 0; i < order.getItems().size(); i++) {
                    String status = order.getItemStatus().get(i);
                    if ("Pending".equals(status)) {
                        hasPendingOrders = true;

                        MenuItem item = order.getItems().get(i);
                        String option = order.getOptions().get(i);
                        String remarks = order.getRemarks().get(i);
                        int tableId = table.getTableId();

                        final int itemIndex = i;
                        final int tableID = tableId;
                        Node itemNode = ProcessOrderView.createOrderProcessingItemView(
                            item, 1, option, remarks, tableID, itemIndex, e -> handleCompleteItem(tableID, itemIndex)
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
            order.setItemStatus(itemIndex, "Completed");

            // Reload the pending orders view
            loadPendingOrders();
        }
    }

    @FXML
    private void handleBackClick() {
        App.setRoot("AdminPage/admin_homepage");
    }
}