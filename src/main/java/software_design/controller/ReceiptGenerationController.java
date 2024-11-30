package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import software_design.model.Table;
import software_design.model.TableManager;
import software_design.App;
import software_design.model.Order;
import software_design.view.ReceiptGenerationPage.ReceiptGenerationView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ReceiptGenerationController {

    @FXML
    private TextField tableNumberInput;
    @FXML
    private Button generateButton;
    @FXML
    private VBox receiptContainer;

    private TableManager tableManager;
    private ReceiptGenerationView view;
    private Table currentTable;

    @FXML
    private void initialize() {
        tableManager = TableManager.getInstance();
        view = new ReceiptGenerationView(receiptContainer, this::onReceiptGenerated);
    }

    @FXML
    private void handleGenerateReceipt() {
        String tableNumberText = tableNumberInput.getText();
        if (tableNumberText.isEmpty()) {
            showAlert("Please enter a table number.");
            return;
        }

        int tableNumber;
        try {
            tableNumber = Integer.parseInt(tableNumberText);
        } catch (NumberFormatException e) {
            showAlert("Invalid table number.");
            return;
        }

        if (tableNumber < 1 || tableNumber > 20) {
            showAlert("Table number must be between 1 and 20.");
            return;
        }

        currentTable = tableManager.getTable(tableNumber);
        Order order = currentTable.getOrder();

        if (order == null) {
            showAlert("No orders for this table yet.");
            return;
        }

        if (order.hasPendingItems()) {
            showAlert("This table still has incoming orders.");
            return;
        }

        // Display the receipt
        view.displayReceipt(order);


    }

    private void onReceiptGenerated() {
        // Clear the table's cart and order
        tableManager.clearTable(currentTable.getTableId());

        // Redirect to admin homepage
        App.setRoot("AdminPage/admin_homepage");
    }

    @FXML
    private void handleCancel() {
        App.setRoot("AdminPage/admin_homepage");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Receipt Generation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) receiptContainer.getScene().getWindow();
        alert.initOwner(stage);
        alert.showAndWait();
    }
}