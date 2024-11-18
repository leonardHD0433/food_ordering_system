package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import software_design.App;
import software_design.model.TableManager;
import software_design.view.TableSelectionView;

// TableSelectionController.java
public class TableSelectionController {
    @FXML private Button selectTableButton;
    @FXML private TextField tableNumberInput;
    @FXML private Button submitButton;
    @FXML private Label alert;

    private TableSelectionView view;
    private TableManager tableManager;

    @FXML
    private void initialize() {
        view = new TableSelectionView(alert, selectTableButton, 
                                    tableNumberInput, submitButton);
        setupInputValidation();
    }

    private void setupInputValidation() {
        tableNumberInput.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tableNumberInput.setText(oldValue);
            }
        });
    }

    @FXML
    private void handleSwitchAdmin() {
        App.setRoot("admin_homepage");
    }

    @FXML
    private void handleTableSelection() {
        view.showTableInputControls();
    }

    @FXML
    private void handleSubmit() {
        alert.setText("");
        alert.setVisible(false);

        try {
            validateAndProcessTableNumber();
        } catch (NumberFormatException e) {
            view.showErrorWithFade("Please enter a valid number");
            tableNumberInput.setText("");
        }
    }

    private void validateAndProcessTableNumber() {
        int value = Integer.parseInt(tableNumberInput.getText());
        if (value < 1 || value > 20) {
            view.showErrorWithFade("Please enter a table number between 1 and 20");
            tableNumberInput.setText("");
            tableNumberInput.requestFocus();
            return;
        }

        tableManager = TableManager.getInstance();
        tableManager.getTable(value);
        App.setRoot("menu");
    }
}