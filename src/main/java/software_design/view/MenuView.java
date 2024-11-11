package software_design.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import software_design.model.MenuItem;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuView 
{
    private final GridPane menuGrid;
    private final Label tableNumberLabel;
    private final HBox filterContainer;
    private Consumer<MenuItem> onItemClick;
    
    public MenuView(GridPane menuGrid, Label tableNumberLabel, HBox filterContainer, Consumer<MenuItem> onItemClick) 
    {
        this.menuGrid = menuGrid;
        this.tableNumberLabel = tableNumberLabel;
        this.filterContainer = filterContainer;
        this.onItemClick = onItemClick;
    }

    public void setTableNumberLabel(int tableNumber) 
    {
        tableNumberLabel.setText("Table: " + tableNumber);
    }
    
    public void setupMenuGrid(List<MenuItem> menuItems) {
        menuGrid.getChildren().clear();
        menuGrid.getColumnConstraints().clear();
        
        // Set column constraints
        for (int i = 0; i < 2; i++) {
            javafx.scene.layout.ColumnConstraints column = new javafx.scene.layout.ColumnConstraints();
            column.setPercentWidth(50);
            menuGrid.getColumnConstraints().add(column);
        }
        
        // Create menu items
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            VBox menuItem = createMenuItem(item, this.onItemClick);
            menuGrid.add(menuItem, i % 2, i / 2);
        }
    }
    
    private VBox createMenuItem(MenuItem item, Consumer<MenuItem> onItemClick) {
        VBox menuItem = new VBox(10);
        styleMenuItem(menuItem);
        
        HBox imageContainer = new HBox();
        imageContainer.setAlignment(javafx.geometry.Pos.CENTER);
        ImageView imageView = createImageView(item);
        imageContainer.getChildren().add(imageView);
        
        Label itemLabel = createLabel(item.getName() + "\t " + String.format("RM%.2f", item.getPrice()), "19px");
        
        menuItem.getChildren().addAll(imageContainer, itemLabel);
        menuItem.setOnMouseClicked(e -> onItemClick.accept(item));
        return menuItem;
    }

    public void setupFilterButtons(List<String> categories, Consumer<String> onFilterClick) 
    {
        filterContainer.getChildren().clear();

        // Add padding to the filter container - increase bottom padding
        // If more spacing needed, can also add margin
        
        for (String category : categories) 
        {
            Button filterButton = new Button(category);
            
            // Set explicit button properties
            filterButton.setPrefWidth(200);
            filterButton.setPrefHeight(60);
            filterButton.setMinWidth(100);
            filterButton.setMinHeight(30);
            filterButton.setStyle(
                "-fx-background-color: #808080;\n" +
                "-fx-text-fill: white;\n" +
                "-fx-font-size: 20px;\n" +
                "-fx-font-weight: bold;"
            );

            // Add hover effect
            filterButton.setOnMouseEntered(e -> 
            filterButton.setStyle(filterButton.getStyle() + "-fx-background-color: #666666;")
            );
            
            filterButton.setOnMouseExited(e -> 
            filterButton.setStyle(filterButton.getStyle() + "-fx-background-color: #808080;")
            );

            filterButton.setOnAction(e -> onFilterClick.accept(category));
            filterContainer.getChildren().add(filterButton);
        }
    }

    private ImageView createImageView(MenuItem item) 
    {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);
        
        byte[] imageData = item.getImage();
        if (imageData != null && imageData.length > 0) 
        {
            try 
            {
                Image image = new Image(new ByteArrayInputStream(imageData));

                if (!image.isError()) 
                {
                    imageView.setImage(image);
                } 
                else 
                {
                    setDefaultImage(imageView);
                }
            } 
            catch (Exception e)
            {
                setDefaultImage(imageView);
            }
        } 
        else 
        {
            setDefaultImage(imageView);
        }
        return imageView;
    }

    private void setDefaultImage(ImageView imageView) 
    {
        // Set a default placeholder image
        imageView.setStyle("-fx-background-color: #666666;");
    }

    private Label createLabel(String text, String fontSize) 
    {
        Label label = new Label(text);
        label.setStyle( "-fx-text-fill: white;" + 
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: " + fontSize + ";");
        label.setWrapText(true);
        return label;
    }

    public void styleScrollPanes(ScrollPane filterScrollPane, ScrollPane menuScrollPane) 
    {
        // Set the style class for the ScrollPanes
        filterScrollPane.getStyleClass().add("styled-scroll-pane");
        menuScrollPane.getStyleClass().add("styled-scroll-pane");
        
        // Apply CSS through Platform.runLater to ensure nodes are available
        javafx.application.Platform.runLater(() -> {
            String scrollBarStyle = 
                ".styled-scroll-pane .scroll-bar:horizontal," +
                ".styled-scroll-pane .scroll-bar:vertical {" +
                "    -fx-background-color: #f0f0f0;" +
                "    -fx-pref-width: 16px;" +
                "    -fx-pref-height: 16px;" +
                "}" +
                ".styled-scroll-pane .scroll-bar .thumb {" +
                "    -fx-background-color: #808080;" +
                "    -fx-background-radius: 8px;" +
                "}" +
                ".styled-scroll-pane .scroll-bar .track {" +
                "    -fx-background-color: #e0e0e0;" +
                "    -fx-background-radius: 8px;" +
                "}";
    
            filterScrollPane.getScene().getStylesheets().clear();
            filterScrollPane.getScene().getStylesheets().add(
                "data:text/css," + scrollBarStyle.replace(" ", "%20")
            );
        });
        
        // Set base styles for ScrollPanes
        String scrollPaneStyle = 
            "-fx-background: #f0f0f0;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 1px;" +
            "-fx-padding: 2;";
            
        filterScrollPane.setStyle(scrollPaneStyle);
        menuScrollPane.setStyle(scrollPaneStyle);
    }

    private void styleMenuItem(VBox menuItem) {
        menuItem.getStyleClass().add("menu-item");
        menuItem.setStyle("-fx-background-color: #808080; -fx-padding: 15; -fx-background-radius: 5;");
        menuItem.setMinHeight(150);
        
        menuItem.setOnMouseEntered(e -> 
            menuItem.setStyle(menuItem.getStyle().replace("#808080", "#666666"))
        );
        menuItem.setOnMouseExited(e -> 
            menuItem.setStyle(menuItem.getStyle().replace("#666666", "#808080"))
        );
    }
}
