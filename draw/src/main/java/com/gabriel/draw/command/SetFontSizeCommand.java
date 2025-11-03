package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetFontSizeCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final int prevFontSize;
    private final int newFontSize;

    public SetFontSizeCommand(AppService appService, int prevFontSize, int newFontSize) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevFontSize = prevFontSize;
        this.newFontSize = newFontSize;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setFontSize(newFontSize);
        } else {
            appService.setFontSize(newFontSize);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setFontSize(prevFontSize);
        } else {
            appService.setFontSize(prevFontSize);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

