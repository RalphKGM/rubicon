package com.gabriel.draw.view;

import com.gabriel.draw.controller.ActionController;
import com.gabriel.drawfx.ActionCommand;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class DrawingToolBar extends JToolBar {

    protected JTextArea textArea;
    ActionListener actionListener;

  public DrawingToolBar( ActionListener actionListener){
        setFloatable(false);
        setRollover(true);
        this.actionListener = actionListener;
        addButtons();

        textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        
        setPreferredSize(new Dimension(200, 30));
        setBackground(Color.GREEN);
    }

    protected void addButtons() {
        JButton button = null;
        button = makeNavigationButton("rect", ActionCommand.RECT, "Draw a rectangle",ActionCommand.RECT);
        add(button);

        button = makeNavigationButton("line", ActionCommand.LINE, "Draw a line",ActionCommand.LINE);
        add(button);

        button = makeNavigationButton("ellipse", ActionCommand.ELLIPSE,"Draw an ellipse",ActionCommand.ELLIPSE);
        add(button);

        button = makeNavigationButton("text",ActionCommand.TEXT,"Add a text",ActionCommand.TEXT);
        add(button);

        button = makeNavigationButton("image",ActionCommand.IMAGE,"Add an  image",ActionCommand.IMAGE);
        add(button);

        button = makeNavigationButton("select",ActionCommand.SELECT,"Switch to select",ActionCommand.SELECT);
        add(button);

        button = makeNavigationButton("imagefile",ActionCommand.IMAGEFILE,"Select another image ",ActionCommand.IMAGEFILE);
        add(button);

        button = makeNavigationButton("font",ActionCommand.FONT,"Select another font ",ActionCommand.FONT);
        add(button);

        
        addSeparator();

    
    button = makeNavigationButton("undo", com.gabriel.drawfx.ActionCommand.UNDO, "Undo last action", com.gabriel.drawfx.ActionCommand.UNDO);
    add(button);

    button = makeNavigationButton("redo", com.gabriel.drawfx.ActionCommand.REDO, "Redo last undone action", com.gabriel.drawfx.ActionCommand.REDO);
    add(button);

    
    JTextField textField = new JTextField("");
    textField.setColumns(10);
    textField.addActionListener(actionListener);
    textField.setActionCommand("TEXT_ENTERED");
    add(textField);
    }

    protected JButton makeNavigationButton(String imageName,
        String actionCommand,
        String toolTipText,
        String altText) {
        
        String imgLocation = "images/"
                + imageName
                + ".png";
        URL imageURL = DrawingToolBar.class.getResource(imgLocation);

        
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(actionListener);

        if (imageURL != null) {                      
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
            try {
                java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(24, 24, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g2 = img.createGraphics();
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(0,0,24,24);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(0,0,23,23);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(altText.substring(0, Math.min(3, altText.length())), 3, 14);
                g2.dispose();
                button.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                
            }
        }
        return button;
    }

}
