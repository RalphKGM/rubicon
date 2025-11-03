package com.gabriel.draw.command;

import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScaleCommand implements Command {
    private final AppService appService;
    private final DrawingAppService underlyingService; 
    private final Shape shape;
    private final Point start;
    private final Point end;
    
    private Map<Integer, ShapeState> shapeStates = new HashMap<>();

    private static class ShapeState {
        Point location;
        int width;
        int height;

        ShapeState(Shape shape) {
            this.location = new Point(shape.getLocation());
            this.width = shape.getWidth();
            this.height = shape.getHeight();
        }

        void restore(Shape shape) {
            shape.setLocation(new Point(location));
            shape.setWidth(width);
            shape.setHeight(height);
        }
    }

    public ScaleCommand(AppService appService, Shape shape, Point start, Point end) {
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
        
        
        if (shape == null) {
            
            List<Shape> shapes = appService.getDrawing().getShapes();
            for (Shape s : shapes) {
                shapeStates.put(s.getId(), new ShapeState(s));
            }
        } else {
            
            shapeStates.put(shape.getId(), new ShapeState(shape));
        }
    }

    @Override
    public void execute() {
        
        
        
        System.out.println("[ScaleCommand] execute - applying deterministic scale for debugging");
        for (Shape s : appService.getDrawing().getShapes()) {
            ShapeState prevState = shapeStates.get(s.getId());
            if (prevState == null) continue;
            
            int dx = end.x - start.x;
            int dy = end.y - start.y;
            com.gabriel.drawfx.SelectionMode selectionMode = s.getSelectionMode();
            
            Point baseLoc = new Point(prevState.location);
            int baseW = prevState.width;
            int baseH = prevState.height;
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
            
            s.setLocation(newLoc);
            s.setWidth(Math.max(0, newW));
            s.setHeight(Math.max(0, newH));
            System.out.println("[ScaleCommand] shape " + s.getId() + " applied w=" + s.getWidth() + " h=" + s.getHeight());
        }
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
        }
    }

    @Override
    public void undo() {
        
        List<Shape> shapes = appService.getDrawing().getShapes();
        for (Shape s : shapes) {
            ShapeState state = shapeStates.get(s.getId());
            if (state != null) {
                state.restore(s);
            }
        }
        
        for (Shape s : appService.getDrawing().getShapes()) {
            System.out.println("[ScaleCommand] undo restored shape " + s.getId() + " w=" + s.getWidth() + " h=" + s.getHeight());
        }
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            underlyingService.getDrawingView().repaint();
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
