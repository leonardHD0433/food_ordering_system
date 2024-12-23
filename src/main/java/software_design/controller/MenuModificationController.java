package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import software_design.App;
import software_design.view.MenuModificationPage.MenuModificationView;

public class MenuModificationController {
    @FXML private Button backButton;
    @FXML private Button addItemButton;
    @FXML private Button editItemButton;
    
    private MenuModificationView view;
    
    @FXML
    private void initialize() {
        view = new MenuModificationView(backButton, addItemButton, editItemButton);
        view.styleButtons();
    }
    
    @FXML
    private void handleBack() {
        App.setRoot("AdminPage/admin_homepage");
    }
    
    @FXML
    private void handleAddItem() {
        App.setRoot("AddItemPage/item_addition");
    }
    
    @FXML
    private void handleEditItem() {
        App.setRoot("AdminMenuPage/menu_admin");
    }
}