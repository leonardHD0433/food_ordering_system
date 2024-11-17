package software_design.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import software_design.model.MenuItem;

public class OrderView {
    
    public static VBox createOrderItemView(MenuItem item, int quantity, String option, String remarks, String status) {
        VBox itemBox = new VBox(5);
        itemBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 10;"
        );
        
        // Item name and price
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        Label priceLabel = new Label(String.format("Price: RM%.2f", item.getPrice()));
        
        // Quantity and status with subtotal
        HBox detailsBox = new HBox(10);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        
        Label quantityLabel = new Label("Quantity: " + quantity);
        Label statusLabel = new Label("Status: " + status);
        Label subtotalLabel = new Label(String.format("RM%.2f", item.getPrice() * quantity));
        
        statusLabel.setStyle("-fx-font-weight: bold;");
        subtotalLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 80px;");
        subtotalLabel.setAlignment(Pos.CENTER_RIGHT);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        detailsBox.getChildren().addAll(quantityLabel, statusLabel, spacer, subtotalLabel);
        
        itemBox.getChildren().addAll(nameLabel, priceLabel, detailsBox);
        
        if (!option.equals("null")) {
            Label optionLabel = new Label("Option: " + option);
            itemBox.getChildren().add(optionLabel);
        }
        
        Label remarksLabel = new Label("Remarks: " + (remarks != null && !remarks.isEmpty() ? remarks : "None"));
        remarksLabel.setWrapText(true);
        
        itemBox.getChildren().add(remarksLabel);
        
        return itemBox;
    }
}
