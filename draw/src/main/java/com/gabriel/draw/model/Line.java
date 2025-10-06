package com.gabriel.draw.model;

import com.gabriel.draw.service.LineRendererService;
import com.gabriel.drawfx.model.Shape;
import java.awt.*;

public class Line extends Shape {
    public Line(Point start, Point end, Color stroke, Color fill){
        super(start);
        this.setEnd(end);
        this.setColor(stroke);
        this.setFill(fill);
        this.setRendererService(new LineRendererService());
    }

    @Override
    public boolean contains(Point p) {
        int x1 = getLocation().x;
        int y1 = getLocation().y;
        int x2 = getEnd().x;
        int y2 = getEnd().y;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lengthSq = dx * dx + dy * dy;
        if (lengthSq == 0) return p.distance(x1, y1) < 3;
        double t = ((p.x - x1) * dx + (p.y - y1) * dy) / lengthSq;
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        double dist = p.distance(projX, projY);
        return dist <= 3;
    }

    @Override
    public Shape cloneShape() {
        Line clone = new Line(new Point(getLocation()), new Point(getEnd()), getColor(), getFill());
        return clone;
    }
}