import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Main design interface for the Furniture Visualizer application.
 * Contains the design area and welcome message.
 */
public class DesignScreen extends JFrame {
    private DesignCanvas designCanvas;
    private UndoManager undoManager;
    private ActionLogger actionLogger;
    private static final String DEFAULT_FILENAME = "design.txt";

    public DesignScreen() {
        setTitle("Furniture Visualizer - Design Area");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize managers
        undoManager = new UndoManager();
        actionLogger = new ActionLogger();

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to the Furniture Visualizer!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Undo/Redo buttons
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> redo());

        // File operations
        JButton saveButton = new JButton("Save Design");
        JButton loadButton = new JButton("Load Design");
        JButton exportButton = new JButton("Export PNG");
        saveButton.addActionListener(e -> saveDesign());
        loadButton.addActionListener(e -> loadDesign());
        exportButton.addActionListener(e -> exportPNG());

        // Styling options
        JButton fillColorButton = new JButton("Fill Color");
        JButton borderColorButton = new JButton("Border Color");
        JComboBox<Integer> borderThicknessCombo = new JComboBox<>(new Integer[] { 1, 2, 4 });

        fillColorButton.addActionListener(e -> changeFillColor());
        borderColorButton.addActionListener(e -> changeBorderColor());
        borderThicknessCombo
                .addActionListener(e -> changeBorderThickness((Integer) borderThicknessCombo.getSelectedItem()));

        // Logging toggle
        JCheckBox loggingCheckbox = new JCheckBox("Log Interactions", true);
        loggingCheckbox.addActionListener(e -> actionLogger.setEnabled(loggingCheckbox.isSelected()));

        // Add buttons to toolbar
        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        toolbar.add(exportButton);
        toolbar.add(fillColorButton);
        toolbar.add(borderColorButton);
        toolbar.add(new JLabel("Border:"));
        toolbar.add(borderThicknessCombo);
        toolbar.add(loggingCheckbox);

        mainPanel.add(toolbar, BorderLayout.SOUTH);

        // Create design canvas
        designCanvas = new DesignCanvas(undoManager, actionLogger);
        mainPanel.add(designCanvas, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void undo() {
        List<FurnitureShape> previousState = undoManager.undo(designCanvas.getShapes());
        designCanvas.setShapes(previousState);
        designCanvas.repaint();
        actionLogger.logAction("undo", "Reverted to previous state");
    }

    private void redo() {
        List<FurnitureShape> nextState = undoManager.redo(designCanvas.getShapes());
        designCanvas.setShapes(nextState);
        designCanvas.repaint();
        actionLogger.logAction("redo", "Restored next state");
    }

    private void changeFillColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose Fill Color", Color.WHITE);
        if (newColor != null) {
            designCanvas.changeSelectedShapeFillColor(newColor);
            actionLogger.logAction("change_fill_color",
                    "New color: " + String.format("%06x", newColor.getRGB() & 0xFFFFFF));
        }
    }

    private void changeBorderColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose Border Color", Color.BLACK);
        if (newColor != null) {
            designCanvas.changeSelectedShapeBorderColor(newColor);
            actionLogger.logAction("change_border_color",
                    "New color: " + String.format("%06x", newColor.getRGB() & 0xFFFFFF));
        }
    }

    private void changeBorderThickness(int thickness) {
        designCanvas.changeSelectedShapeBorderThickness(thickness);
        actionLogger.logAction("change_border_thickness", "New thickness: " + thickness);
    }

    private void saveDesign() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(DEFAULT_FILENAME));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                for (FurnitureShape shape : designCanvas.getShapes()) {
                    writer.println(shape.toFileString());
                }
                JOptionPane.showMessageDialog(this, "Design saved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                actionLogger.logAction("save_design", "Saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving design: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadDesign() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(DEFAULT_FILENAME));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                designCanvas.clearCanvas();
                String line;
                while ((line = reader.readLine()) != null) {
                    FurnitureShape shape = FurnitureShape.fromFileString(line);
                    if (shape != null) {
                        designCanvas.addShape(shape);
                    }
                }
                designCanvas.repaint();
                JOptionPane.showMessageDialog(this, "Design loaded successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                actionLogger.logAction("load_design", "Loaded from: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading design: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportPNG() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("design.png"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = new BufferedImage(
                        designCanvas.getWidth(),
                        designCanvas.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                designCanvas.paint(g2d);
                g2d.dispose();

                ImageIO.write(image, "PNG", file);
                JOptionPane.showMessageDialog(this, "Image exported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                actionLogger.logAction("export_png", "Exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting image: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
