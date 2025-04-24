import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JPanel for drawing furniture shapes in 2D.
 * Handles mouse events and maintains a list of drawn shapes.
 */
public class DesignCanvas extends JPanel {
    private List<FurnitureShape> shapes;
    private FurnitureShape selectedShape;
    private static final int TABLE_SIZE = 50;
    private static final int CHAIR_SIZE = 30;
    private static final double SCALE_FACTOR = 1.1;

    private final UndoManager undoManager;
    private final ActionLogger actionLogger;

    public DesignCanvas(UndoManager undoManager, ActionLogger actionLogger) {
        this.undoManager = undoManager;
        this.actionLogger = actionLogger;
        shapes = new ArrayList<>();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Add mouse listener for placing and selecting furniture
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Check if clicking on existing shape
                    boolean clickedOnShape = false;
                    for (FurnitureShape shape : shapes) {
                        if (shape.contains(e.getX(), e.getY())) {
                            // Deselect previous shape if any
                            if (selectedShape != null) {
                                selectedShape.setSelected(false);
                            }
                            // Select new shape
                            selectedShape = shape;
                            shape.setSelected(true);
                            clickedOnShape = true;
                            actionLogger.logAction("select_shape",
                                    String.format("Selected shape at (%d, %d)", e.getX(), e.getY()));
                            break;
                        }
                    }

                    // If not clicking on existing shape, create new table
                    if (!clickedOnShape) {
                        if (selectedShape != null) {
                            selectedShape.setSelected(false);
                            selectedShape = null;
                        }
                        Rectangle2D.Double rect = new Rectangle2D.Double(
                                e.getX() - TABLE_SIZE / 2, e.getY() - TABLE_SIZE / 2,
                                TABLE_SIZE, TABLE_SIZE);
                        FurnitureShape newShape = new FurnitureShape(rect, new Color(139, 69, 19));
                        shapes.add(newShape);
                        actionLogger.logAction("add_table",
                                String.format("Added table at (%d, %d)", e.getX(), e.getY()));
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // Create new chair
                    Ellipse2D.Double circle = new Ellipse2D.Double(
                            e.getX() - CHAIR_SIZE / 2, e.getY() - CHAIR_SIZE / 2,
                            CHAIR_SIZE, CHAIR_SIZE);
                    FurnitureShape newShape = new FurnitureShape(circle, new Color(210, 180, 140));
                    shapes.add(newShape);
                    actionLogger.logAction("add_chair",
                            String.format("Added chair at (%d, %d)", e.getX(), e.getY()));
                }
                saveState();
                repaint();
            }
        });

        // Add keyboard listener for scaling
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (selectedShape != null) {
                    switch (e.getKeyChar()) {
                        case '+':
                            selectedShape.scale(SCALE_FACTOR);
                            actionLogger.logAction("scale_shape", "Increased size");
                            break;
                        case '-':
                            selectedShape.scale(1 / SCALE_FACTOR);
                            actionLogger.logAction("scale_shape", "Decreased size");
                            break;
                    }
                    saveState();
                    repaint();
                }
            }
        });

        // Enable keyboard focus
        setFocusable(true);
    }

    private void saveState() {
        undoManager.saveState(shapes);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all shapes
        for (FurnitureShape shape : shapes) {
            shape.draw(g2d);
        }
    }

    /**
     * Clears all shapes from the canvas
     */
    public void clearCanvas() {
        shapes.clear();
        selectedShape = null;
        saveState();
        actionLogger.logAction("clear_canvas", "All shapes removed");
        repaint();
    }

    /**
     * Changes the fill color of the selected shape
     */
    public void changeSelectedShapeFillColor(Color color) {
        if (selectedShape != null) {
            selectedShape.setFillColor(color);
            saveState();
            repaint();
        }
    }

    /**
     * Changes the border color of the selected shape
     */
    public void changeSelectedShapeBorderColor(Color color) {
        if (selectedShape != null) {
            selectedShape.setBorderColor(color);
            saveState();
            repaint();
        }
    }

    /**
     * Changes the border thickness of the selected shape
     */
    public void changeSelectedShapeBorderThickness(int thickness) {
        if (selectedShape != null) {
            selectedShape.setBorderThickness(thickness);
            saveState();
            repaint();
        }
    }

    /**
     * Adds a shape to the canvas
     */
    public void addShape(FurnitureShape shape) {
        shapes.add(shape);
    }

    /**
     * Sets the shapes on the canvas
     */
    public void setShapes(List<FurnitureShape> newShapes) {
        this.shapes = new ArrayList<>(newShapes);
        selectedShape = null;
    }

    /**
     * Returns the list of shapes for potential saving/loading
     */
    public List<FurnitureShape> getShapes() {
        return shapes;
    }
}