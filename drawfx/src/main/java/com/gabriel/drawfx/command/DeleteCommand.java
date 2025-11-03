package com.gabriel.drawfx.command;

import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;

public class DeleteCommand implements Command {

    private final Drawing drawing;
    private final Shape shape;
    private int index = -1;

    public DeleteCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        if (shape == null) return;
        index = drawing.getShapes().indexOf(shape);
        if (index >= 0) {
            drawing.getShapes().remove(index);
        }
    }

    @Override
    public void undo() {
        if (shape == null) return;
        if (index >= 0 && index <= drawing.getShapes().size()) {
            drawing.getShapes().add(index, shape);
        } else {
            drawing.getShapes().add(shape);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
