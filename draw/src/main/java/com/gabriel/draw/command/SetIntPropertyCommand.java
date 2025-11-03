package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetIntPropertyCommand implements Command {
    public enum Property {
        X, Y, WIDTH, HEIGHT, THICKNESS, STARTX, STARTY, ENDX, ENDY
    }

    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Property property;
    private final int prevValue;
    private final int newValue;

    public SetIntPropertyCommand(AppService appService, Property property, int prevValue, int newValue) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.property = property;
        this.prevValue = prevValue;
        this.newValue = newValue;
    }

    private void apply(int value) {
        
        if (underlyingService != null) {
            switch (property) {
                case X: underlyingService.setXLocation(value); break;
                case Y: underlyingService.setYLocation(value); break;
                case WIDTH: underlyingService.setWidth(value); break;
                case HEIGHT: underlyingService.setHeight(value); break;
                case THICKNESS: underlyingService.setThickness(value); break;
                case STARTX: underlyingService.setStartX(value); break;
                case STARTY: underlyingService.setStarty(value); break;
                case ENDX: underlyingService.setEndx(value); break;
                case ENDY: underlyingService.setEndy(value); break;
            }
        } else {
            
            switch (property) {
                case X: appService.setXLocation(value); break;
                case Y: appService.setYLocation(value); break;
                case WIDTH: appService.setWidth(value); break;
                case HEIGHT: appService.setHeight(value); break;
                case THICKNESS: appService.setThickness(value); break;
                case STARTX: appService.setStartX(value); break;
                case STARTY: appService.setStarty(value); break;
                case ENDX: appService.setEndx(value); break;
                case ENDY: appService.setEndy(value); break;
            }
        }
    }

    @Override
    public void execute() {
        apply(newValue);
    }

    @Override
    public void undo() {
        apply(prevValue);
    }

    @Override
    public void redo() {
        execute();
    }
}
