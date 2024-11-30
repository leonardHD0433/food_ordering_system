package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import software_design.model.MenuItem;
import software_design.view.AdminItemDetailsPage.ItemDetailsAdminView;
import software_design.App;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
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
    @FXML private Button imageButton;
    @FXML private Label imageLabel;

    private MenuItem currentItem;
    private boolean isEditing = false;
    private byte[] selectedImageData;

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
            selectedImageData = item.getImage();
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
            imageButton.setVisible(true);
            isEditing = true;

            // Change edit button icon to save icon
            ItemDetailsAdminView.setSaveIcon(editButton);
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

            if (selectedImageData == null) {
                showError("Item image cannot be empty");
                return;
            }

            try {
                // Save the changes
                currentItem.setName(itemNameField.getText());
                currentItem.setDescription(itemDescArea.getText());
                currentItem.setPrice(Double.parseDouble(itemPriceField.getText()));
                currentItem.setOptions(itemOptionsArea.getText());
                currentItem.setAvailable(availabilityCheckBox.isSelected());
                currentItem.setImage(selectedImageData);
    
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
                imageButton.setVisible(false);
                isEditing = false;
    
                // Change icon back to edit icon
                ItemDetailsAdminView.setEditIcon(editButton);
    
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

    @FXML
    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Menu Item Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(App.getScene().getWindow());
        if (file != null) {
            try {
                // Check file size (999KB limit)
                if (file.length() > 999 * 1024) {
                    showError("Image size must be less than 999KB");
                    return;
                }

                // Load and preview image
                Image image = new Image(file.toURI().toString());

                // Check if image width and height are the same
                if (image.getWidth() != image.getHeight()) {
                    showError("Image width and height must be the same");
                    return;
                }

                itemImage.setImage(image);
                imageLabel.setText(file.getName());
  
                // Store image data
                selectedImageData = Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                showError("Error loading image: " + e.getMessage());
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
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(App.getScene().getWindow());

        // Position alert
        Window mainWindow = App.getScene().getWindow();
        alert.setX(mainWindow.getX() + (mainWindow.getWidth() - alert.getWidth()) / 2);
        alert.setY(mainWindow.getY() + (mainWindow.getHeight() - alert.getHeight()) / 2);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Delete the item from the database
                    Database.removeMenuITem(currentItem.getId());
                    // Navigate back to the admin menu
                    App.setRoot("AdminMenuPage/menu_admin");
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
        App.setRoot("AdminMenuPage/menu_admin");
    }
}