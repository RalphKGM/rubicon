package com.gabriel.draw.view;

import com.gabriel.drawfx.command.ActionCommand;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DrawingMenuBar extends JMenuBar {

    private final JMenuItem lineMenuItem = new JMenuItem("Line");
    private final JMenuItem rectangleMenuItem = new JMenuItem("Rectangle");
    private final JMenuItem ellipseMenuItem = new JMenuItem("Ellipse");
    private final JMenuItem undoMenuItem = new JMenuItem("Undo");
    private final JMenuItem redoMenuItem = new JMenuItem("Redo");
    private final JMenuItem colorMenuItem = new JMenuItem("Color");
    private final JMenuItem fillMenuItem = new JMenuItem("Fill");
    private final JMenuItem selectMenuItem = new JMenuItem("Select");

    public DrawingMenuBar(ActionListener actionListener, JToolBar toolBar) {

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        add(editMenu);

        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoMenuItem.setActionCommand(ActionCommand.UNDO);
        undoMenuItem.addActionListener(actionListener);
        undoMenuItem.setEnabled(false); 
        editMenu.add(undoMenuItem);

        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        redoMenuItem.setActionCommand(ActionCommand.REDO);
        redoMenuItem.addActionListener(actionListener);
        redoMenuItem.setEnabled(false); 
        editMenu.add(redoMenuItem);

        JCheckBoxMenuItem showToolbar = new JCheckBoxMenuItem("Show Toolbar", true);
        showToolbar.addActionListener(e -> toolBar.setVisible(showToolbar.isSelected()));
        editMenu.add(showToolbar);

        JMenu drawMenu = new JMenu("Draw");
        drawMenu.setMnemonic(KeyEvent.VK_D);
        add(drawMenu);

        lineMenuItem.setActionCommand(ActionCommand.LINE);
        lineMenuItem.addActionListener(actionListener);
        drawMenu.add(lineMenuItem);

        rectangleMenuItem.setActionCommand(ActionCommand.RECTANGLE);
        rectangleMenuItem.addActionListener(actionListener);
        drawMenu.add(rectangleMenuItem);

        ellipseMenuItem.setActionCommand(ActionCommand.ELLIPSE);
        ellipseMenuItem.addActionListener(actionListener);
        drawMenu.add(ellipseMenuItem);

        JMenu propMenu = new JMenu("Properties");
        propMenu.setMnemonic(KeyEvent.VK_P);
        add(propMenu);

        colorMenuItem.setActionCommand(ActionCommand.COLOR);
        colorMenuItem.addActionListener(actionListener);
        propMenu.add(colorMenuItem);

        fillMenuItem.setActionCommand(ActionCommand.FILL);
        fillMenuItem.addActionListener(actionListener);
        propMenu.add(fillMenuItem);

        selectMenuItem.setActionCommand(ActionCommand.SELECT);
        selectMenuItem.addActionListener(actionListener);
        drawMenu.add(selectMenuItem);
    }

    public void enableUndo(boolean enable) {
        undoMenuItem.setEnabled(enable);
    }

    public void enableRedo(boolean enable) {
        redoMenuItem.setEnabled(enable);
    }
}
