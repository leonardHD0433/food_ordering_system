package software_design.view.TableSelectionPage;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import software_design.model.Table;
import software_design.model.TableManager;

import java.util.function.Consumer;

public class TableSelectionView {
    private final FadeTransition fadeTransition;
    private final Label alert;
    private GridPane tableMapGrid;
    private Consumer<Integer> onTableSelected;

    public TableSelectionView(Label alert, GridPane tableMapGrid) {
        this.alert = alert;
        this.tableMapGrid = tableMapGrid;
        this.fadeTransition = new FadeTransition(Duration.seconds(3), alert);
    }

    public void createTableMap(Consumer<Integer> onTableSelected) {
        this.onTableSelected = onTableSelected;
        tableMapGrid.getChildren().clear();
        
        TableManager tableManager = TableManager.getInstance();
        Table[] tables = tableManager.getAllTables();
        
        // Arrange tables in a 5x4 grid layout
        int cols = 5;
        int rows = 4;
        
        for (int i = 0; i < tables.length; i++) {
            Table table = tables[i];
            int tableId = table.getTableId();
            
            // Create a visual table representation
            StackPane tablePane = createTableVisual(tableId, table);
            
            // Add to grid - calculate row and column
            int col = i % cols;
            int row = i / cols;
            
            tableMapGrid.add(tablePane, col, row);
        }
    }

    private StackPane createTableVisual(int tableId, Table table) {
        // Create table visual
        Rectangle tableRect = new Rectangle(80, 80);
        tableRect.setArcWidth(15);
        tableRect.setArcHeight(15);
        
        // Style based on table status (e.g., if order exists)
        boolean hasOrder = table.getOrder() != null;
        tableRect.setFill(hasOrder ? Color.LIGHTGREEN : Color.LIGHTGRAY);
        tableRect.setStroke(Color.DARKGRAY);
        tableRect.setStrokeWidth(2);
        
        // Add table number
        Label tableNumberLabel = new Label("Table " + tableId);
        tableNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Status indicator if needed
        Label statusLabel = new Label(hasOrder ? "In Use" : "Available");
        statusLabel.setStyle("-fx-font-size: 10px;");
        
        VBox tableInfo = new VBox(5, tableNumberLabel, statusLabel);
        tableInfo.setAlignment(Pos.CENTER);
        
        // Combine into a stack pane
        StackPane tablePane = new StackPane(tableRect, tableInfo);
        tablePane.setStyle("-fx-padding: 10;");
        
        // Add click handler
        tablePane.setOnMouseClicked(e -> {
            if (onTableSelected != null) {
                onTableSelected.accept(tableId);
            }
        });
        
        // Add hover effect
        tablePane.setOnMouseEntered(e -> {
            tableRect.setStroke(Color.BLUE);
            tableRect.setStrokeWidth(3);
            tablePane.setStyle("-fx-cursor: hand; -fx-padding: 10;");
        });
        
        tablePane.setOnMouseExited(e -> {
            tableRect.setStroke(Color.DARKGRAY);
            tableRect.setStrokeWidth(2);
            tablePane.setStyle("-fx-padding: 10;");
        });
        
        return tablePane;
    }

    public void showErrorWithFade(String message) {
        if (fadeTransition != null) {
            fadeTransition.stop();
        }
        
        alert.setOpacity(1.0);
        alert.setText(message);
        alert.setVisible(true);
        
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> alert.setVisible(false));
        fadeTransition.play();
    }
}