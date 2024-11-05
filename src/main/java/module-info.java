module software_design {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires jakarta.persistence;

    opens software_design to javafx.fxml;
    exports software_design;
}
