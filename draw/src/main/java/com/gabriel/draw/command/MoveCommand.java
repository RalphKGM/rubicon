package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;

public class MoveCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Shape shape;
    private final Point start;
    private final Point end;

    public MoveCommand(AppService appService, Shape shape, Point start, Point end) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.shape = shape;
        this.start = new Point(start);
        this.end = new Point(end);
    }

    @Override
    public void execute() {
        
        if (underlyingService != null) {
            if (shape == null) {
                underlyingService.move(start, end);
            } else {
                underlyingService.move(shape, start, end);
            }
        } else {
            
            if (shape == null) {
                appService.move(start, end);
            } else {
                appService.move(shape, start, end);
            }
        }
    }

    @Override
    public void undo() {
        
        if (underlyingService != null) {
            if (shape == null) {
                underlyingService.move(end, start);
            } else {
                underlyingService.move(shape, end, start);
            }
            
            if (underlyingService.getDrawingView() != null) {
                underlyingService.getDrawingView().repaint();
            }
        } else {
            
            if (shape == null) {
                appService.move(end, start);
            } else {
                appService.move(shape, end, start);
            }
        }
    }

    @Override
    public void redo() {
        execute();
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
        }
    }
}
