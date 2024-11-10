package software_design.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.sql.*;
import java.io.ByteArrayInputStream;
import software_design.database.Database;

public class TestImageController {
    @FXML
    private ImageView testImageView;

    @FXML
    private void initialize() {
        try {
            // Get first image from database
            String query = "SELECT ItemImage FROM menu_table LIMIT 1";
            try (Connection conn = Database.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("ItemImage");
                    if (imageData != null && imageData.length > 0) {
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        testImageView.setImage(image);
                    } else {
                        System.out.println("No image data found");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }
}
