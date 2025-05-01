import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;

/**
 * Represents a furniture shape (table or chair) with its properties.
 * Supports serialization for saving/loading designs.
 */
public class FurnitureShape implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient RectangularShape shape;
    private Color fillColor;
    private Color borderColor;
    private int borderThickness;
    private boolean isSelected;
    private static final float SELECTION_STROKE_WIDTH = 2.0f;
    private static final Color SELECTION_COLOR = Color.BLUE;

    // Shape properties for serialization
    private double x, y, width, height;
    private boolean isRectangle;

    public FurnitureShape(RectangularShape shape, Color fillColor) {
        this.shape = shape;
        this.fillColor = fillColor;
        this.borderColor = Color.BLACK;
        this.borderThickness = 1;
        this.isSelected = false;
        updateProperties();
    }

    @Override
    public FurnitureShape clone() {
        try {
            FurnitureShape clone = (FurnitureShape) super.clone();
            clone.shape = null; // Will be restored when needed
            clone.updateProperties();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private void updateProperties() {
        if (shape != null) {
            this.x = shape.getX();
            this.y = shape.getY();
            this.width = shape.getWidth();
            this.height = shape.getHeight();
            this.isRectangle = shape instanceof Rectangle2D;
        }
    }

    private void restoreShape() {
        if (isRectangle) {
            shape = new Rectangle2D.Double(x, y, width, height);
        } else {
            shape = new Ellipse2D.Double(x, y, width, height);
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public RectangularShape getShape() {
        if (shape == null) {
            restoreShape();
        }
        return shape;
    }

    public void scale(double factor) {
        double newWidth = width * factor;
        double newHeight = height * factor;
        double newX = x + (width - newWidth) / 2;
        double newY = y + (height - newHeight) / 2;

        if (isRectangle) {
            shape = new Rectangle2D.Double(newX, newY, newWidth, newHeight);
        } else {
            shape = new Ellipse2D.Double(newX, newY, newWidth, newHeight);
        }
        updateProperties();
    }

    public boolean contains(double x, double y) {
        return getShape().contains(x, y);
    }

    public void draw(Graphics2D g2d) {
        // Draw the shape
        g2d.setColor(fillColor);
        g2d.fill(getShape());

        // Draw border
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.setColor(borderColor);
        g2d.draw(getShape());
        g2d.setStroke(oldStroke);

        // Draw selection highlight if selected
        if (isSelected) {
            g2d.setStroke(new BasicStroke(SELECTION_STROKE_WIDTH));
            g2d.setColor(SELECTION_COLOR);
            g2d.draw(getShape());
            g2d.setStroke(oldStroke);
        }
    }

    public String toFileString() {
        return String.format("%s,%.2f,%.2f,%.2f,%.2f,%06x,%06x,%d",
                isRectangle ? "rectangle" : "circle",
                x, y, width, height,
                fillColor.getRGB() & 0xFFFFFF,
                borderColor.getRGB() & 0xFFFFFF,
                borderThickness);
    }

    public static FurnitureShape fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 8)
            return null;

        try {
            boolean isRectangle = parts[0].equals("rectangle");
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double width = Double.parseDouble(parts[3]);
            double height = Double.parseDouble(parts[4]);
            Color fillColor = new Color(Integer.parseInt(parts[5], 16));
            Color borderColor = new Color(Integer.parseInt(parts[6], 16));
            int borderThickness = Integer.parseInt(parts[7]);

            RectangularShape shape;
            if (isRectangle) {
                shape = new Rectangle2D.Double(x, y, width, height);
            } else {
                shape = new Ellipse2D.Double(x, y, width, height);
            }

            FurnitureShape furnitureShape = new FurnitureShape(shape, fillColor);
            furnitureShape.setBorderColor(borderColor);
            furnitureShape.setBorderThickness(borderThickness);
            return furnitureShape;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}