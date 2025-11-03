package com.gabriel.draw.command;

import com.gabriel.draw.model.Image;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Drawing;

public class ReplaceImageCommand implements Command {
    private final Image imageShape;
    private final String prevFilename;
    private final String newFilename;
    private final Drawing drawing;

    public ReplaceImageCommand(Image imageShape, String prevFilename, String newFilename, Drawing drawing) {
        this.imageShape = imageShape;
        this.prevFilename = prevFilename;
        this.newFilename = newFilename;
        this.drawing = drawing;
    }

    @Override
    public void execute() {
        imageShape.setImageFilename(newFilename);
        drawing.fireDrawingChanged();
    }

    @Override
    public void undo() {
        imageShape.setImageFilename(prevFilename);
        drawing.fireDrawingChanged();
    }

    @Override
    public void redo() {
        execute();
    }
}