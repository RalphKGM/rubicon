package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

public class SetTextCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final String prevText;
    private final String newText;

    public SetTextCommand(AppService appService, String prevText, String newText) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevText = prevText;
        this.newText = newText;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setText(newText);
        } else {
            appService.setText(newText);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setText(prevText);
        } else {
            appService.setText(prevText);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

