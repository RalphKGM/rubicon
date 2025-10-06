
package com.gabriel.draw;

import com.gabriel.draw.controller.ActionController;
import com.gabriel.draw.controller.DrawingController;
import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.draw.view.DrawingMenuBar;
import com.gabriel.draw.view.DrawingToolBar;
import com.gabriel.draw.view.DrawingFrame;
import com.gabriel.draw.view.DrawingView;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AppService drawingAppService = new DrawingAppService();
        AppService appService = new DrawingCommandAppService(drawingAppService);

        ActionController actionController = new ActionController(appService);

        DrawingToolBar drawingToolBar = new DrawingToolBar(appService, actionController);
        DrawingMenuBar drawingMenuBar = new DrawingMenuBar(actionController, drawingToolBar);

        DrawingFrame drawingFrame = new DrawingFrame(appService, drawingMenuBar, drawingToolBar);

        DrawingView drawingView = drawingFrame.getDrawingView();
        DrawingController drawingController = new DrawingController(appService, drawingView);
        drawingView.addMouseListener(drawingController);
        drawingView.addMouseMotionListener(drawingController);

        drawingFrame.setSize(600, 500);
        drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawingFrame.setVisible(true);
    }
}
