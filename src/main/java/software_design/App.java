package software_design;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class App extends Application 
{

    private static Scene scene;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    @Override
    public void start(Stage stage) throws IOException 
    {
        scene = new Scene(loadFXML("table_selection"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml)
    {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML file: " + fxml, e);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException 
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) 
    {
        launch();
    }
}