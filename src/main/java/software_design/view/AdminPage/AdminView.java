package software_design.view.AdminPage;

import javafx.scene.control.Button;
import software_design.command.ModifyMenuCommand;
import software_design.command.ProcessOrderCommand;
import software_design.command.GenerateReceiptCommand;

public class AdminView {
    private Button modifyMenuButton;
    private Button processOrderButton;
    private Button generateReceiptButton;
    
    public AdminView(Button modifyMenuButton, Button processOrderButton, Button generateReceiptButton, 
                     ModifyMenuCommand modifyMenuCommand, ProcessOrderCommand processOrderCommand, 
                     GenerateReceiptCommand generateReceiptCommand) 
    {
        this.modifyMenuButton = modifyMenuButton;
        this.processOrderButton = processOrderButton;
        this.generateReceiptButton = generateReceiptButton;

        modifyMenuButton.setOnAction(e -> modifyMenuCommand.execute());
        processOrderButton.setOnAction(e -> processOrderCommand.execute());
        generateReceiptButton.setOnAction(e -> generateReceiptCommand.execute());
    }
    
    public void styleButtons() {
        String buttonStyle = 
            "-fx-background-color: #808080;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5px;" +
            "-fx-min-width: 300;" +
            "-fx-min-height: 70;" + 
            "-fx-font-size: 24px;";
            
        String hoverStyle = buttonStyle +
            "-fx-background-color: #666666;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);";
            
        styleButton(modifyMenuButton, buttonStyle, hoverStyle);
        styleButton(processOrderButton, buttonStyle, hoverStyle);
        styleButton(generateReceiptButton, buttonStyle, hoverStyle);
    }
    
    private void styleButton(Button button, String defaultStyle, String hoverStyle) {
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }
}
