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
    
    // Add class-level fields to track available space
    private double cellWidth;
    private double cellHeight;

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
        
        // Calculate cell size based on available space
        calculateCellSize(cols, rows);
        
        // Listen for grid size changes and recalculate
        tableMapGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            calculateCellSize(cols, rows);
            updateTableSizes();
        });
        
        tableMapGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            calculateCellSize(cols, rows);
            updateTableSizes();
        });
        
        for (int i = 0; i < tables.length; i++) {
            Table table = tables[i];
            int tableId = table.getTableId();
            
            // Create a visual table representation
            StackPane tablePane = createTableVisual(tableId, table);
            
            int col = i % cols;
            int row = i / cols;
            
            tableMapGrid.add(tablePane, col, row);
        }
    }

    private void calculateCellSize(int cols, int rows) {
        // Account for gaps between cells
        double gapSpace = (cols - 1) * tableMapGrid.getHgap() + (rows - 1) * tableMapGrid.getVgap();
        double paddingSpace = 40; // Account for padding
        
        // Calculate available width and height for each cell
        cellWidth = (tableMapGrid.getWidth() - gapSpace - paddingSpace) / cols;
        cellHeight = (tableMapGrid.getHeight() - gapSpace - paddingSpace) / rows;
        
        // Ensure minimum size
        cellWidth = Math.max(cellWidth, 50);
        cellHeight = Math.max(cellHeight, 50);
    }
    
    private void updateTableSizes() {
        for (javafx.scene.Node node : tableMapGrid.getChildren()) {
            if (node instanceof StackPane) {
                StackPane tablePane = (StackPane) node;
                for (javafx.scene.Node child : tablePane.getChildren()) {
                    if (child instanceof Rectangle) {
                        Rectangle rect = (Rectangle) child;
                        // Resize rectangle
                        rect.setWidth(cellWidth * 0.8);
                        rect.setHeight(cellHeight * 0.8);
                    } else if (child instanceof VBox) {
                        VBox tableInfo = (VBox) child;
                        // Update text sizes
                        for (javafx.scene.Node textNode : tableInfo.getChildren()) {
                            if (textNode instanceof Label) {
                                Label label = (Label) textNode;
                                if (label.getText().startsWith("Table")) {
                                    double fontSize = Math.max(10, cellWidth * 0.18);
                                    label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px;");
                                } else {
                                    double fontSize = Math.max(8, cellWidth * 0.12);
                                    label.setStyle("-fx-font-size: " + fontSize + "px;");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private StackPane createTableVisual(int tableId, Table table) {
        // Create table visual with dynamic size
        Rectangle tableRect = new Rectangle(cellWidth * 0.8, cellHeight * 0.8);
        tableRect.setArcWidth(15);
        tableRect.setArcHeight(15);
        
        boolean hasOrder = table.getOrder() != null;
        tableRect.setFill(hasOrder ? Color.LIGHTGREEN : Color.LIGHTGRAY);
        tableRect.setStroke(Color.DARKGRAY);
        tableRect.setStrokeWidth(2);
        
        double tableFontSize = Math.max(10, cellWidth * 0.16);
        double statusFontSize = Math.max(8, cellWidth * 0.10);
        
        Label tableNumberLabel = new Label("Table " + tableId);
        tableNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: " + tableFontSize + "px;");
        
        Label statusLabel = new Label(hasOrder ? "In Use" : "Available");
        statusLabel.setStyle("-fx-font-size: " + statusFontSize + "px;");
        
        VBox tableInfo = new VBox(5, tableNumberLabel, statusLabel);
        tableInfo.setAlignment(Pos.CENTER);
        
        StackPane tablePane = new StackPane(tableRect, tableInfo);
        tablePane.setStyle("-fx-padding: 10;");
        
        tablePane.setOnMouseClicked(e -> {
            if (onTableSelected != null) {
                onTableSelected.accept(tableId);
            }
        });
        
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