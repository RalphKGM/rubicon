package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.List;

public class DeleteSelectedShapesCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final List<Shape> deletedShapes;

    public DeleteSelectedShapesCommand(AppService appService) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.deletedShapes = new ArrayList<>(appService.getSelectedShapes());
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            for (Shape shape : deletedShapes) {
                underlyingService.delete(shape);
            }
        } else {
            for (Shape shape : deletedShapes) {
                appService.delete(shape);
            }
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            for (Shape shape : deletedShapes) {
                underlyingService.create(shape);
            }
        } else {
            for (Shape shape : deletedShapes) {
                appService.create(shape);
            }
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

