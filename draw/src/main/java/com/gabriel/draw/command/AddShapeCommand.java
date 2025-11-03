package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

public class AddShapeCommand implements Command{
    Shape shape;
    AppService appService;
    private final DrawingAppService underlyingService; 

    public AddShapeCommand(AppService appService, Shape shape){
        this.shape = shape;
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
    }
    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.create(shape);
        } else {
            appService.create(shape);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.delete(shape);
        } else {
            appService.delete(shape);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}