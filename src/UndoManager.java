import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages undo and redo operations for the design canvas.
 */
public class UndoManager {
    private Stack<List<FurnitureShape>> undoStack;
    private Stack<List<FurnitureShape>> redoStack;
    private static final int MAX_STACK_SIZE = 50;

    public UndoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void saveState(List<FurnitureShape> shapes) {
        // Create a deep copy of the shapes
        List<FurnitureShape> stateCopy = new ArrayList<>();
        for (FurnitureShape shape : shapes) {
            stateCopy.add(shape.clone());
        }

        undoStack.push(stateCopy);
        redoStack.clear(); // Clear redo stack when new action is performed

        // Limit stack size
        if (undoStack.size() > MAX_STACK_SIZE) {
            undoStack.remove(0);
        }
    }

    public List<FurnitureShape> undo(List<FurnitureShape> currentState) {
        if (undoStack.isEmpty())
            return currentState;

        // Save current state to redo stack
        List<FurnitureShape> currentCopy = new ArrayList<>();
        for (FurnitureShape shape : currentState) {
            currentCopy.add(shape.clone());
        }
        redoStack.push(currentCopy);

        return undoStack.pop();
    }

    public List<FurnitureShape> redo(List<FurnitureShape> currentState) {
        if (redoStack.isEmpty())
            return currentState;

        // Save current state to undo stack
        List<FurnitureShape> currentCopy = new ArrayList<>();
        for (FurnitureShape shape : currentState) {
            currentCopy.add(shape.clone());
        }
        undoStack.push(currentCopy);

        return redoStack.pop();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}