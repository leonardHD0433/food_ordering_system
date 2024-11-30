package software_design.view.ReceiptGenerationPage;

import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import software_design.model.Order;
import software_design.model.ConsolidatedOrder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReceiptGenerationView {

    private VBox receiptContainer;
    private Runnable onReceiptGeneratedCallback;

    public ReceiptGenerationView(VBox receiptContainer, Runnable onReceiptGeneratedCallback) {
        this.receiptContainer = receiptContainer;
        this.onReceiptGeneratedCallback = onReceiptGeneratedCallback;
    }

    public void displayReceipt(Order order) {
        receiptContainer.getChildren().clear();
    
        // Title
        Label titleLabel = new Label("Receipt for Table " + (order.getTableId() + 1));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        receiptContainer.getChildren().add(titleLabel);
    
        addHorizontalLine(receiptContainer);
        addReceiptHeader(receiptContainer);
        addHorizontalLine(receiptContainer);
    
        // Create grid for items with same column constraints as header
        GridPane itemsGrid = new GridPane();
        itemsGrid.setHgap(20);
        
        // Use same column constraints as header
        ColumnConstraints itemCol = new ColumnConstraints();
        itemCol.setMinWidth(200);
        itemCol.setHgrow(Priority.ALWAYS);
        
        ColumnConstraints qtyCol = new ColumnConstraints();
        qtyCol.setMinWidth(50);
        qtyCol.setHalignment(HPos.CENTER);
        
        ColumnConstraints priceCol = new ColumnConstraints();
        priceCol.setMinWidth(80);
        priceCol.setHalignment(HPos.RIGHT);
        
        itemsGrid.getColumnConstraints().addAll(itemCol, qtyCol, priceCol);
    
        // Add items
        int row = 0;
        for (ConsolidatedOrder item : order.getConsolidatedView()) {
            Label nameLabel = new Label(item.getItem().getName());
            Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
            Label priceLabel = new Label(String.format("RM%.2f", 
                item.getItem().getPrice() * item.getQuantity()));
    
            itemsGrid.add(nameLabel, 0, row);
            itemsGrid.add(quantityLabel, 1, row);
            itemsGrid.add(priceLabel, 2, row);
            row++;
        }
    
        receiptContainer.getChildren().add(itemsGrid);
    
        addHorizontalLine(receiptContainer);
    
        // Total amount with right alignment
        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label(String.format("Total: RM%.2f", order.getTotal()));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        totalBox.getChildren().add(totalLabel);
        receiptContainer.getChildren().add(totalBox);
    
        addHorizontalLine(receiptContainer);
    
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        Button printButton = new Button("Print");
        buttonContainer.getChildren().add(printButton);

        printButton.setOnAction(e -> showSaveConfirmation(order));
        receiptContainer.getChildren().add(buttonContainer);
    }

    private void addHorizontalLine(VBox container) {
        // Create separator line that scales with window
        Region line = new Region();
        line.setStyle(
            "-fx-background-color: black;" +
            "-fx-pref-height: 2px;" + 
            "-fx-min-width: 100%;"
        );
        container.getChildren().add(line);
    }

    private void addReceiptHeader(VBox container) {
        // Create header with fixed column widths
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        // Use GridPane for consistent column alignment
        GridPane grid = new GridPane();
        grid.setHgap(20); // Horizontal gap between columns
        
        // Configure column constraints
        ColumnConstraints itemCol = new ColumnConstraints();
        itemCol.setMinWidth(200); // Adjust width as needed
        itemCol.setHgrow(Priority.ALWAYS);
        
        ColumnConstraints qtyCol = new ColumnConstraints();
        qtyCol.setMinWidth(50); // Fixed width for quantity
        qtyCol.setHalignment(HPos.CENTER); // Center align quantity
        
        ColumnConstraints priceCol = new ColumnConstraints();
        priceCol.setMinWidth(80); // Fixed width for price
        priceCol.setHalignment(HPos.RIGHT); // Right align price
        
        grid.getColumnConstraints().addAll(itemCol, qtyCol, priceCol);
        
        // Add header labels
        Label itemLabel = new Label("Item");
        Label qtyLabel = new Label("Qty");
        Label priceLabel = new Label("Price");
        
        // Add to grid
        grid.add(itemLabel, 0, 0);
        grid.add(qtyLabel, 1, 0);
        grid.add(priceLabel, 2, 0);
        
        headerBox.getChildren().add(grid);
        HBox.setHgrow(grid, Priority.ALWAYS);
        container.getChildren().add(headerBox);
    }

    private void showSaveConfirmation(Order order) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save Receipt");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to save the receipt?");
        
        Stage primaryStage = (Stage) receiptContainer.getScene().getWindow();
        alert.initOwner(primaryStage);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            saveReceiptToFile(order);
        }
    }

    private void saveReceiptToFile(Order order) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Receipt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    
        // Set initial directory to the default Downloads folder
        File downloadsFolder = new File(System.getProperty("user.home"), "Downloads");
        fileChooser.setInitialDirectory(downloadsFolder);
    
        fileChooser.setInitialFileName("receipt.txt");
    
        File file = fileChooser.showSaveDialog(receiptContainer.getScene().getWindow());
    
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                StringBuilder receipt = new StringBuilder();
                receipt.append("===================================================================================\n");
                receipt.append("Receipt for Table ").append((order.getTableId() + 1)).append("\n");
                receipt.append("===================================================================================\n");
                receipt.append(String.format("%-60s %10s %10s\n", "Item", "Qty", "Price"));
                receipt.append("===================================================================================\n");
        
                for (ConsolidatedOrder item : order.getConsolidatedView()) {
                    receipt.append(String.format("%-60s %10d %10.2f\n",
                            item.getItem().getName(),
                            item.getQuantity(),
                            item.getItem().getPrice() * item.getQuantity()));
                }
        
                receipt.append("===================================================================================\n");
                receipt.append(String.format("%-60s %21.2f\n", "Total", order.getTotal()));
                receipt.append("===================================================================================\n");
        
                writer.write(receipt.toString());
                showAlert("Receipt saved successfully.");

                if (onReceiptGeneratedCallback != null) {
                    onReceiptGeneratedCallback.run();
                }
            } catch (IOException e) {
                showAlert("Failed to save receipt: " + e.getMessage());
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Receipt Generation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.initStyle(StageStyle.UNDECORATED);
        Stage primaryStage = (Stage) receiptContainer.getScene().getWindow();
        alert.initOwner(primaryStage);

        if (!alert.isShowing()) {
            alert.showAndWait();

            double centerX = primaryStage.getX() + (primaryStage.getWidth() - alert.getWidth()) / 2;
            double centerY = primaryStage.getY() + (primaryStage.getHeight() - alert.getHeight()) / 2;
            alert.setX(centerX);
            alert.setY(centerY);
        }
    }
}