package app;  // adjust package name if different

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggerConfig {
    private static final String LOG_DIR = "logs";
    private static final boolean APPEND = true;

    private static final String SEPARATOR = "--------------------------------------------------------------------";

    static {
        try {
            // ✅ Ensure logs directory exists
            Path logDirPath = Paths.get(LOG_DIR);
            if (!Files.exists(logDirPath)) {
                Files.createDirectories(logDirPath);
            }

            // ✅ Create date-based filename
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String logFileName = LOG_DIR + "/app-" + date + ".log";

            LogManager.getLogManager().reset();
            Logger rootLogger = Logger.getLogger("");

            // 🔹 File handler logs only SEVERE (exceptions)
            FileHandler fileHandler = new FileHandler(logFileName, APPEND);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.SEVERE);

            // 🔹 Console handler logs ALL (for normal debugging + exceptions)
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.ALL);

            rootLogger.addHandler(fileHandler);
            rootLogger.addHandler(consoleHandler);

            // Root logger should allow all, filtering is done at handler level
            rootLogger.setLevel(Level.ALL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    /**
     * Centralized exception logging with separators
     */
    public static void logException(Logger logger, String message, Exception e) {
        logger.severe(SEPARATOR);
        logger.log(Level.SEVERE, message, e);
        logger.severe(SEPARATOR);
    }
}
