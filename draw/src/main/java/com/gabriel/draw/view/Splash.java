package com.gabriel.draw.view;

import com.gabriel.draw.util.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Splash extends JPanel implements MouseListener {
    private BufferedImage image;
    private GPanel gPanel;
    ImageLoader imageLoader;
    int width;
    int height;
    public Splash() {
        try {
            imageLoader = new ImageLoader();
            image = imageLoader.loadImage("/nette1440_800.png");

            height = image.getHeight();
            width = image.getWidth();

        } catch (IOException ex) {
            
        }
        setSize(width, height);
        setLayout(null);

        gPanel = new GPanel("GoDraw");
        gPanel.setBounds(1100,700,150,50);
        gPanel.addMouseListener(this);
        this.add(gPanel);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource()==gPanel){
            final java.awt.Window topWindow = SwingUtilities.getWindowAncestor(this);

            
            gPanel.setEnabled(false);

            
            
            final int delay = 40; 
            final float step = 0.05f; 

            
            try {
                if (topWindow != null) topWindow.setOpacity(1.0f);
            } catch (Exception ex) {
                
                
                
                DrawingFrame mf = new DrawingFrame();
                mf.setExtendedState(mf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                mf.setVisible(true);
                if (topWindow != null) {
                    topWindow.setVisible(false);
                    topWindow.dispose();
                }
                return;
            }

            final Timer timer = new Timer(delay, null);
            timer.addActionListener(ev -> {
                try {
                    float cur = topWindow != null ? topWindow.getOpacity() : 0f;
                    cur -= step;
                    if (cur <= 0f) {
                        timer.stop();
                        
                        DrawingFrame mf = new DrawingFrame();
                        mf.setExtendedState(mf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                        mf.setVisible(true);
                        
                        if (topWindow != null) {
                            topWindow.setVisible(false);
                            topWindow.dispose();
                        }
                    } else {
                        if (topWindow != null) topWindow.setOpacity(Math.max(0f, cur));
                    }
                } catch (Exception ex) {
                    
                    
                    timer.stop();
                    DrawingFrame mf = new DrawingFrame();
                    mf.setExtendedState(mf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                    mf.setVisible(true);
                    if (topWindow != null) {
                        topWindow.setVisible(false);
                        topWindow.dispose();
                    }
                }
            });
            timer.setInitialDelay(0);
            timer.start();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
