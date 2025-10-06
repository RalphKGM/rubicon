package com.gabriel.drawfx.service;

import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Shape;

import javax.swing.*;
import java.awt.*;

public interface AppService {
    void executeCommand(com.gabriel.drawfx.command.Command cmd);
    void undo();
    void redo();

    ShapeMode getShapeMode();
    void setShapeMode(ShapeMode shapeMode);

    DrawMode getDrawMode();
    void setDrawMode(DrawMode drawMode);

    Color getColor();
    void setColor(Color color);

    Color getFill();
    void setFill(Color color);

    void move(Shape shape, Point newLoc);
    void scale(Shape shape, Point newEnd);

    void create(Shape shape);
    void delete(Shape shape);

    void close();

    Object getModel();
    JPanel getView();
    void setView(JPanel panel);
    void repaint();

    void registerMenuBar(JMenuBar menuBar);
    void registerToolBar(JToolBar toolBar);
    void updateUndoRedoState();

    Shape findShapeAt(Point p);
    Shape getSelectedShape();
    void setSelectedShape(Shape shape);

    Shape newShape(ShapeMode mode, Point start, Color color, Color fill);
}
