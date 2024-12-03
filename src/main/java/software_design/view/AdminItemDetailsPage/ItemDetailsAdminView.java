package software_design.view.AdminItemDetailsPage;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemDetailsAdminView {

    public void setSaveIcon(Button button) {
        try {
            Image saveIcon = new Image(ItemDetailsAdminView.class.getResourceAsStream("/software_design/images/save.png"));
            if (saveIcon.isError()) {
                System.err.println("Error loading save icon");
                return;
            }
            ImageView saveIconView = new ImageView(saveIcon);
            saveIconView.setFitWidth(30);  // Set width to 30px
            saveIconView.setFitHeight(30); // Set height to 30px
            saveIconView.setPreserveRatio(true);
            button.setGraphic(saveIconView);
        } catch (Exception e) {
            System.err.println("Could not load save icon: " + e.getMessage());
        }
    }

    public void setEditIcon(Button button) {
        try {
            Image editIcon = new Image(ItemDetailsAdminView.class.getResourceAsStream("/software_design/images/edit.png"));
            if (editIcon.isError()) {
                System.err.println("Error loading edit icon");
                return;
            }
            ImageView editIconView = new ImageView(editIcon);
            editIconView.setFitWidth(30);  // Set width to 30px
            editIconView.setFitHeight(30); // Set height to 30px
            editIconView.setPreserveRatio(true);
            button.setGraphic(editIconView);
        } catch (Exception e) {
            System.err.println("Could not load edit icon: " + e.getMessage());
        }
    }
}