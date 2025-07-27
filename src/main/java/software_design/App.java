package software_design;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import software_design.database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Stage primaryStage;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        //Customer View
        primaryStage = stage; // Store reference to primary stage
        scene = new Scene(loadFXML("TableSelectionPage/table_selection"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Restaurant Ordering System");
        stage.show();
    }

    public static double getWindowX() {
        return primaryStage.getX(); // Get current X position
    }

    public static double getWindowY() {
        return primaryStage.getY(); // Get current Y position
    }

    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML file: " + fxml, e);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/software_design/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        try {
            Database database = Database.getInstance();
            // Initialize the database
            System.out.println("Checking if database exist...");
            if (database.databaseExists()) {
                System.out.println("Database already exists. Skipping creation...");
            }
            else {
                System.out.println("Database does not exist. Creating database...");
                database.createDatabase();
            }

            // Test getting a connection to the database
            Connection connection = database.getConnection();
            if (connection != null) {
                System.out.println("Database Connection Successful!");
                connection.close();
            } else {
                System.out.println("Failed to make connection!");
            }

            // Check if tables exist and create them if not
            if (database.tableExists("menu_table")) {
                System.out.println("Tables already exists. Skipping creation...");
            }
            else {
                System.out.println("Tables does not exist. Creating tables...");
                database.createTables();
            }

            database.importMenuData();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        launch();
    }
}
