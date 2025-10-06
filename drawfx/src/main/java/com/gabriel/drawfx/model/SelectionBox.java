package com.gabriel.drawfx.model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SelectionBox {
    private Shape target;
    private Rectangle2D bounds;
    private static final int HANDLE_SIZE = 8;

    public SelectionBox(Shape target) {
        this.target = target;
        this.bounds = target.getBounds2D();
    }

    public void update() {
        this.bounds = target.getBounds2D();
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        float[] dash = {4f, 4f};
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4f, dash, 0f));
        g2.draw(bounds);

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.WHITE);
        for (Rectangle2D handle : getHandles()) {
            g2.fill(handle);
            g2.setColor(Color.BLACK);
            g2.draw(handle);
            g2.setColor(Color.WHITE);
        }
    }

    public Rectangle2D[] getHandles() {
        Rectangle2D[] handles = new Rectangle2D[8];
        double x = bounds.getX();
        double y = bounds.getY();
        double w = bounds.getWidth();
        double h = bounds.getHeight();

        handles[0] = new Rectangle2D.Double(x - HANDLE_SIZE/2, y - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[1] = new Rectangle2D.Double(x + w/2 - HANDLE_SIZE/2, y - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[2] = new Rectangle2D.Double(x + w - HANDLE_SIZE/2, y - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[3] = new Rectangle2D.Double(x - HANDLE_SIZE/2, y + h/2 - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[4] = new Rectangle2D.Double(x + w - HANDLE_SIZE/2, y + h/2 - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[5] = new Rectangle2D.Double(x - HANDLE_SIZE/2, y + h - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[6] = new Rectangle2D.Double(x + w/2 - HANDLE_SIZE/2, y + h - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
        handles[7] = new Rectangle2D.Double(x + w - HANDLE_SIZE/2, y + h - HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);

        return handles;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }
}
