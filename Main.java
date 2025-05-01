import javax.swing.SwingUtilities;

/**
 * Main entry point for the Furniture Visualizer application.
 * Initializes the application using Swing's event dispatch thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}