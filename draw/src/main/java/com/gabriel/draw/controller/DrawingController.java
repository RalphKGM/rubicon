package com.gabriel.draw.controller;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.model.SelectionBox;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.ShapeMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class DrawingController extends MouseAdapter {
    private final AppService appService;
    private final JPanel drawingView;
    private Shape dragShape;
    private SelectionBox selectionBox;
    private Point lastPoint;
    private int dragHandle = -1;
    private Point originalLocation;
    private Point originalEnd;

    public DrawingController(AppService appService, JPanel drawingView) {
        this.appService = appService;
        this.drawingView = drawingView;
        drawingView.addMouseListener(this);
        drawingView.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (appService.getShapeMode() == ShapeMode.SELECT) {
            dragShape = appService.findShapeAt(p);
            appService.setSelectedShape(dragShape);
            selectionBox = (dragShape != null) ? new SelectionBox(dragShape) : null;
            if (dragShape != null) {
                dragHandle = getHandleAt(dragShape, p);
                lastPoint = p;
                originalLocation = new Point(dragShape.getLocation());
                originalEnd = new Point(dragShape.getEnd());
            }
        } else {
            Shape previewShape = appService.newShape(appService.getShapeMode(), p, appService.getColor(), appService.getFill());
            ((com.gabriel.draw.view.DrawingView)drawingView).setPreviewShape(previewShape);
        }
        drawingView.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if (appService.getShapeMode() == ShapeMode.SELECT && dragShape != null) {
            if (dragHandle != -1) {
                resizeShape(dragShape, p, dragHandle);
            } else if (lastPoint != null) {
                int dx = p.x - lastPoint.x;
                int dy = p.y - lastPoint.y;
                dragShape.move(dx, dy);
                lastPoint = p;
            }
        }
        // update preview if creating
        if (appService.getShapeMode() != ShapeMode.SELECT) {
            com.gabriel.draw.view.DrawingView dv = (com.gabriel.draw.view.DrawingView) drawingView;
            Shape preview = dv.getPreviewShape();
            if (preview != null) preview.setEnd(p);
        }
        drawingView.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (appService.getShapeMode() == ShapeMode.SELECT && dragShape != null && originalLocation != null && originalEnd != null) {
            Point newLocation = dragShape.getLocation();
            Point newEnd = dragShape.getEnd();
            // Only push command if shape was actually moved or resized
            if (!originalLocation.equals(newLocation) || !originalEnd.equals(newEnd)) {
                com.gabriel.draw.command.MoveResizeCommand cmd = new com.gabriel.draw.command.MoveResizeCommand(
                    dragShape, originalLocation, originalEnd, newLocation, newEnd);
                appService.executeCommand(cmd);
            }
        }
        else if (appService.getShapeMode() != ShapeMode.SELECT) {
            com.gabriel.draw.view.DrawingView dv = (com.gabriel.draw.view.DrawingView) drawingView;
            Shape preview = dv.getPreviewShape();
            if (preview != null) {
                appService.executeCommand(new com.gabriel.draw.command.AddShapeCommand(appService, preview));
                dv.setPreviewShape(null);
            }
        }
        dragShape = null;
        dragHandle = -1;
        lastPoint = null;
        originalLocation = null;
        originalEnd = null;
        drawingView.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (appService.getShapeMode() == ShapeMode.SELECT && appService.getSelectedShape() != null) {
            int handle = getHandleAt(appService.getSelectedShape(), e.getPoint());
            if (handle != -1) {
                drawingView.setCursor(getResizeCursor(handle));
            } else {
                drawingView.setCursor(Cursor.getDefaultCursor());
            }
        } else {
            drawingView.setCursor(Cursor.getDefaultCursor());
        }
    }

    private int getHandleAt(Shape shape, Point p) {
        Rectangle2D bounds = shape.getBounds2D();
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int w = (int) bounds.getWidth();
        int h = (int) bounds.getHeight();
        int s = 6;
        Rectangle[] handles = new Rectangle[]{
                new Rectangle(x - s/2, y - s/2, s, s),
                new Rectangle(x + w/2 - s/2, y - s/2, s, s),
                new Rectangle(x + w - s/2, y - s/2, s, s),
                new Rectangle(x - s/2, y + h/2 - s/2, s, s),
                new Rectangle(x + w - s/2, y + h/2 - s/2, s, s),
                new Rectangle(x - s/2, y + h - s/2, s, s),
                new Rectangle(x + w/2 - s/2, y + h - s/2, s, s),
                new Rectangle(x + w - s/2, y + h - s/2, s, s)
        };
        for (int i = 0; i < handles.length; i++) {
            if (handles[i].contains(p)) return i;
        }
        return -1;
    }

    private void resizeShape(Shape shape, Point p, int handle) {
        Point loc = shape.getLocation();
        Point end = shape.getEnd();
        int x1 = loc.x, y1 = loc.y, x2 = end.x, y2 = end.y;
        switch (handle) {
            case 0 -> { x1 = p.x; y1 = p.y; }
            case 1 -> y1 = p.y;
            case 2 -> { x2 = p.x; y1 = p.y; }
            case 3 -> x1 = p.x;
            case 4 -> x2 = p.x;
            case 5 -> { x1 = p.x; y2 = p.y; }
            case 6 -> y2 = p.y;
            case 7 -> { x2 = p.x; y2 = p.y; }
        }
        shape.setLocation(new Point(x1, y1));
        shape.setEnd(new Point(x2, y2));
    }

    private Cursor getResizeCursor(int handle) {
        return switch (handle) {
            case 0 -> Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            case 1 -> Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            case 2 -> Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
            case 3 -> Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            case 4 -> Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            case 5 -> Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
            case 6 -> Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            case 7 -> Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
            default -> Cursor.getDefaultCursor();
        };
    }
}
