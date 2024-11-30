package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import software_design.App;
import software_design.view.AdminPage.AdminView;

public class AdminController {
    @FXML private Button modifyMenuButton;
    @FXML private Button processOrderButton;
    @FXML private Button generateReceiptButton;
    
    private AdminView view;
    
    @FXML
    private void initialize() {
        view = new AdminView(modifyMenuButton, processOrderButton, generateReceiptButton);
        view.styleButtons();
    }
    
    @FXML
    private void handleModifyMenu() {
        App.setRoot("MenuModificationPage/menu_modification");
    }
    
    @FXML
    private void handleProcessOrder() {
        App.setRoot("OrderProcessingPage/process_order");
    }
    
    @FXML
    private void handleGenerateReceipt() {
        App.setRoot("receipt_generation");
    }

    @FXML
    private void handleSwitchCustomer() {
        App.setRoot("TableSelectionPage/table_selection");
    }
}
