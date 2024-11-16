package software_design.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import software_design.model.MenuItem;

public class CartView {
    
    public static void showEmptyCart(VBox container) {
        container.getChildren().clear();
        Label emptyLabel = new Label("WOW! SUCH EMPTY...");
        emptyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #808080;");
        
        VBox centerBox = new VBox(emptyLabel);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setStyle("-fx-padding: 50 0;");
        
        container.getChildren().add(centerBox);
    }
    
    public static void hideBottomControls(VBox bottomControls) {
        bottomControls.setVisible(false);
        bottomControls.setManaged(false);
    }
    
    public static void showBottomControls(VBox bottomControls) {
        bottomControls.setVisible(true);
        bottomControls.setManaged(true);
    }

    public static VBox createCartItemView(MenuItem item, int quantity, String option, String remarks) {
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
        
        Label priceLabel = new Label(String.format("RM%.2f", item.getPrice() * quantity));
        
        // Quantity and option
        Label quantityLabel = new Label("Quantity: " + quantity);

        itemBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel);

        if (!option.equals("null")) 
        {
            Label optionLabel = new Label("Option: " + option);
            itemBox.getChildren().add(optionLabel);
        }
        
        // Remarks if any
        Label remarksLabel = new Label("Remarks: " + (remarks != null && !remarks.isEmpty() ? remarks : "None"));
        remarksLabel.setWrapText(true);
        
        itemBox.getChildren().add(remarksLabel);
        
        return itemBox;
    }
}
