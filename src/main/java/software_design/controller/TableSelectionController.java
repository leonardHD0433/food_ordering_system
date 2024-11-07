package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TableSelectionController 
{
    @FXML
    private Button selectTableButton;
    
    @FXML
    private TextField tableNumberInput;
    
    @FXML
    private Button submitButton;

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
        try 
        {
            int value = Integer.parseInt(tableNumberInput.getText());
            if (value < 1 || value > 20) 
            {
                System.out.println("Please enter a table number between 1 and 20");
                tableNumberInput.setText("");
                return;
            }
            System.out.println("Table " + value + " selected");
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("Please enter a valid number");
            tableNumberInput.setText("");
        }
    }
}