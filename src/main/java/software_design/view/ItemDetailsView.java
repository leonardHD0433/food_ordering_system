package software_design.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import software_design.model.MenuItem;
import java.io.ByteArrayInputStream;

public class ItemDetailsView {
    private final Label tableNumberLabel;
    private final ImageView itemImage;
    private final Label itemNameLabel;
    private final Label itemDescLabel;
    private final Spinner<Integer> quantitySpinner;
    private final SpinnerValueFactory<Integer> quantityFactory;
    private final VBox optionsBox;
    private final TextArea remarksArea;
    private ToggleGroup optionsGroup;
    private ScrollPane scrollPane;

    public ItemDetailsView(Label tableNumberLabel, ImageView itemImage, 
                      Label itemNameLabel, Label itemDescLabel,
                      Spinner<Integer> quantitySpinner, VBox optionsBox,
                      TextArea remarksArea, ScrollPane scrollPane) {
        this.tableNumberLabel = tableNumberLabel;
        this.itemImage = itemImage;
        this.itemNameLabel = itemNameLabel;
        this.itemDescLabel = itemDescLabel;
        this.quantityFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        this.quantitySpinner = quantitySpinner;
        this.quantitySpinner.setValueFactory(quantityFactory);
        this.optionsBox = optionsBox;
        this.remarksArea = remarksArea;
        this.scrollPane = scrollPane;
        styleScrollPane(this.scrollPane);
    }

    public void setTableNumber(int tableNumber) {
        tableNumberLabel.setText("Table: " + tableNumber);
    }

    public void displayItem(MenuItem item) {
        // Clear previous image
        itemImage.setImage(null);
        
        // Set new image with error handling
        if (item.getImage() != null && item.getImage().length > 0) {
            try {
                Image image = new Image(new ByteArrayInputStream(item.getImage()));
                if (!image.isError()) {
                    itemImage.setImage(image);
                } else {
                    setDefaultImage();
                }
            } catch (Exception e) {
                setDefaultImage();
            }
        } else {
            setDefaultImage();
        }
        
        itemNameLabel.setText(item.getName());
        itemDescLabel.setText(item.getDescription());
        setupOptions(item.getOptions());
    }
    
    private void setDefaultImage() {
        // Set a default placeholder image or style
        itemImage.setStyle("-fx-background-color: #666666;");
    }

    private void setupOptions(String options) {
        optionsBox.getChildren().clear();
        if (options != null && !options.isEmpty()) {
            options = options.replace("\"", "");
            String[] parts = options.split(",");
            if (parts.length > 1) {
                Label optionHeader = new Label(parts[0]);
                optionHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                
                HBox headerBox = new HBox(optionHeader);
                headerBox.setAlignment(Pos.CENTER_LEFT);

                optionsGroup = new ToggleGroup();
                VBox radioBox = new VBox(5);
                
                for (int i = 1; i < parts.length; i++) {
                    RadioButton rb = new RadioButton(parts[i].trim());
                    rb.setToggleGroup(optionsGroup);
                    if (i == 1) rb.setSelected(true);
                    radioBox.getChildren().add(rb);
                }
                
                optionsBox.getChildren().addAll(headerBox, radioBox);
            }
        }
    }

    public String getSelectedOption() {
        if (optionsGroup == null) {
            return "null";
        }
        
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected == null) {
            return "must select";
        }
        
        return selected.getText();
    }

    public int getQuantity() {
        return quantitySpinner.getValue();
    }

    public String getRemarks() {
        return remarksArea.getText();
    }

    public void reset() {
        optionsBox.getChildren().clear();
        remarksArea.clear();
        quantitySpinner.getValueFactory().setValue(1);
    }

    private void styleScrollPane(ScrollPane scrollPane) {
        scrollPane.getStyleClass().add("styled-scroll-pane");
        
        javafx.application.Platform.runLater(() -> {
            String scrollBarStyle = 
                ".styled-scroll-pane .scroll-bar:vertical {" +
                "    -fx-background-color: #f0f0f0;" +
                "    -fx-pref-width: 16px;" +
                "}" +
                ".styled-scroll-pane .scroll-bar .thumb {" +
                "    -fx-background-color: #808080;" +
                "    -fx-background-radius: 8px;" +
                "}" +
                ".styled-scroll-pane .scroll-bar .track {" +
                "    -fx-background-color: #e0e0e0;" +
                "    -fx-background-radius: 8px;" +
                "}";

            scrollPane.getScene().getStylesheets().add(
                "data:text/css," + scrollBarStyle.replace(" ", "%20")
            );
            
            scrollPane.setStyle(
                "-fx-background: #f0f0f0;" +
                "-fx-border-color: #cccccc;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 2;"
            );
        });
    }
}