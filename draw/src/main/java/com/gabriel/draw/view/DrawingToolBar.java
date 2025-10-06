package com.gabriel.draw.view;

import com.gabriel.draw.controller.ActionController;
import com.gabriel.drawfx.command.ActionCommand;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;

public class DrawingToolBar extends JToolBar {

    private final JButton undoBtn;
    private final JButton redoBtn;
    private final JButton strokeBtn;
    private final JButton fillBtn;
    private final JButton lineBtn;
    private final JButton rectBtn;
    private final JButton ellipseBtn;
    private final JButton selectBtn;

    public DrawingToolBar(AppService appService, ActionController controller) {
        super("Tools");
        setFloatable(false);

        undoBtn = createButton("undo.png", ActionCommand.UNDO, controller);
        redoBtn = createButton("redo.png", ActionCommand.REDO, controller);
        strokeBtn = createButton("stroke.png", ActionCommand.COLOR, controller);
        fillBtn = createButton("fill.png", ActionCommand.FILL, controller);
        lineBtn = createButton("line.png", ActionCommand.LINE, controller);
        rectBtn = createButton("rectangle.png", ActionCommand.RECTANGLE, controller);
        ellipseBtn = createButton("ellipse.png", ActionCommand.ELLIPSE, controller);
        selectBtn = createButton("cursor.png", ActionCommand.SELECT, controller);

        add(selectBtn);
        add(undoBtn);
        add(redoBtn);
        addSeparator();
        add(strokeBtn);
        add(fillBtn);
        addSeparator();
        add(lineBtn);
        add(rectBtn);
        add(ellipseBtn);
        

        undoBtn.setEnabled(false);
        redoBtn.setEnabled(false);
    }

    private JButton createButton(String iconName, String cmd, ActionController controller) {
        JButton btn = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconName));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // scale icon
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText(cmd);
        }
        btn.setActionCommand(cmd);
        btn.addActionListener(controller);
        btn.setToolTipText(cmd);
        return btn;
    }

    public void enableUndo(boolean enable) {
        undoBtn.setEnabled(enable);
    }

    public void enableRedo(boolean enable) {
        redoBtn.setEnabled(enable);
    }
}
