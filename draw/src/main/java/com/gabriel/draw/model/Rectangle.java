package com.gabriel.draw.model;

import com.gabriel.draw.service.RectangleRendererService;
import com.gabriel.drawfx.model.Shape;
import java.awt.*;

public class Rectangle extends Shape {
    public Rectangle(Point start, Point end, Color stroke, Color fill){
        super(start);
        this.setEnd(end);
        this.setColor(stroke);
        this.setFill(fill);
        this.setRendererService(new RectangleRendererService());
    }

    @Override
    public boolean contains(Point p) {
        int x1 = Math.min(getLocation().x, getEnd().x);
        int y1 = Math.min(getLocation().y, getEnd().y);
        int x2 = Math.max(getLocation().x, getEnd().x);
        int y2 = Math.max(getLocation().y, getEnd().y);
        return p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2;
    }

    @Override
    public Shape cloneShape() {
        Rectangle clone = new Rectangle(new Point(getLocation()), new Point(getEnd()), getColor(), getFill());
        return clone;
    }
}