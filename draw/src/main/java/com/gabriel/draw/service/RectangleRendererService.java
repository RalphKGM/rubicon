package com.gabriel.draw.service;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.RendererService;

import java.awt.*;

public class RectangleRendererService implements RendererService {
    @Override
    public void render(Graphics g, Shape shape, boolean finalRender) {
        Point start = shape.getLocation();
        Point end = shape.getEnd();

        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        if (shape.getFill() != null) {
            g.setColor(shape.getFill());
            g.fillRect(x, y, width, height);
        }

        g.setColor(shape.getColor());
        g.drawRect(x, y, width, height);
    }
}
