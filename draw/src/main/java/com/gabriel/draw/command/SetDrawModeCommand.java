package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetDrawModeCommand implements Command {
    DrawMode drawMode;
    DrawMode prevDrawMode;
    AppService appService;
    private final DrawingAppService underlyingService; 
    
    public SetDrawModeCommand(AppService appService, DrawMode drawMode){
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.drawMode = drawMode;
    }

    @Override
    public void execute() {
        prevDrawMode = appService.getDrawMode();
        
        if (underlyingService != null) {
            underlyingService.setDrawMode(drawMode);
        } else {
            appService.setDrawMode(drawMode);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setDrawMode(prevDrawMode);
        } else {
            appService.setDrawMode(prevDrawMode);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
