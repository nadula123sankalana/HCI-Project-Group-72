import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles logging of user interactions for usability testing.
 */
public class ActionLogger {
    private static final String LOG_FILE = "user_interactions.log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean isEnabled = true;

    public void logAction(String action, String details) {
        if (!isEnabled)
            return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = DATE_FORMAT.format(new Date());
            writer.printf("%s, %s, %s%n", timestamp, action, details);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}