package software_design.view.AddItemPage;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class ItemAdditionView {
    private final static String BUTTON_STYLE = 
        "-fx-background-color: #808080;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 5px;" +
        "-fx-min-width: 150;" +
        "-fx-min-height: 40;" + 
        "-fx-font-size: 16px;";

    private final static String BUTTON_HOVER_STYLE = BUTTON_STYLE +
        "-fx-background-color: #666666;" +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);";

    public static void styleControls(Button saveButton, Button cancelButton, 
                                   Button imageButton, ComboBox<?> categoryBox,
                                   TextField nameField, TextArea descArea,
                                   TextField priceField, TextField optionsField) {
        styleButton(saveButton);
        styleButton(cancelButton);
        styleButton(imageButton);
        
        String inputStyle = "-fx-font-size: 14px; -fx-padding: 5;";
        categoryBox.setStyle(inputStyle);
        nameField.setStyle(inputStyle);
        descArea.setStyle(inputStyle);
        priceField.setStyle(inputStyle);
        optionsField.setStyle(inputStyle);
    }

    private static void styleButton(Button button) {
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(e -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));
    }
}