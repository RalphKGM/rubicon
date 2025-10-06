package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import java.awt.Point;

public class MoveResizeCommand implements Command {
    private final Shape shape;
    private final Point oldLocation;
    private final Point oldEnd;
    private final Point newLocation;
    private final Point newEnd;

    public MoveResizeCommand(Shape shape, Point oldLocation, Point oldEnd, Point newLocation, Point newEnd) {
        this.shape = shape;
        this.oldLocation = new Point(oldLocation);
        this.oldEnd = new Point(oldEnd);
        this.newLocation = new Point(newLocation);
        this.newEnd = new Point(newEnd);
    }

    @Override
    public void execute() {
        shape.setLocation(new Point(newLocation));
        shape.setEnd(new Point(newEnd));
    }

    @Override
    public void undo() {
        shape.setLocation(new Point(oldLocation));
        shape.setEnd(new Point(oldEnd));
    }

    @Override
    public void redo() {
        execute();
    }
}
