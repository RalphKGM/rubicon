package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.service.AppService;

import java.awt.Font;

public class SetFontCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Font prevFont;
    private final Font newFont;

    public SetFontCommand(AppService appService, Font prevFont, Font newFont) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.prevFont = prevFont;
        this.newFont = newFont;
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            underlyingService.setFont(newFont);
        } else {
            appService.setFont(newFont);
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            underlyingService.setFont(prevFont);
        } else {
            appService.setFont(prevFont);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

