package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import software_design.App;
import software_design.model.Menu;
import software_design.model.MenuItem;
import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.util.List;

public class ItemAdditionController {
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField priceField;
    @FXML private TextField optionsField;
    @FXML private Button imageButton;
    @FXML private ImageView imagePreview;
    @FXML private Label imageLabel;
    
    private byte[] selectedImageData;
    private Menu menu;

    @FXML
    private void initialize() {
        menu = new Menu();
        try {
            List<String>category = menu.getDistinctCategories();
            category.remove("All");
            categoryComboBox.getItems().addAll(category);
        } catch (SQLException e) {
            showError("Error loading categories: " + e.getMessage());
        }
    }

    @FXML
    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Menu Item Image");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("Image Files", "*.jpg", "*.jpeg")
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

                imagePreview.setImage(image);
                imageLabel.setText(file.getName());
  
                // Store image data
                selectedImageData = Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                showError("Error loading image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            MenuItem newItem = new MenuItem(
                0, // ID will be auto-generated by database
                categoryComboBox.getValue(),
                nameField.getText(),
                descriptionArea.getText(),
                optionsField.getText(),
                Double.parseDouble(priceField.getText()),
                true,
                selectedImageData
            );

            menu.createItem(newItem);
            showSuccess("Item added successfully!");
            App.setRoot("MenuModificationPage/menu_modification");
        } catch (Exception e) {
            showError("Error saving item: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        App.setRoot("MenuModificationPage/menu_modification");
    }

    private boolean validateInputs() {
        //Validate category
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().trim().isEmpty()) {
            showError("Please select or enter a category");
            return false;
        }

        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            showError("Please enter item name");
            return false;
        }

        // Validate description
        if (descriptionArea.getText().trim().isEmpty()) {
            showError("Please enter item description");
            return false;
        }

        // Validate price
        try {
            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                showError("Price must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid price");
            return false;
        }

        // Validate Image
        if (selectedImageData == null) {
            showError("Please select an image");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage mainWindow = (Stage) App.getScene().getWindow();
        alert.initOwner(mainWindow);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage mainWindow = (Stage) App.getScene().getWindow();
        alert.initOwner(mainWindow);
        alert.showAndWait();
    }
}