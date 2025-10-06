package com.gabriel.draw.controller;

import com.gabriel.draw.view.DrawingMenuBar;
import com.gabriel.draw.view.DrawingToolBar;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

public class DrawingWindowController implements WindowListener,
        WindowFocusListener,
        WindowStateListener {

    AppService appService;

    public DrawingWindowController(AppService appService, JFrame frame,
                                   DrawingMenuBar menuBar, DrawingToolBar toolBar) {
        this.appService = appService;
        frame.setJMenuBar(menuBar);
        frame.add(toolBar, java.awt.BorderLayout.NORTH);
        appService.registerMenuBar(menuBar);
        appService.registerToolBar(toolBar);
        appService.updateUndoRedoState();
        frame.addWindowListener(this);
        frame.addWindowFocusListener(this);
        frame.addWindowStateListener(this);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) { }

    @Override
    public void windowLostFocus(WindowEvent e) { }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) {
        appService.close();
    }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }

    @Override
    public void windowStateChanged(WindowEvent e) { }
}
