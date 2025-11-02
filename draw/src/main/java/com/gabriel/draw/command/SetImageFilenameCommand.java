package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetImageFilenameCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final String prevFilename;
    private final String newFilename;

    public SetImageFilenameCommand(AppService appService, String prevFilename, String newFilename) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevFilename = prevFilename;
        this.newFilename = newFilename;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setImageFilename(newFilename);
        } else {
            appService.setImageFilename(newFilename);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setImageFilename(prevFilename);
        } else {
            appService.setImageFilename(prevFilename);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

