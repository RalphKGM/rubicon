package com.gabriel.draw.view;

import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.ShapeMode;
import javax.swing.*;
import java.awt.*;

public class DrawingView extends JPanel {
    private final AppService appService;
    private Shape previewShape;

    public DrawingView(AppService appService) {
        this.appService = appService;
        appService.setView(this);
        setBackground(Color.WHITE);
    }

    public void setPreviewShape(Shape preview) {
        this.previewShape = preview;
        repaint();
    }

    public Shape getPreviewShape() {
        return this.previewShape;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Drawing drawing = (Drawing) appService.getModel();
    for (Shape shape : drawing.getShapes()) shape.getRendererService().render(g, shape, false);
    if (previewShape != null) previewShape.getRendererService().render(g, previewShape, false);

        Shape selected = appService.getSelectedShape();
        if (selected != null && appService.getShapeMode() == ShapeMode.SELECT) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            float[] dash = {4f, 4f};
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));

            int x = Math.min(selected.getLocation().x, selected.getEnd().x);
            int y = Math.min(selected.getLocation().y, selected.getEnd().y);
            int w = Math.abs(selected.getEnd().x - selected.getLocation().x);
            int h = Math.abs(selected.getEnd().y - selected.getLocation().y);
            g2.drawRect(x, y, w, h);

            int s = 12;
            g2.setColor(Color.WHITE);
            g2.fillRect(x - s / 2, y - s / 2, s, s);
            g2.fillRect(x + w / 2 - s / 2, y - s / 2, s, s);
            g2.fillRect(x + w - s / 2, y - s / 2, s, s);
            g2.fillRect(x - s / 2, y + h / 2 - s / 2, s, s);
            g2.fillRect(x + w - s / 2, y + h / 2 - s / 2, s, s);
            g2.fillRect(x - s / 2, y + h - s / 2, s, s);
            g2.fillRect(x + w / 2 - s / 2, y + h - s / 2, s, s);
            g2.fillRect(x + w - s / 2, y + h - s / 2, s, s);

            g2.setColor(Color.BLACK);
            g2.drawRect(x - s / 2, y - s / 2, s, s);
            g2.drawRect(x + w / 2 - s / 2, y - s / 2, s, s);
            g2.drawRect(x + w - s / 2, y - s / 2, s, s);
            g2.drawRect(x - s / 2, y + h / 2 - s / 2, s, s);
            g2.drawRect(x + w - s / 2, y + h / 2 - s / 2, s, s);
            g2.drawRect(x - s / 2, y + h - s / 2, s, s);
            g2.drawRect(x + w / 2 - s / 2, y + h - s / 2, s, s);
            g2.drawRect(x + w - s / 2, y + h - s / 2, s, s);
            g2.dispose();
        }
    }
}
