package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetIsGradientCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final boolean prevValue;
    private final boolean newValue;

    public SetIsGradientCommand(AppService appService, boolean prevValue, boolean newValue) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevValue = prevValue;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setIsGradient(newValue);
        } else {
            appService.setIsGradient(newValue);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setIsGradient(prevValue);
        } else {
            appService.setIsGradient(prevValue);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

