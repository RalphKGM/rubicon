package com.gabriel.draw.service;

import com.gabriel.draw.model.Ellipse;
import com.gabriel.draw.model.Line;
import com.gabriel.draw.model.Rectangle;
import com.gabriel.draw.view.DrawingMenuBar;
import com.gabriel.draw.view.DrawingToolBar;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.service.MoverService;
import com.gabriel.drawfx.service.ScalerService;
import com.gabriel.drawfx.command.Command;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class DrawingAppService implements AppService {

    private final Drawing drawing;
    private final MoverService moverService;
    private final ScalerService scalerService;
    private JPanel drawingView;
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    private DrawingMenuBar menuBar;
    private DrawingToolBar toolBar;

    private Shape selectedShape;

    public DrawingAppService() {
        this.drawing = new Drawing();
        this.moverService = new MoverService();
        this.scalerService = new ScalerService();
        drawing.setDrawMode(DrawMode.IDLE);
        drawing.setShapeMode(ShapeMode.ELLIPSE);
    }

    @Override
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
            updateUndoRedoState();
            repaint();
        }
    }

    @Override
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.redo();
            undoStack.push(cmd);
            updateUndoRedoState();
            repaint();
        }
    }

    @Override
    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
        updateUndoRedoState();
        repaint();
    }

    @Override
    public ShapeMode getShapeMode() { return drawing.getShapeMode(); }
    @Override
    public void setShapeMode(ShapeMode shapeMode) { drawing.setShapeMode(shapeMode); }
    @Override
    public DrawMode getDrawMode() { return drawing.getDrawMode(); }
    @Override
    public void setDrawMode(DrawMode drawMode) { drawing.setDrawMode(drawMode); }
    @Override
    public Color getColor() { return drawing.getColor(); }
    @Override
    public void setColor(Color color) { drawing.setColor(color); }
    @Override
    public Color getFill() { return drawing.getFill(); }
    @Override
    public void setFill(Color color) { drawing.setFill(color); }

    @Override
    public void move(Shape shape, Point newLoc) {
        moverService.move(shape, newLoc);
        repaint();
    }

    @Override
    public void scale(Shape shape, Point newEnd) {
        shape.setEnd(newEnd);
        repaint();
    }

    @Override
    public void create(Shape shape) {
        shape.setId(drawing.getShapes().size());
        drawing.getShapes().add(shape);
        redoStack.clear();
        updateUndoRedoState();
        repaint();
    }

    @Override
    public void delete(Shape shape) {
        drawing.getShapes().remove(shape);
        // If the deleted shape was selected, clear selection so selection box is removed
        if (selectedShape != null && selectedShape.equals(shape)) {
            setSelectedShape(null);
        }
        updateUndoRedoState();
        repaint();
    }

    @Override
    public void close() { System.exit(0); }

    @Override
    public Object getModel() { return drawing; }
    @Override
    public JPanel getView() { return drawingView; }
    @Override
    public void setView(JPanel panel) { this.drawingView = panel; }
    @Override
    public void repaint() { if (drawingView != null) drawingView.repaint(); }

    @Override
    public void registerMenuBar(JMenuBar menuBar) { this.menuBar = (DrawingMenuBar) menuBar; }
    @Override
    public void registerToolBar(JToolBar toolBar) { this.toolBar = (DrawingToolBar) toolBar; }

    @Override
    public void updateUndoRedoState() {
        boolean canUndo = !drawing.getShapes().isEmpty();
        boolean canRedo = !undoStack.isEmpty();

        if (menuBar != null) {
            menuBar.enableUndo(canUndo);
            menuBar.enableRedo(canRedo);
        }
        if (toolBar != null) {
            toolBar.enableUndo(canUndo);
            toolBar.enableRedo(canRedo);
        }
    }

    @Override
    public Shape findShapeAt(Point p) {
        for (int i = drawing.getShapes().size() - 1; i >= 0; i--) {
            Shape s = drawing.getShapes().get(i);
            if (s.getBounds().contains(p)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Shape getSelectedShape() {
        return selectedShape;
    }

    @Override
    public void setSelectedShape(Shape shape) {
        this.selectedShape = shape;
        repaint();
    }

    @Override
    public Shape newShape(ShapeMode mode, Point start, Color color, Color fill) {
        switch (mode) {
            case LINE:
                return new Line(start, start, color, fill);
            case RECTANGLE:
                return new Rectangle(start, start, color, fill);
            case ELLIPSE:
                return new Ellipse(start, start, color, fill);
            default:
                throw new UnsupportedOperationException("Unsupported ShapeMode: " + mode);
        }
    }
}
