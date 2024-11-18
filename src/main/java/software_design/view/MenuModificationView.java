package software_design.view;

import javafx.scene.control.Button;

public class MenuModificationView {
    private final Button backButton;
    private final Button addItemButton;
    private final Button editItemButton;
    
    public MenuModificationView(Button backButton, Button addItemButton, Button editItemButton) {
        this.backButton = backButton;
        this.addItemButton = addItemButton;
        this.editItemButton = editItemButton;
    }
    
    public void styleButtons() {
        String mainButtonStyle = 
            "-fx-background-color: #808080;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5px;" +
            "-fx-min-width: 300;" +
            "-fx-min-height: 70;" + 
            "-fx-font-size: 24px;";
            
        String mainHoverStyle = mainButtonStyle +
            "-fx-background-color: #666666;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);";
        
        styleMainButton(addItemButton, mainButtonStyle, mainHoverStyle);
        styleMainButton(editItemButton, mainButtonStyle, mainHoverStyle);
        styleMainButton(backButton, mainButtonStyle, mainHoverStyle);
    }
    
    private void styleMainButton(Button button, String defaultStyle, String hoverStyle) {
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }
}