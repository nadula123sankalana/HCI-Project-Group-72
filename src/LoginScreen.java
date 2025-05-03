import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login screen for the Furniture Visualizer application.
 * Handles user authentication and transitions to the main design interface.
 */
public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Furniture Visualizer - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // For demo purposes, we'll just show success and transition
                JOptionPane.showMessageDialog(LoginScreen.this, "Login Successful!");
                dispose(); // Close login window
                new DesignScreen().setVisible(true); // Open design screen
            }
        });
        mainPanel.add(loginButton, gbc);

        add(mainPanel);
    }
}