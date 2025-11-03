package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;

public class SetColorCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Color newColor;
    private final Color prevColor;
    
    public SetColorCommand(AppService appService, Color prevColor, Color newColor){
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevColor = prevColor;
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setColor(newColor);
        } else {
            appService.setColor(newColor);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setColor(prevColor);
        } else {
            appService.setColor(prevColor);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
