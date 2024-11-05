package software_design.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String dbUser = dotenv.get("DB_USER");
            String dbPass = dotenv.get("DB_PASSWORD");

            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName);
            configuration.setProperty("hibernate.connection.username", dbUser);
            configuration.setProperty("hibernate.connection.password", dbPass);
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");

            sessionFactory = configuration.configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
