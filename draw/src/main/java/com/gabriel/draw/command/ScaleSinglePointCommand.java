package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;
import java.util.List;

public class ScaleSinglePointCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Shape shape;
    private final Point newEnd;
    private int prevWidth;
    private int prevHeight;
    private Point prevLocation;
    private int shapeId;

    public ScaleSinglePointCommand(AppService appService, Shape shape, Point newEnd) {
        this.appService = appService;
        
        if (appService instanceof DrawingCommandAppService) {
            this.underlyingService = ((DrawingCommandAppService) appService).getUnderlyingService();
        } else if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        } else {
            this.underlyingService = null;
        }
        this.shape = shape;
        this.newEnd = new Point(newEnd);
        
        if (shape != null) {
            this.shapeId = shape.getId();
            this.prevWidth = shape.getWidth();
            this.prevHeight = shape.getHeight();
            this.prevLocation = new Point(shape.getLocation());
        }
    }

    @Override
    public void execute() {
        System.out.println("[ScaleSinglePointCommand] exec shape " + (shape!=null?shape.getId():-1) + " prev w=" + prevWidth + " h=" + prevHeight + " newEnd=" + newEnd);
        if (shape != null) {
            
            int dx = newEnd.x - prevLocation.x;
            int dy = newEnd.y - prevLocation.y;
            com.gabriel.drawfx.SelectionMode selectionMode = shape.getSelectionMode();
            Point baseLoc = new Point(prevLocation);
            int baseW = prevWidth;
            int baseH = prevHeight;
            int newW = baseW;
            int newH = baseH;
            Point newLoc = new Point(baseLoc);
            if (selectionMode == com.gabriel.drawfx.SelectionMode.UpperLeft) {
                newLoc.x += dx;
                newLoc.y += dy;
                newW = baseW - dx;
                newH = baseH - dy;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.LowerLeft) {
                newLoc.x += dx;
                newW = baseW - dx;
                newH = baseH + dy;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.UpperRight) {
                newLoc.y += dy;
                newW = baseW + dx;
                newH = baseH - dy;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.LowerRight) {
                newW = baseW + dx;
                newH = baseH + dy;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.MiddleRight) {
                newW = baseW + dx;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.MiddleLeft) {
                newW = baseW - dx;
                newLoc.x += dx;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.MiddleTop) {
                newH = baseH - dy;
                newLoc.y += dy;
            } else if (selectionMode == com.gabriel.drawfx.SelectionMode.MiddleBottom) {
                newH = baseH + dy;
            }
            shape.setLocation(newLoc);
            shape.setWidth(Math.max(0, newW));
            shape.setHeight(Math.max(0, newH));
            if (underlyingService != null && underlyingService.getDrawingView() != null) {
                underlyingService.getDrawingView().repaint();
            }
            System.out.println("[ScaleSinglePointCommand] exec done shape " + shape.getId() + " w=" + shape.getWidth() + " h=" + shape.getHeight());
        }
    }

    @Override
    public void undo() {
        if (shape != null && appService.getDrawing() != null) {
            
            Shape actualShape = findShapeById(appService.getDrawing().getShapes(), shapeId);
            if (actualShape != null) {
                actualShape.setWidth(prevWidth);
                actualShape.setHeight(prevHeight);
                actualShape.setLocation(new Point(prevLocation));
            }
            
            if (underlyingService != null && underlyingService.getDrawingView() != null) {
                underlyingService.getDrawingView().repaint();
            }
            System.out.println("[ScaleSinglePointCommand] undo restored shape " + shapeId + " w=" + prevWidth + " h=" + prevHeight);
        }
    }

    private Shape findShapeById(List<Shape> shapes, int id) {
        for (Shape s : shapes) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void redo() {
        execute();
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
        }
    }
}

