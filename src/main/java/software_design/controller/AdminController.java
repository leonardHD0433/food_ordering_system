package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import software_design.App;
import software_design.view.AdminPage.AdminView;
import software_design.command.ModifyMenuCommand;
import software_design.command.ProcessOrderCommand;
import software_design.command.GenerateReceiptCommand;

public class AdminController {
    @FXML private Button modifyMenuButton;
    @FXML private Button processOrderButton;
    @FXML private Button generateReceiptButton;

    private ModifyMenuCommand modifyMenuCommand;
    private ProcessOrderCommand processOrderCommand;
    private GenerateReceiptCommand generateReceiptCommand;
    
    @FXML
    private void initialize() {
        modifyMenuCommand = new ModifyMenuCommand(this);
        processOrderCommand = new ProcessOrderCommand(this);
        generateReceiptCommand = new GenerateReceiptCommand(this);

        AdminView view = new AdminView(modifyMenuButton, processOrderButton, generateReceiptButton, 
                             modifyMenuCommand, processOrderCommand, generateReceiptCommand);

        view.styleButtons();
    }
    
    public void handleModifyMenu() 
    {
        App.setRoot("MenuModificationPage/menu_modification");
    }

    public void handleProcessOrder() 
    {
        App.setRoot("OrderProcessingPage/process_order");
    }

    public void handleGenerateReceipt() 
    {
        App.setRoot("ReceiptGenerationPage/generate_receipt");
    }

    @FXML
    private void handleSwitchCustomer() {
        App.setRoot("TableSelectionPage/table_selection");
    }
}
