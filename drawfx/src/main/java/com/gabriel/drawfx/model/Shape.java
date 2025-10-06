package com.gabriel.drawfx.model;

import com.gabriel.drawfx.service.RendererService;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Shape {
    private int id;
    private Point location;
    private Point end;
    private Color color;
    private Color fill;
    private RendererService rendererService;
    private boolean selected;

    public abstract Shape cloneShape();

    public Shape(Point location) {
        this.location = location;
        this.end = location;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }

    public Point getEnd() { return end; }
    public void setEnd(Point end) { this.end = end; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Color getFill() { return fill; }
    public void setFill(Color fill) { this.fill = fill; }

    public RendererService getRendererService() { return rendererService; }
    public void setRendererService(RendererService rendererService) { this.rendererService = rendererService; }

    public abstract boolean contains(Point p);

    public void move(int dx, int dy) {
        location = new Point(location.x + dx, location.y + dy);
        end = new Point(end.x + dx, end.y + dy);
    }

    public void scale(Point newEnd) {
        this.end = newEnd;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Rectangle getBounds() {
        int x = Math.min(location.x, end.x);
        int y = Math.min(location.y, end.y);
        int w = Math.abs(end.x - location.x);
        int h = Math.abs(end.y - location.y);
        return new Rectangle(x, y, w, h);
    }

    public Rectangle2D getBounds2D() {
        return getBounds().getBounds2D();
    }
}
