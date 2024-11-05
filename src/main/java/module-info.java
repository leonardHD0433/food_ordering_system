module software_design {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires org.slf4j;
    requires java.persistence;
    requires java.naming;

    opens software_design to javafx.fxml;
    exports software_design;
}
