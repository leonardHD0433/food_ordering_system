package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import software_design.model.MenuItem;
import software_design.App;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javafx.scene.control.Alert.AlertType;
import software_design.database.Database;

public class ItemDetailsAdminController {

    @FXML private ImageView itemImage;
    @FXML private TextField itemNameField;
    @FXML private TextArea itemDescArea;
    @FXML private TextField itemPriceField;
    @FXML private TextArea itemOptionsArea;
    @FXML private CheckBox availabilityCheckBox;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private MenuItem currentItem;
    private boolean isEditing = false;

    @FXML
    public void initialize() {
        // Retrieve the selected MenuItem (assuming you have a method to do so)
        currentItem = ItemEditingController.getSelectedMenuItem();
        if (currentItem != null) {
            displayItemDetails(currentItem);
        }
    }

    private void displayItemDetails(MenuItem item) {
        // Set item details
        itemNameField.setText(item.getName());
        itemDescArea.setText(item.getDescription());
        itemPriceField.setText(String.valueOf(item.getPrice()));
        itemOptionsArea.setText(item.getOptions());
        availabilityCheckBox.setSelected(item.isAvailable());

        // Load item image
        if (item.getImage() != null && item.getImage().length > 0) {
            Image image = new Image(new ByteArrayInputStream(item.getImage()));
            itemImage.setImage(image);
        } else {
            itemImage.setStyle("-fx-background-color: #666666;");
        }
    }

    @FXML
    private void handleEdit() {
        if (!isEditing) {
            // Enable fields for editing
            itemNameField.setEditable(true);
            itemDescArea.setEditable(true);
            itemPriceField.setEditable(true);
            itemOptionsArea.setEditable(true);
            availabilityCheckBox.setDisable(false);
            isEditing = true;

            // Change edit button icon to save icon
            try {
                Image saveIcon = new Image(getClass().getResourceAsStream("/software_design/images/save.png"));
                if (saveIcon.isError()) {
                    System.err.println("Error loading save icon");
                    return;
                }
                ImageView saveIconView = new ImageView(saveIcon);
                saveIconView.setFitWidth(30);  // Set width to 20px
                saveIconView.setFitHeight(30); // Set height to 20px
                saveIconView.setPreserveRatio(true);
                editButton.setGraphic(saveIconView);
            } catch (Exception e) {
                System.err.println("Could not load save icon: " + e.getMessage());
            }
        } else {
            if (itemNameField.getText().trim().isEmpty()) {
                showError("Item name cannot be empty");
                return;
            }
            if (itemDescArea.getText().trim().isEmpty()) {
                showError("Item description cannot be empty");
                return;
            }
            if (itemPriceField.getText().trim().isEmpty() || !itemPriceField.getText().matches("^\\d+(\\.\\d+)?$") || 
            Double.parseDouble(itemPriceField.getText()) == 0) {
                showError("Item price cannot be empty, must be a number, and must be greater than 0");
                return;
            }

            try {
                // Save the changes
                currentItem.setName(itemNameField.getText());
                currentItem.setDescription(itemDescArea.getText());
                currentItem.setPrice(Double.parseDouble(itemPriceField.getText()));
                currentItem.setOptions(itemOptionsArea.getText());
                currentItem.setAvailable(availabilityCheckBox.isSelected());
    
                // Update the item in the database
                Database.updateMenuItem(currentItem.getCategory(), 
                                     currentItem.getName(), 
                                     currentItem.getDescription(), 
                                     currentItem.getOptions(), 
                                     currentItem.getPrice(), 
                                     currentItem.isAvailable(), 
                                     currentItem.getImage(), 
                                     currentItem.getId());
    
                // Disable editing
                itemNameField.setEditable(false);
                itemDescArea.setEditable(false);
                itemPriceField.setEditable(false);
                itemOptionsArea.setEditable(false);
                availabilityCheckBox.setDisable(true);
                isEditing = false;
    
                // Change icon back to edit icon
                editButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/software_design/images/edit.png"))));
    
            } catch (SQLException e) {
                // Show error alert
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to update menu item: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDelete() {
        // Prompt user for confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this item?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Delete the item from the database
                    Database.removeMenuITem(currentItem.getId());
                    // Navigate back to the admin menu
                    App.setRoot("menu_admin");
                } catch (SQLException e) {
                    // Show error alert
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete menu item: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleClose() {
        App.setRoot("menu_admin");
    }
}