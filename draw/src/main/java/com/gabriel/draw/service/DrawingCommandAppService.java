package com.gabriel.draw.service;

import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;
import java.awt.Point;

import javax.swing.*;

public class DrawingCommandAppService implements AppService {
    @Override
    public void executeCommand(com.gabriel.drawfx.command.Command cmd) {
        wrapped.executeCommand(cmd);
    }

    private final AppService wrapped;
    private Shape selectedShape;

    public DrawingCommandAppService(AppService wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void undo() { wrapped.undo(); }

    @Override
    public void redo() { wrapped.redo(); }

    @Override
    public void create(Shape shape) { wrapped.create(shape); }

    @Override
    public void delete(Shape shape) { wrapped.delete(shape); }

    @Override
    public void move(Shape shape, Point newLoc) { wrapped.move(shape, newLoc); }

    @Override
    public void scale(Shape shape, Point newEnd) { wrapped.scale(shape, newEnd); }

    @Override
    public ShapeMode getShapeMode() { return wrapped.getShapeMode(); }

    @Override
    public void setShapeMode(ShapeMode shapeMode) { wrapped.setShapeMode(shapeMode); }

    @Override
    public com.gabriel.drawfx.DrawMode getDrawMode() { return wrapped.getDrawMode(); }

    @Override
    public void setDrawMode(com.gabriel.drawfx.DrawMode drawMode) { wrapped.setDrawMode(drawMode); }

    @Override
    public Color getColor() { return wrapped.getColor(); }

    @Override
    public void setColor(Color color) { wrapped.setColor(color); }

    @Override
    public Color getFill() { return wrapped.getFill(); }

    @Override
    public void setFill(Color color) { wrapped.setFill(color); }

    @Override
    public void close() { wrapped.close(); }

    @Override
    public Object getModel() { return wrapped.getModel(); }

    @Override
    public JPanel getView() { return wrapped.getView(); }

    @Override
    public void setView(JPanel panel) { wrapped.setView(panel); }

    @Override
    public void repaint() { wrapped.repaint(); }

    @Override
    public void registerMenuBar(JMenuBar menuBar) { wrapped.registerMenuBar(menuBar); }

    @Override
    public void registerToolBar(JToolBar toolBar) { wrapped.registerToolBar(toolBar); }

    @Override
    public void updateUndoRedoState() { wrapped.updateUndoRedoState(); }

    @Override
    public Shape findShapeAt(Point p) { return wrapped.findShapeAt(p); }

    @Override
    public Shape getSelectedShape() { return selectedShape; }

    @Override
    public void setSelectedShape(Shape shape) { this.selectedShape = shape; }

    @Override
    public Shape newShape(ShapeMode mode, Point start, Color color, Color fill) {
        return wrapped.newShape(mode, start, color, fill);
    }
}
