package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;

public class SetFillCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Color prevColor;
    private final Color newColor;

    public SetFillCommand(AppService appService, Color prevColor, Color newColor) {
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
            underlyingService.setFill(newColor);
        } else {
            appService.setFill(newColor);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setFill(prevColor);
        } else {
            appService.setFill(prevColor);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

