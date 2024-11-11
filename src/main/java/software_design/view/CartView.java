package software_design.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

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
}
