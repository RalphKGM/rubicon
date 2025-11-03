package com.gabriel.draw.service;

import com.gabriel.draw.command.*;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingCommandAppService implements AppService {
    public AppService appService;
    protected static AppService drawingCommandAppService = null;
    
    private DrawingAppService underlyingService;

    protected DrawingCommandAppService(AppService appService){
        this.appService = appService;
        
        if (appService instanceof DrawingAppService) {
            this.underlyingService = (DrawingAppService) appService;
        }
    }
    
    
    public DrawingAppService getUnderlyingService() {
        return underlyingService;
    }

    public static AppService getInstance(){
        return drawingCommandAppService;
    }

    public static AppService getInstance(AppService appService){
        if(drawingCommandAppService == null){
            drawingCommandAppService = new DrawingCommandAppService(appService);
        };
        return drawingCommandAppService;
    }

    @Override
    public void undo() {
        CommandService.undo();
        System.out.println("[DrawingCommandAppService] undo completed, attempting UI refresh");
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            System.out.println("[DrawingCommandAppService] underlying drawingView found");
            try {
                underlyingService.getDrawingView().repaint();
                java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(underlyingService.getDrawingView());
                System.out.println("[DrawingCommandAppService] window ancestor: " + win);
                if (win != null && win instanceof com.gabriel.draw.view.DrawingFrame) {
                    System.out.println("[DrawingCommandAppService] calling refreshPropertySheet on DrawingFrame");
                    ((com.gabriel.draw.view.DrawingFrame) win).refreshPropertySheet();
                }
            } catch (Exception ex) {
                
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void redo() {
        CommandService.redo();
        System.out.println("[DrawingCommandAppService] redo completed, attempting UI refresh");
        
        if (underlyingService != null && underlyingService.getDrawingView() != null) {
            System.out.println("[DrawingCommandAppService] underlying drawingView found");
            try {
                underlyingService.getDrawingView().repaint();
                java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(underlyingService.getDrawingView());
                System.out.println("[DrawingCommandAppService] window ancestor: " + win);
                if (win != null && win instanceof com.gabriel.draw.view.DrawingFrame) {
                    System.out.println("[DrawingCommandAppService] calling refreshPropertySheet on DrawingFrame");
                    ((com.gabriel.draw.view.DrawingFrame) win).refreshPropertySheet();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public ShapeMode getShapeMode() {
        return appService.getShapeMode();
    }

    @Override
    public void setShapeMode(ShapeMode shapeMode) {
        appService.setShapeMode(shapeMode);
    }

    @Override
    public DrawMode getDrawMode() {
        return appService.getDrawMode();
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        
        
        appService.setDrawMode(drawMode);
    }

    @Override
    public Color getColor() {
        return appService.getColor();
    }

    @Override
    public void setColor(Color color) {
        Color prev = appService.getColor();
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetColorCommand(appService, prev, color);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public Color getFill() {
        return appService.getFill();
    }

    @Override
    public void setFill(Color color) {
        Color prev = appService.getFill();
        Command command = new SetFillCommand(appService, prev, color);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void move(Shape shape, Point start, Point end) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.MoveCommand(appService, shape, start, end);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void move(Point start, Point end) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.MoveCommand(appService, null, start, end);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void scale(Point start, Point end) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.ScaleCommand(appService, null, start, end);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void scale(Shape shape, Point start, Point end) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.ScaleCommand(appService, shape, start, end);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void scale(Shape shape, Point end) {
        Command command = new ScaleSinglePointCommand(appService, shape, end);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void create(Shape shape) {
        Command command = new AddShapeCommand(appService, shape);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void delete(Shape shape) {
        Command command = new DeleteShapeCommand(appService, shape);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void close() {
        appService.close();
    }

    @Override
    public Drawing getDrawing() {
        return appService.getDrawing();
    }

    @Override
    public void setDrawing(Drawing drawing) {
        appService.setDrawing(drawing);
    }

    @Override
    public int getSearchRadius() {
        return appService.getSearchRadius();
    }

    @Override
    public void setSearchRadius(int radius) {
        appService.setSearchRadius(radius);
    }

    @Override
    public void search(Point p) {
        appService.search(p);
    }

    @Override
    public void search(Point p, boolean single) {
        appService.search(p, single);
    }

    @Override
    public void open(String filename) {
        appService.open(filename);
    }


    @Override
    public void save() {
        appService.save();;
    }

    @Override
    public String getFileName() {
        return appService.getFileName();
    }

    @Override
    public void select(Shape selectedShape) {
        appService.select(selectedShape);
    }

    @Override
    public void unSelect(Shape selectedShape) {
        appService.unSelect(selectedShape);
    }

    @Override
    public Shape getSelectedShape() {
        return appService.getSelectedShape();
    }

    @Override
    public List<Shape> getSelectedShapes() {
        return appService.getSelectedShapes();
    }

    @Override
    public void clearSelections(){
        appService.clearSelections();;
    }

    @Override
    public void setThickness(int thickness) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.THICKNESS, appService.getThickness(), thickness);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getThickness() {
        return appService.getThickness();
    }

    @Override
    public void setXLocation(int xLocation) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.X, appService.getXLocation(), xLocation);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getXLocation() {
        return appService.getXLocation();
    }

    @Override
    public void setYLocation(int yLocation) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.Y, appService.getYLocation(), yLocation);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getYLocation() {
        return appService.getYLocation();
    }

    @Override
    public void setWidth(int width) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.WIDTH, appService.getWidth(), width);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getWidth() {
        return appService.getWidth();
    }

    @Override
    public void setHeight(int height) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.HEIGHT, appService.getHeight(), height);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getHeight() {
        return appService.getHeight();
    }

    @Override
    public void setImageFilename(String imageFilename) {
        String prev = appService.getImageFilename();
        Command command = new SetImageFilenameCommand(appService, prev, imageFilename);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public String getImageFilename() {
        return appService.getImageFilename();
    }

    @Override
    public void setText(String text) {
        String prev = appService.getText();
        Command command = new SetTextCommand(appService, prev, text);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void setFontSize(int fontSize) {
        
        Font currentFont = appService.getFont();
        int prevFontSize = currentFont != null ? currentFont.getSize() : 12;
        Command command = new SetFontSizeCommand(appService, prevFontSize, fontSize);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public Color getStartColor() {
        return appService.getStartColor();
    }

    @Override
    public void setStartColor(Color color) {
        Color prev = appService.getStartColor();
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetColorCommand(appService, prev, color);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public Color getEndColor() {
        return appService.getEndColor();
    }

    @Override
    public void setEndColor(Color color) {
        Color prev = appService.getEndColor();
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetColorCommand(appService, prev, color);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public boolean isGradient() {
        return appService.isGradient();
    }

    @Override
    public void setIsGradient(boolean yes) {
        boolean prev = appService.isGradient();
        Command command = new SetIsGradientCommand(appService, prev, yes);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public boolean isVisible() {
        return appService.isVisible();
    }

    @Override
    public void setIsVisible(boolean yes) {
        boolean prev = appService.isVisible();
        Command command = new SetIsVisibleCommand(appService, prev, yes);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void delete() {
        Command command = new DeleteSelectedShapesCommand(appService);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void setStartX(int startx) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.STARTX, appService.getStartX(), startx);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getStartX() {
        return appService.getStartX();
    }

    @Override
    public void setStarty(int starty) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.STARTY, appService.getStarty(), starty);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getStarty() {
        return appService.getStarty();
    }

    @Override
    public void setEndx(int endx) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.ENDX, appService.getEndx(), endx);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getEndx() {
        return appService.getEndx();
    }

    @Override
    public void setEndy(int endy) {
        com.gabriel.drawfx.command.Command command = new com.gabriel.draw.command.SetIntPropertyCommand(appService, com.gabriel.draw.command.SetIntPropertyCommand.Property.ENDY, appService.getEndy(), endy);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public int getEndy() {
        return appService.getEndy();
    }

    @Override
    public String getText() {
        return appService.getText();
    }

    @Override
    public Font getFont() {
        return appService.getFont();
    }

    @Override
    public void setFont(Font font) {
        Font prev = appService.getFont();
        Command command = new SetFontCommand(appService, prev, font);
        CommandService.ExecuteCommand(command);
    }
}
