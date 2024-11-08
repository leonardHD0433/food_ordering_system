package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import software_design.App;
import software_design.model.TableManager;

public class TableSelectionController 
{
    @FXML
    private Button selectTableButton;
    
    @FXML
    private TextField tableNumberInput;
    
    @FXML
    private Button submitButton;

    @FXML
    private Label alert;

    private FadeTransition fadeTransition;
    private TableManager tableManager;

    @FXML
    private void initialize() 
    {
        // Add listener to validate
        tableNumberInput.textProperty().addListener((obs, oldValue, newValue) -> 
        {
            if (!newValue.matches("\\d*")) 
            {
                tableNumberInput.setText(oldValue);
            }
        });
    }

    @FXML
    private void handleTableSelection() 
    {
        // Hide the select table button
        selectTableButton.setVisible(false);
        
        // Show the input field and submit button
        tableNumberInput.setVisible(true);
        submitButton.setVisible(true);
    }

    @FXML
    private void handleSubmit() 
    {
        // Clear any previous error
        alert.setText("");
        alert.setVisible(false);

        try 
        {
            int value = Integer.parseInt(tableNumberInput.getText());
            if (value < 1 || value > 20) 
            {
                showErrorWithFade("Please enter a table number between 1 and 20");
                System.out.println("Please enter a table number between 1 and 20");
                tableNumberInput.setText("");
                tableNumberInput.requestFocus();
                return;
            }
            System.out.println("Table " + value + " selected");

            // Get the TableManager instance
            tableManager = TableManager.getInstance();
            tableManager.getTable(value);

            // Direct to the menu page
            App.setRoot("menu");

        } 
        catch (NumberFormatException e) 
        {
            showErrorWithFade("Please enter a valid number");
            System.out.println("Please enter a valid number");
            tableNumberInput.setText("");
        }
    }

    private void showErrorWithFade(String message) 
    {
        // If there's an existing transition running, stop it
        if (fadeTransition != null) 
        {
            fadeTransition.stop();
        }
        
        alert.setOpacity(1.0); // Reset opacity
        alert.setText(message);
        alert.setVisible(true);
        
        fadeTransition = new FadeTransition(Duration.seconds(3), alert);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> alert.setVisible(false));
        fadeTransition.play();
    }
}