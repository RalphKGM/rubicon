package com.gabriel.draw.service;

import com.gabriel.draw.view.DrawingView;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class DrawingAppService implements AppService {

    final private Drawing drawing;;

    @Setter
    DrawingView drawingView;

    
    public DrawingView getDrawingView() {
        return drawingView;
    }

    ImageFileService imageFileService;
    MoverService moverService;
    ScalerService scalerService;
    SearchService searchService;
    XmlDocumentService xmlDocumentService;

    DocumentService documentService;
    public DrawingAppService(){
        drawing = new Drawing();
        moverService = new MoverService();
        scalerService = new ScalerService();
        searchService = new SearchService();
        xmlDocumentService = new XmlDocumentService(drawing);
        imageFileService = new ImageFileService();
        drawing.setDrawMode(DrawMode.Idle);
        drawing.setShapeMode(ShapeMode.Ellipse);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public ShapeMode getShapeMode() {
        return drawing.getShapeMode();
    }

    @Override
    public void setShapeMode(ShapeMode shapeMode) {
        drawing.setShapeMode(shapeMode);
    }

    @Override
    public DrawMode getDrawMode() {
        return drawing.getDrawMode();
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        this.drawing.setDrawMode(drawMode);
    }

    @Override
    public Color getColor() {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            return drawing.getColor();
        } else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getColor();
        }
    }

    @Override
    public void setColor(Color color) {
        List<Shape> shapes = drawing.getShapes();
        boolean isEmpty = true;
        for (Shape shape : shapes) {
            if (shape.isSelected()) {
                shape.setColor(color);
                shape.getRendererService().render(drawingView.getGraphics(), shape, false);
                isEmpty = false;
            }
        }
        if(isEmpty){
            drawing.setColor(color);
        }
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public Color getFill(){
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            return drawing.getFill();
        } else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getFill();
        }
    }

    @Override
    public void setFill(Color color) {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            drawing.setFill(color);
        } else {
            selectedShape.setFill(color);
            
            if (drawingView != null) drawingView.repaint();
            return;
        }
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void move(Shape shape, Point start, Point newLoc) {
        moverService.move(shape, start, newLoc);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void move(Point start, Point newLoc) {
        moverService.move(drawing, start, newLoc);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void scale(Point start, Point end) {
        scalerService.scale(drawing, start, end);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void scale(Shape shape, Point start, Point end) {
        scalerService.scale(shape, start, end);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void scale(Shape shape, Point end) {
        scalerService.scale(shape, end);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void create(Shape shape) {
        this.drawing.getShapes().add(shape);
        shape.setColor(drawing.getColor());
        shape.setR(drawing.getSearchRadius());
        shape.setFill(drawing.getFill());
        shape.setThickness(drawing.getThickness());
        shape.setId(this.drawing.getShapes().size());
        shape.setFont(drawing.getFont());
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void delete(Shape shape) {
        drawing.getShapes().remove(shape);
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void close() {
        System.exit(0);
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

    @Override
    public void setDrawing(Drawing drawing) {

    }

    @Override
    public int getSearchRadius() {
        return drawing.getSearchRadius();
    }

    @Override
    public void setSearchRadius(int radius) {
        drawing.setSearchRadius(radius);
    }

    @Override
    public void search(Point p) {
        searchService.search(this,p);
    }

    @Override
    public void search(Point p, boolean single) {
        searchService.search(this,p, single);
    }

    @Override
    public void open(String filename) {
        xmlDocumentService.open();
    }

    @Override
    public void save() {
        xmlDocumentService.save();
    }

    @Override
    public String getFileName() {
        return drawing.getFilename();
    }

    @Override
    public void select(Shape selectedShape) {
        List<Shape> selectedShapes = drawing.getShapes();
        for(Shape shape : selectedShapes){
            if(shape.equals(selectedShape)){
                shape.setSelected(true);
            }
            else {
                shape.setSelected(false);
            }
        }
        
        drawing.setSelectedShape(selectedShape);
        
        if (drawingView != null) {
            drawingView.repaint();
            try {
                java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(drawingView);
                if (win != null && win instanceof com.gabriel.draw.view.DrawingFrame) {
                    
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        try {
                            ((com.gabriel.draw.view.DrawingFrame) win).refreshPropertySheet();
                        } catch (Exception inner) {
                            
                            inner.printStackTrace();
                        }
                    });
                }
            } catch (Exception ex) {
                
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void unSelect(Shape selectedShape) {
        List<Shape> shapes = drawing.getShapes();
        for (Shape shape : shapes){
            if(shape.getId() == selectedShape.getId()) {
                shape.setSelected(false);
            }
        }
    }

    @Override
    public Shape getSelectedShape() {
        return drawing.getSelectedShape();
    }
    @Override
    public List<Shape> getSelectedShapes() {
        List<Shape> shapes = drawing.getShapes();
        List<Shape> selectedShapes = new ArrayList<>();
        for (Shape shape : shapes){
            if(shape.isSelected()){
                selectedShapes.add(shape);
            }
        }
        return selectedShapes;
    }
    @Override
    public void clearSelections(){
        List<Shape> shapes = drawing.getShapes();
        for (Shape shape : shapes){
            shape.setSelected(false);
            shape.setSelectionMode(SelectionMode.None);
        }
        drawing.setSelectedShape(null);
        drawingView.repaint();
    }

    @Override
    public void setThickness(int thickness) {
        Shape seleectedShape = drawing.getSelectedShape();
        if(seleectedShape == null ){
            drawing.setThickness(thickness);
        }
        else {
            seleectedShape.setThickness(thickness);
        }
    }

    @Override
    public int getThickness() {
        Shape seleectedShape = drawing.getSelectedShape();
        if(seleectedShape == null ){
           return drawing.getThickness();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return seleectedShape.getThickness();
        }
    }

    @Override
    public void setXLocation(int x) {
        Shape seleectedShape = drawing.getSelectedShape();
        if(seleectedShape == null ){
            drawing.getLocation().x = x;
        }
        else {
            seleectedShape.getLocation().x = x;
        }

    }


    @Override
    public int getXLocation() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            return drawing.getLocation().x;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getLocation().x;
        }
    }

    @Override
    public void setYLocation(int yLocation) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            drawing.getLocation().y = yLocation;
        }
        else {
            selectedShape.getLocation().y = yLocation;
        }
    }


    @Override
    public int getYLocation() {
        Shape seleectedShape = drawing.getSelectedShape();
        if(seleectedShape == null ){
            return drawing.getLocation().y;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return seleectedShape.getLocation().y;
        }
    }

    @Override
    public void setWidth(int width) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            drawing.setWidth(width);
        }
        else {
            selectedShape.setWidth(width);
        }
    }

    @Override
    public int getWidth() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            return drawing.getWidth();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getWidth();
        }
    }

    @Override
    public void setHeight(int height) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            drawing.setHeight(height);
        }
        else {
            selectedShape.setHeight(height);
        }
    }

    @Override
    public int getHeight() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            return drawing.getHeight();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getHeight();
        }
    }

    @Override
    public void setImageFilename(String imageFilename) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            drawing.setImageFilename(imageFilename);
        }
        else {
            selectedShape.setImageFilename(imageFilename);;
        }
    }

    @Override
    public String getImageFilename() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            return drawing.getImageFilename();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getImageFilename();
        }
    }

    @Override
    public void setText(String text) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape == null ){
            drawing.setText(text);
        }
        else {
            selectedShape.setText(text);
        }
    }

    @Override
    public void setFontSize(int fontSize) {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            Font font = new Font(drawing.getFont().getFamily(), drawing.getFont().getStyle(), fontSize);
            drawing.setFont(font);
        } else {
            Font font = new Font(selectedShape.getFont().getFamily(), selectedShape.getFont().getStyle(), fontSize);
            selectedShape.setFont(font);
        }
        if (drawingView != null) drawingView.repaint();
    }

    @Override
    public Color getStartColor() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getStartColor();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getStartColor();
        }
    }

    @Override
    public void setStartColor(Color color) {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            drawing.setStartColor(color);
        } else {
            selectedShape.setStartColor(color);
        }
    }

    @Override
    public Color getEndColor() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getEndColor();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getEndColor();
        }
    }

    @Override
    public void setEndColor(Color color) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.setEndColor(color);
        }
        else {
            selectedShape.setEndColor(color);
        }
    }

    @Override
    public boolean isGradient() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.isGradient();
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.isGradient();
        }
    }

    @Override
    public void setIsGradient(boolean yes) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.setGradient(yes);
        }
        else {
            selectedShape.setGradient(yes);
        }
    }

    @Override
    public boolean isVisible() {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            return drawing.isVisible();
        } else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.isVisible();
        }
    }
    @Override
    public void setIsVisible(boolean yes) {
        Shape selectedShape = drawing.getSelectedShape();
        if (selectedShape == null) {
            drawing.setVisible(yes);
        } else {
            selectedShape.setVisible(yes);
        }
    }

    public void delete() {
        List<Shape> shapes = drawing.getShapes();
        for(Shape shape : shapes) {
            if(shape.isSelected()) {
                delete(shape);
            }
        }
            if (drawingView != null) drawingView.repaint();
    }

    @Override
    public void setStartX(int startx) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.getStart().x = startx;
        }
        else {
            selectedShape.getStart().x = startx;
        }
    }

    @Override
    public int getStartX() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getStart().x;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getStart().x;
        }
    }

    @Override
    public void setStarty(int starty) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.getStart().y = starty;
        }
        else {
            selectedShape.getStart().y = starty;
        }
    }

    @Override
    public int getStarty() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getStart().x;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getStart().x;
        }
    }

    @Override
    public void setEndx(int endx) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.getEnd().x = endx;
        }
        else {
            selectedShape.getEnd().x = endx;
        }
    }

    @Override
    public int getEndx() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getEnd().x;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getEnd().x;
        }
    }

    @Override
    public void setEndy(int endy) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.getEnd().y = endy;
        }
        else {
            selectedShape.getEnd().y = endy;
        }
    }

    @Override
    public int getEndy() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getEnd().y;
        }
        else {
            if (drawingView != null) drawingView.repaint();
            return selectedShape.getEnd().y;
        }
    }

    @Override
    public String getText() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getText();
        }
        else {
            return selectedShape.getText();
        }
    }

    @Override
    public Font getFont() {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            return drawing.getFont();
        }
        else {
            return selectedShape.getFont();
        }
    }

    @Override
    public void setFont(Font font) {
        Shape selectedShape = drawing.getSelectedShape();
        if(selectedShape==null) {
            drawing.setFont(font);
        }
        else {
            selectedShape.setFont(font);
        }
    }
}
