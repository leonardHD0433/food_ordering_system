package software_design.view.CartPage;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import software_design.model.MenuItem;

public class CartView {
    
    public void showEmptyCart(VBox container) {
        container.getChildren().clear();
        Label emptyLabel = new Label("WOW! SUCH EMPTY...");
        emptyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #808080;");
        
        VBox centerBox = new VBox(emptyLabel);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setStyle("-fx-padding: 50 0;");
        
        container.getChildren().add(centerBox);
    }
    
    public void hideBottomControls(VBox bottomControls) {
        bottomControls.setVisible(false);
        bottomControls.setManaged(false);
    }
    
    public void showBottomControls(VBox bottomControls) {
        bottomControls.setVisible(true);
        bottomControls.setManaged(true);
    }

    public VBox createCartItemView(MenuItem item, int quantity, String option, String remarks, EventHandler<ActionEvent> minusHandler, EventHandler<ActionEvent> plusHandler) 
    {
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
        
        // Quantity controls with subtotal
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER_LEFT);
        
        Button minusBtn = new Button("-");
        Button plusBtn = new Button("+");
        Label quantityLabel = new Label(String.valueOf(quantity));
        Label subtotalLabel = new Label(String.format("RM%.2f", item.getPrice() * quantity));
        
        minusBtn.setStyle("-fx-min-width: 30px;");
        plusBtn.setStyle("-fx-min-width: 30px;");

        quantityLabel.setStyle("-fx-min-width: 30px; -fx-alignment: center;");
        subtotalLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 80px;");
        subtotalLabel.setAlignment(Pos.CENTER_RIGHT);

        minusBtn.setOnAction(minusHandler);
        plusBtn.setOnAction(plusHandler);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        quantityBox.getChildren().addAll(
            new Label("Quantity:"), minusBtn, quantityLabel, plusBtn, 
            spacer, subtotalLabel
        );

        itemBox.getChildren().addAll(nameLabel, priceLabel, quantityBox);

        if (!option.equals("null")) {
            Label optionLabel = new Label("Option: " + option);
            itemBox.getChildren().add(optionLabel);
        }
        
        Label remarksLabel = new Label("Remarks: " + (remarks != null && !remarks.isEmpty() ? remarks : "None"));
        remarksLabel.setWrapText(true);
        
        itemBox.getChildren().add(remarksLabel);
        
        return itemBox;
    }

    public void styleBackButton(Button backButton) {
        backButton.setStyle(
            "-fx-background-color: #E0E0E0;" +
            "-fx-border-color: #808080;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;"
        );
    
        backButton.setOnMouseEntered(e -> 
            backButton.setStyle(
                "-fx-background-color: #D0D0D0;" +
                "-fx-border-color: #808080;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);"
            )
        );
    
        backButton.setOnMouseExited(e -> 
            backButton.setStyle(
                "-fx-background-color: #E0E0E0;" +
                "-fx-border-color: #808080;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
            )
        );
    }
}
