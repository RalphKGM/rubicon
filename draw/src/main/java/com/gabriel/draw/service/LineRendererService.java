package com.gabriel.draw.service;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.RendererService;

import java.awt.*;

public class LineRendererService implements RendererService {
    @Override
    public void render(Graphics g, Shape shape, boolean finalRender) {
        g.setColor(shape.getColor());
        Point start = shape.getLocation();
        Point end = shape.getEnd();
        g.drawLine(start.x, start.y, end.x, end.y);
    }
}
