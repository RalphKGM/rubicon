package com.gabriel.draw.view;

import com.gabriel.draw.component.PropertySheet;
import com.gabriel.draw.controller.ActionController;
import com.gabriel.draw.controller.DrawingController;
import com.gabriel.draw.controller.DrawingWindowController;
import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.property.PropertyOptions;
import com.gabriel.draw.controller.PropertyEventListener;
import com.gabriel.property.event.PropertyEventAdapter;
import com.gabriel.property.property.Property;

import javax.swing.*;
import java.awt.*;
import com.gabriel.drawfx.command.CommandService;
 

public class DrawingFrame extends JFrame {

    Drawing drawing;
    DrawingAppService drawingAppService;
    AppService appService;
    DrawingFrame drawingFrame;
    Container pane;
    private PropertySheet propertySheet;
    ActionController actionListener;
    DrawingMenuBar drawingMenuBar;
    DrawingToolBar drawingToolBar;
    DrawingView drawingView;
    DrawingController drawingController;
    JScrollPane jScrollPane;
    DrawingStatusPanel drawingStatusPanel;
    DrawingWindowController drawingWindowController;
    public DrawingFrame() {

        drawing = new Drawing();
        drawingAppService = new DrawingAppService();
        appService = DrawingCommandAppService.getInstance(drawingAppService);

        pane = getContentPane();
        setLayout(new BorderLayout());

        actionListener = new ActionController(appService);
        actionListener.setFrame(this);
        drawingMenuBar = new DrawingMenuBar( actionListener);

        setJMenuBar(drawingMenuBar);

        drawingMenuBar.setVisible(true);


        drawingToolBar = new DrawingToolBar(actionListener);
        drawingToolBar.setVisible(true);

        drawingView = new DrawingView(appService);
        actionListener.setComponent(drawingView);


    drawingController = new DrawingController(appService, drawingView);
    drawingController.setDrawingView(drawingView);
        drawingView.setPreferredSize(new Dimension(3000, 6000));

        jScrollPane = new JScrollPane(drawingView);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    drawingStatusPanel = new DrawingStatusPanel();
    drawingController.setDrawingStatusPanel(drawingStatusPanel);

        pane.add(drawingToolBar, BorderLayout.PAGE_START);
        pane.add(jScrollPane, BorderLayout.CENTER );
        pane.add(drawingStatusPanel, BorderLayout.PAGE_END);

        drawingAppService.setDrawingView(drawingView);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500,500);

        drawingWindowController = new DrawingWindowController(appService);
        this.addWindowListener(drawingWindowController);
        this.addWindowFocusListener(drawingWindowController);
        this.addWindowStateListener(drawingWindowController);
    
    buildGUI(pane);
    
    drawingController.setPropertySheet(propertySheet);
    
    drawingView.addMouseMotionListener(drawingController);
    drawingView.addMouseListener(drawingController);

    
    setVisible(true);
        
        CommandService.addListener(() -> SwingUtilities.invokeLater(this::refreshPropertySheet));
    }

    public void buildGUI(Container pane){
        buildPropertyTable(pane);
        JScrollPane scrollPane = new JScrollPane(propertySheet);
        pane.add(scrollPane, BorderLayout.LINE_END);
        pack();
    }

    
    public void refreshPropertySheet() {
        if (propertySheet != null) {
            System.out.println("[DrawingFrame] refreshPropertySheet called");
            propertySheet.populateTable(appService);
        }
    }


    void buildPropertyTable(Container pane) {
    propertySheet = new PropertySheet(new PropertyOptions.Builder().build());
    
    propertySheet.addEventListener(new EventListener());
    
    propertySheet.addEventListener(new PropertyEventListener(appService));
    propertySheet.populateTable(appService);

        repaint();
    }

    class EventListener extends PropertyEventAdapter {
        @Override
    public void onPropertyUpdated(Property property) {
            System.out.println("[DrawingFrame.EventListener] property updated: " + property.getName() + " -> " + property.getValue());
            Shape shape  = appService.getSelectedShape();
            
            
            if (property.getName().equals("Current Shape")) {
                if (shape == null) {
                    appService.setShapeMode((ShapeMode) property.getValue());
                }
            }

            
            drawingView.repaint();
        }
    }
}

