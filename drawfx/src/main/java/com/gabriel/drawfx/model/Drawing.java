package com.gabriel.drawfx.model;

import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class Drawing {

    private Color color;
    private Color fill;
    private ShapeMode shapeMode = ShapeMode.RECTANGLE;
    private DrawMode drawMode = DrawMode.IDLE;
    private List<Shape> shapes;
    private List<Shape> selectedShapes;

    public Drawing() {
        color = Color.RED;
        fill = Color.WHITE;
        selectedShapes = new ArrayList<>();
        shapes = new ArrayList<>();
    }
    private SelectionBox selectionBox;

    public void setSelection(Shape s) {
        if (s == null) {
            selectionBox = null;
        } else {
            selectionBox = new SelectionBox(s);
        }
    }

    public SelectionBox getSelectionBox() {
        return selectionBox;
    }

    public void addShape(Shape shape) {
    shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }


}
