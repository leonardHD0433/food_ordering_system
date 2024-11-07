package software_design.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    public static void logSevere(Logger logger, String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    public static void logInfo(Logger logger, String message) {
        logger.log(Level.INFO, message);
    }

    // Add more utility methods as needed
}
