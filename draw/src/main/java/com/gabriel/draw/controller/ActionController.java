package com.gabriel.draw.controller;

import com.gabriel.drawfx.command.ActionCommand;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionController implements ActionListener {

    private final AppService appService;

    public ActionController(AppService appService) {
        this.appService = appService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case ActionCommand.LINE -> appService.setShapeMode(ShapeMode.LINE);
            case ActionCommand.RECTANGLE -> appService.setShapeMode(ShapeMode.RECTANGLE);
            case ActionCommand.ELLIPSE -> appService.setShapeMode(ShapeMode.ELLIPSE);
            case ActionCommand.COLOR -> {
                Color chosen = JColorChooser.showDialog(null, "Choose stroke color", Color.BLACK);
                if (chosen != null) appService.setColor(chosen);
            }
            case ActionCommand.FILL -> {
                Color chosen = JColorChooser.showDialog(null, "Choose fill color", Color.WHITE);
                if (chosen != null) appService.setFill(chosen);
            }
            case ActionCommand.UNDO -> appService.undo();
            case ActionCommand.REDO -> appService.redo();
            case ActionCommand.SELECT -> appService.setShapeMode(ShapeMode.SELECT);
            case ActionCommand.MOVE -> appService.setShapeMode(ShapeMode.MOVE);
            case ActionCommand.SCALE -> appService.setShapeMode(ShapeMode.SCALE);

        }
    }
}
