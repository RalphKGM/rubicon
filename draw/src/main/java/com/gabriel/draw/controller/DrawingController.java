package com.gabriel.draw.controller;

import com.gabriel.draw.component.PropertySheet;
import com.gabriel.draw.model.*;
import com.gabriel.draw.model.Image;
import com.gabriel.draw.model.Rectangle;
import com.gabriel.draw.view.DrawingStatusPanel;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.util.Normalizer;
import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.draw.view.DrawingView;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.model.Shape;
import lombok.Setter;

import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class DrawingController  implements MouseListener, MouseMotionListener, KeyListener {
    Point start;
    private Point end;
    private Point originalStart; 
    private java.util.Map<Shape, Point> originalPositions; 
    private Point originalScaleStart; 
    private java.util.Map<Shape, ScaleState> originalScaleStates; 

    
    private static class ScaleState {
        Point location;
        int width;
        int height;

        ScaleState(Shape shape) {
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

    private final AppService appService;
    
    private com.gabriel.draw.service.DrawingAppService underlyingService;
    private final Drawing drawing;

    @Setter
    private DrawingView drawingView;

    @Setter
    private DrawingStatusPanel drawingStatusPanel;

    @Setter
    private PropertySheet propertySheet;

    private Shape currentShape = null;

         public DrawingController(AppService appService, DrawingView drawingView){
             this.appService = appService;
             this.drawing = appService.getDrawing();
             this.drawingView = drawingView;
             
             if (appService instanceof com.gabriel.draw.service.DrawingCommandAppService) {
                     this.underlyingService = ((com.gabriel.draw.service.DrawingCommandAppService) appService).getUnderlyingService();
             } else if (appService instanceof com.gabriel.draw.service.DrawingAppService) {
                     this.underlyingService = (com.gabriel.draw.service.DrawingAppService) appService;
             }
             drawingView.addMouseListener(this);
             drawingView.addMouseMotionListener(this);
         }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(appService.getDrawMode() == DrawMode.Idle) {
            start = e.getPoint();
            originalStart = e.getPoint(); 
            originalScaleStart = e.getPoint(); 
            originalPositions = new java.util.HashMap<>(); 
            originalScaleStates = new java.util.HashMap<>(); 
            ShapeMode currentShapeMode = appService.getShapeMode();
            if (currentShapeMode == ShapeMode.Select) {
                
                
                appService.search(start, !e.isControlDown());
                
                List<Shape> shapes = drawing.getShapes();
                for (Shape shape : shapes) {
                    if (shape.isSelected()) {
                        if (shape.getSelectionMode() == SelectionMode.None) {
                            originalPositions.put(shape, new Point(shape.getLocation()));
                        } else {
                            
                            originalScaleStates.put(shape, new ScaleState(shape));
                        }
                    }
                }
            } else {
                if(currentShape!=null){
                    currentShape.setSelected(false);
                }
                switch (currentShapeMode) {
                    case Line:
                        currentShape = new Line(start);
                        currentShape.setColor(appService.getColor());
                        currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, false);
                        break;
                    case Rectangle:
                        currentShape = new Rectangle(start);
                        currentShape.setColor(appService.getColor());
                        currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, false);
                        break;
                    case Text:
                        currentShape = new Text(start);
                        currentShape.setColor(appService.getColor());
                        currentShape.setText(drawing.getText());
                        currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, true);
                        appService.setDrawMode(DrawMode.MousePressed);
                        break;
                    case Ellipse:
                        currentShape = new Ellipse(start);
                        currentShape.setColor(appService.getColor());
                        currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, false);
                        break;
                    case Image:
                        currentShape = new Image(start);
                        currentShape.setImageFilename(drawing.getImageFilename());
                        currentShape.setColor(appService.getColor());
                        currentShape.setThickness(appService.getThickness());
                }

          }
            appService.setDrawMode(DrawMode.MousePressed);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        end = e.getPoint();
        if(appService.getDrawMode() == DrawMode.MousePressed) {
            if (appService.getShapeMode() == ShapeMode.Select) {
                Shape selectedShape = drawing.getSelectedShape();
                if (selectedShape != null) {
                    if (selectedShape.getSelectionMode() == SelectionMode.None) {
                        
                        java.util.List<Shape> selectedShapes = appService.getSelectedShapes();
                        if (selectedShapes.size() == 1) {
                            Shape shape = selectedShapes.get(0);
                            Point originalPos = originalPositions.get(shape);
                            if (originalPos != null) {
                                Point currentPos = shape.getLocation();
                                if (currentPos.x != originalPos.x || currentPos.y != originalPos.y) {
                                    
                                    int prevW = shape.getWidth();
                                    int prevH = shape.getHeight();
                                    
                                    Point newLoc = new Point(currentPos);
                                    int newW = prevW;
                                    int newH = prevH;
                                    
                                    shape.setLocation(new Point(originalPos));
                                    com.gabriel.draw.command.MoveResizeCommand cmd = new com.gabriel.draw.command.MoveResizeCommand(appService, shape, originalPos, prevW, prevH, newLoc, newW, newH);
                                    com.gabriel.drawfx.command.CommandService.ExecuteCommand(cmd);
                                }
                            }
                        } else {
                            
                            java.util.List<Shape> shapes = drawing.getShapes();
                            boolean anyMoved = false;
                            for (Shape shape : shapes) {
                                if (shape.isSelected()) {
                                    Point originalPos = originalPositions.get(shape);
                                    if (originalPos != null) {
                                        Point currentPos = shape.getLocation();
                                        if (currentPos.x != originalPos.x || currentPos.y != originalPos.y) {
                                            anyMoved = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (anyMoved) {
                                for (Shape shape : shapes) {
                                    if (shape.isSelected()) {
                                        Point originalPos = originalPositions.get(shape);
                                        if (originalPos != null) {
                                            shape.setLocation(new Point(originalPos));
                                        }
                                    }
                                }
                                appService.move(originalStart, end);
                            }
                        }
                    } else {
                        
                        ScaleState originalState = originalScaleStates.get(selectedShape);
                        if (originalState != null) {
                            
                            Point newLoc = new Point(selectedShape.getLocation());
                            int newW = selectedShape.getWidth();
                            int newH = selectedShape.getHeight();
                            boolean hasChanged = !newLoc.equals(originalState.location) ||
                                              newW != originalState.width ||
                                              newH != originalState.height;

                            if (hasChanged) {
                                
                                Point prevLoc = new Point(originalState.location);
                                int prevW = originalState.width;
                                int prevH = originalState.height;
                                originalState.restore(selectedShape);
                                
                                com.gabriel.draw.command.MoveResizeCommand cmd = new com.gabriel.draw.command.MoveResizeCommand(
                                        appService,
                                        selectedShape,
                                        prevLoc, prevW, prevH,
                                        newLoc, newW, newH
                                );
                                com.gabriel.drawfx.command.CommandService.ExecuteCommand(cmd);
                            }
                        } else {
                            
                            appService.scale(selectedShape, originalScaleStart, end);
                        }
                        Normalizer.normalize(selectedShape);
                        drawingView.repaint();
                    }
                }
            }
            else {
                currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, true);
                appService.scale(currentShape, end);
                currentShape.setText(drawing.getText());
                currentShape.setFont(drawing.getFont());
                currentShape.setGradient(drawing.isGradient());
                currentShape.setFill(drawing.getFill());
                currentShape.setStartColor(drawing.getStartColor());
                currentShape.setEndColor(drawing.getEndColor());
                Normalizer.normalize(currentShape);
                appService.create(currentShape);
                currentShape.setSelected(true);

                drawing.setSelectedShape(currentShape);
                drawing.setShapeMode(ShapeMode.Select);
                drawingView.repaint();
            }
            appService.setDrawMode(DrawMode.Idle);
        }
        if (propertySheet != null) {
            propertySheet.populateTable(appService);
        } else {
            
            System.err.println("Warning: propertySheet is null in DrawingController.mouseReleased");
        }
        drawingView.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(appService.getDrawMode() == DrawMode.MousePressed) {
            end = e.getPoint();
            if(drawing.getShapeMode() == ShapeMode.Select){
                Shape selectedShape = drawing.getSelectedShape();
                if(selectedShape != null){
                    if(selectedShape.getSelectionMode() == SelectionMode.None){
                        List<Shape> shapes =drawing.getShapes();
                        for(Shape shape : shapes) {
                            if (shape.isSelected()) {
                                shape.getRendererService().render(drawingView.getGraphics(), shape, true);
                                
                                
                                int dx = end.x - start.x;
                                int dy = end.y - start.y;
                                shape.getLocation().x += dx;
                                shape.getLocation().y += dy;
                                shape.getRendererService().render(drawingView.getGraphics(), shape, true);
                            }
                        }
                    }
                    else {
                        
                        
                        int dx = end.x - start.x;
                        int dy = end.y - start.y;
                        int height = selectedShape.getHeight();
                        int width = selectedShape.getWidth();
                        com.gabriel.drawfx.SelectionMode selectionMode = selectedShape.getSelectionMode();
                        
                        if(selectionMode == com.gabriel.drawfx.SelectionMode.UpperLeft) {
                            selectedShape.getLocation().x += dx;
                            selectedShape.getLocation().y += dy;
                            selectedShape.setWidth(width - dx);
                            selectedShape.setHeight(height - dy);
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.LowerLeft) {
                            selectedShape.getLocation().x += dx;
                            selectedShape.setWidth(width -dx);
                            selectedShape.setHeight(height + dy);
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.UpperRight){
                            selectedShape.getLocation().y += dy;
                            selectedShape.setWidth(width + dx);
                            selectedShape.setHeight(height - dy);
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.LowerRight){
                            selectedShape.setWidth(width + dx);
                            selectedShape.setHeight(height+ dy);
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.MiddleRight){
                            selectedShape.setWidth(width + dx);
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.MiddleLeft){
                            selectedShape.setWidth(width - dx);
                            selectedShape.getLocation().x += dx;
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.MiddleTop) {
                            selectedShape.setHeight(height - dy);
                            selectedShape.getLocation().y += dy;
                        } else if(selectionMode == com.gabriel.drawfx.SelectionMode.MiddleBottom){
                            selectedShape.setHeight(height + dy);
                        }
                    }
                }
                start = end;

            }
            else {
                currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, true);
                
                if (underlyingService != null) {
                    underlyingService.scale(currentShape, end);
                } else {
                    appService.scale(currentShape, end);
                }
                currentShape.getRendererService().render(drawingView.getGraphics(), currentShape, true);
            }
       }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        drawingStatusPanel.setPoint(e.getPoint());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {



    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
