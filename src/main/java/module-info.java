module software_design {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires jakarta.persistence;
    requires javafx.swing;

    exports software_design.controller;
    exports software_design.model;
    opens software_design.controller to javafx.fxml;
    exports software_design to javafx.graphics;
}