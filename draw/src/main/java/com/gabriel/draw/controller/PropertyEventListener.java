package com.gabriel.draw.controller;

import com.gabriel.property.event.PropertyEventAdapter;
import com.gabriel.property.property.Property;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.draw.service.DrawingCommandAppService;
import java.awt.*;

public class PropertyEventListener extends PropertyEventAdapter {
    private final AppService appService;
    private final DrawingCommandAppService commandService;

    public PropertyEventListener(AppService appService) {
        this.appService = appService;
        // Get or create the command service wrapper
        this.commandService = (DrawingCommandAppService) DrawingCommandAppService.getInstance(appService);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void onPropertyUpdated(Property property) {
        System.out.println("[PropertyEventListener] property updated: " + property.getName() + " -> " + property.getValue());
        if (property.getName().equals("Fill color")) {
            commandService.setFill((Color) property.getValue());
        } else if (property.getName().equals("Fore color")) {
            commandService.setColor((Color) property.getValue());
        } else if (property.getName().equals("X Location")) {
            commandService.setXLocation((int) property.getValue());
        } else if (property.getName().equals("Y Location")) {
            commandService.setYLocation((int) property.getValue());
        } else if (property.getName().equals("Width")) {
            commandService.setWidth((int) property.getValue());
        } else if (property.getName().equals("Height")) {
            commandService.setHeight((int) property.getValue());
        } else if (property.getName().equals("Line Thickness")) {
            commandService.setThickness((int) property.getValue());
        } else if (property.getName().equals("Text")) {
            commandService.setText((String)property.getValue());
    } else if (property.getName().equals("Start color")) {
            commandService.setStartColor((Color) property.getValue());
        } else if (property.getName().equals("End color")) {
            commandService.setEndColor((Color) property.getValue());
        } else if (property.getName().equals("IsGradient")) {
            commandService.setIsGradient((Boolean) property.getValue());
        } else if (property.getName().equals("IsVisible")) {
            commandService.setIsVisible((Boolean) property.getValue());
        } else if (property.getName().equals("Start x")) {
            commandService.setStartX((int) property.getValue());
        } else if (property.getName().equals("Start y")) {
            commandService.setStarty((int) property.getValue());
        } else if (property.getName().equals("End x")) {
            commandService.setEndx((int) property.getValue());
        } else if (property.getName().equals("End y")) {
            commandService.setEndy((int) property.getValue());
        } else if (property.getName().equals("Font family")) {
            Font font = appService.getFont();
            Font newFont = new Font((String) property.getValue(), font.getStyle(), font.getSize());
            commandService.setFont(newFont);
        } else if (property.getName().equals("Font style")) {
            Font font = appService.getFont();
            Font newFont = new Font(font.getFamily(), (int)property.getValue(), font.getSize());
            commandService.setFont(newFont);
        } else if (property.getName().equals("Font size")) {
            Font font = appService.getFont();
            Font newFont = new Font(font.getFamily(), font.getStyle(), (int) property.getValue());
            commandService.setFont(newFont);
        }
    }
}