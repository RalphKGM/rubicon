package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;


public class MoveResizeCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService;
    private final int shapeId;

    private final Point prevLocation;
    private final int prevWidth;
    private final int prevHeight;

    private final Point newLocation;
    private final int newWidth;
    private final int newHeight;

    public MoveResizeCommand(AppService appService, Shape shape, Point prevLocation, int prevWidth, int prevHeight, Point newLocation, int newWidth, int newHeight) {
        this.appService = appService;
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.shapeId = shape != null ? shape.getId() : -1;
        this.prevLocation = prevLocation != null ? new Point(prevLocation) : null;
        this.prevWidth = prevWidth;
        this.prevHeight = prevHeight;
        this.newLocation = newLocation != null ? new Point(newLocation) : null;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void execute() {
        Shape s = findShape();
        if (s == null) return;
        if (newLocation != null) s.setLocation(new Point(newLocation));
        s.setWidth(newWidth);
        s.setHeight(newHeight);
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
        }
    }

    @Override
    public void undo() {
        Shape s = findShape();
        if (s == null) return;
        if (prevLocation != null) s.setLocation(new Point(prevLocation));
        s.setWidth(prevWidth);
        s.setHeight(prevHeight);
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
        }
    }

    @Override
    public void redo() {
        execute();
    }

    private Shape findShape() {
        if (appService == null || appService.getDrawing() == null) return null;
        for (Shape s : appService.getDrawing().getShapes()) {
            if (s.getId() == shapeId) return s;
        }
        return null;
    }
}
