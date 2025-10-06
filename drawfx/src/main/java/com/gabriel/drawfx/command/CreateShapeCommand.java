package com.gabriel.drawfx.command;

import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;

public class CreateShapeCommand implements Command {
    private final Drawing drawing;
    private final Shape shape;

    public CreateShapeCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        drawing.addShape(shape);
    }

    @Override
    public void undo() {
        drawing.removeShape(shape);
    }

    @Override
    public void redo() {
        execute();
    }
}
