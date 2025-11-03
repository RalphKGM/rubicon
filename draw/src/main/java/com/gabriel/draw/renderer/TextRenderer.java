package com.gabriel.draw.renderer;

import com.gabriel.draw.model.Text;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.renderer.ShapeRenderer;

import java.awt.*;

public class TextRenderer extends ShapeRenderer {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        if(!shape.isVisible()){
            return;
        }

        Text text = (Text) shape;

        int x = shape.getLocation().x;
        int y = shape.getLocation().y;
        int width = shape.getWidth() ;
        int height = shape.getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(shape.getThickness()));

        if (xor) {
            java.awt.Color c = shape.getColor();
            if (c != null) {
                g2.setXORMode(c);
                g2.drawRect(x, y, width, height);
            } else {
                
            }
        } else {
            
            if (shape.isGradient()) {
                java.awt.Color start = shape.getStartColor() != null ? shape.getStartColor() : (shape.getFill() != null ? shape.getFill() : shape.getColor());
                java.awt.Color end = shape.getEndColor() != null ? shape.getEndColor() : Color.BLACK;
                GradientPaint gp = new GradientPaint(x, y, start, x + width, y + height, end);
                g2.setPaint(gp);
            } else if (shape.getFill() != null) {
                g2.setColor(shape.getFill());
            } else {
                g2.setColor(shape.getColor());
            }
            g2.setFont(shape.getFont());
            g2.drawString(shape.getText(), shape.getLocation().x, shape.getLocation().y);
        }
        super.render(g, shape, xor);
    }
}
