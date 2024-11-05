package software_design.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class Database {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASSWORD");
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    public static void createDatabase() throws SQLException {
        // Create the database if it does not exist
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + ":" + DB_PORT, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void initializeHibernate() {
        try {
            createDatabase();
            Configuration cfg = new Configuration()
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.connection.url", DB_URL)
                    .setProperty("hibernate.connection.username", DB_USER)
                    .setProperty("hibernate.connection.password", DB_PASS)
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .addAnnotatedClass(MenuTable.class);

            try (SessionFactory sessionFactory = cfg.buildSessionFactory();
                 Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.getTransaction().commit();
            }
            System.out.println("Hibernate initialized and schema updated.");
        } catch (Exception e) {
            System.out.println("Error initializing Hibernate: " + e.getMessage());
        }
    }
}
