package software_design.view.OrderProcessingPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import software_design.model.MenuItem;

public class ProcessOrderView {

    public static VBox createOrderProcessingItemView(
        MenuItem item,
        int quantity,
        String option,
        String remarks,
        int tableId,
        int itemIndex,
        EventHandler<ActionEvent> completeHandler
    ) {
        VBox itemBox = new VBox(5);
        itemBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 10;"
        );
    
        Label tableLabel = new Label("Table: " + tableId);
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label optionLabel = new Label("Option: " + option);
        Label remarksLabel = new Label("Remarks: " + (remarks != null && !remarks.isEmpty() ? remarks : "None"));
    
        Button completeButton = new Button("Complete");
        completeButton.setOnAction(completeHandler);
    
        Label quantityLabel = new Label("Quantity: " + quantity);
        itemBox.getChildren().addAll(tableLabel, nameLabel, quantityLabel, optionLabel, remarksLabel, completeButton);
    
        return itemBox;
    }

    public static VBox createNoOrdersLabel() {
        Label noOrdersLabel = new Label("No incoming orders.");
        noOrdersLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #808080;");
        
        VBox container = new VBox(noOrdersLabel);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-padding: 20;"); // Optional: Add padding for better spacing
        
        return container;
    }
}