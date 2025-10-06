package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

public class DeleteShapeCommand implements Command {
    private final Shape shape;
    private final AppService appService;

    public DeleteShapeCommand(AppService appService, Shape shape) {
        this.shape = shape;
        this.appService = appService;
    }

    @Override
    public void execute() {
        appService.delete(shape);
    }

    @Override
    public void undo() {
        appService.create(shape);
    }

    @Override
    public void redo() {
        execute();
    }
}
