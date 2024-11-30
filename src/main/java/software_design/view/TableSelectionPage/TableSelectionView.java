package software_design.view.TableSelectionPage;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

// TableSelectionView.java
public class TableSelectionView {
    private final FadeTransition fadeTransition;
    private final Label alert;
    private final Button selectTableButton;
    private final TextField tableNumberInput;
    private final Button submitButton;

    public TableSelectionView(Label alert, Button selectTableButton, 
                            TextField tableNumberInput, Button submitButton) {
        this.alert = alert;
        this.selectTableButton = selectTableButton;
        this.tableNumberInput = tableNumberInput;
        this.submitButton = submitButton;
        this.fadeTransition = new FadeTransition(Duration.seconds(3), alert);
    }

    public void showTableInputControls() {
        selectTableButton.setVisible(false);
        tableNumberInput.setVisible(true);
        submitButton.setVisible(true);
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
