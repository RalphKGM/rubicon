package com.gabriel.draw.view;

import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;

public class DrawingFrame extends JFrame {
    private final DrawingView drawingView;

    public DrawingFrame(AppService appService, DrawingMenuBar menuBar, DrawingToolBar toolBar) {
        super("Drawing App");

        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.NORTH);

        drawingView = new DrawingView(appService);
        add(drawingView, BorderLayout.CENTER);

        setJMenuBar(menuBar);
        appService.registerMenuBar(menuBar);
        appService.registerToolBar(toolBar);
        appService.updateUndoRedoState();
    }

    public DrawingView getDrawingView() {
        return drawingView;
    }
}
