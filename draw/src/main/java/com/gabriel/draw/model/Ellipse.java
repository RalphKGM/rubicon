package com.gabriel.draw.model;

import com.gabriel.draw.service.EllipseRenderer;
import com.gabriel.drawfx.model.Shape;
import java.awt.*;

public class Ellipse extends Shape {
    public Ellipse(Point start, Point end, Color stroke, Color fill){
        super(start);
        this.setEnd(end);
        this.setColor(stroke);
        this.setFill(fill);
        this.setRendererService(new EllipseRenderer());
    }

    @Override
    public boolean contains(Point p) {
        int x1 = Math.min(getLocation().x, getEnd().x);
        int y1 = Math.min(getLocation().y, getEnd().y);
        int w = Math.abs(getEnd().x - getLocation().x);
        int h = Math.abs(getEnd().y - getLocation().y);
        if (w == 0 || h == 0) return false;
        double normX = (p.x - (x1 + w / 2.0)) / (w / 2.0);
        double normY = (p.y - (y1 + h / 2.0)) / (h / 2.0);
        return normX * normX + normY * normY <= 1.0;
    }

    @Override
    public Shape cloneShape() {
        Ellipse clone = new Ellipse(new Point(getLocation()), new Point(getEnd()), getColor(), getFill());
        return clone;
    }
}