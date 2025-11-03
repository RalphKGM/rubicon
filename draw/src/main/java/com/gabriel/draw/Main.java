package com.gabriel.draw;

import com.gabriel.draw.controller.ActionController;
import com.gabriel.draw.view.*;
import com.gabriel.draw.service.DrawingCommandAppService;
import com.gabriel.draw.service.DrawingAppService;
import com.gabriel.draw.controller.DrawingController;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Show splash screen first. The Splash panel will create the main DrawingFrame when the
        // user clicks the "GoDraw" panel. This keeps startup UX consistent with expectations.
        javax.swing.SwingUtilities.invokeLater(() -> {
            JWindow splashWindow = new JWindow();
            com.gabriel.draw.view.Splash splashPanel = new com.gabriel.draw.view.Splash();
            splashWindow.getContentPane().add(splashPanel);
            // Pack and size the splash to full-screen so it doesn't appear in Alt-Tab and supports per-window opacity.
            splashWindow.pack();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            splashWindow.setBounds(0, 0, screen.width, screen.height);
            splashWindow.setLocationRelativeTo(null);
            splashWindow.setVisible(true);

            // Do not auto-open the main frame; the Splash panel handles opening the DrawingFrame
            // when the user clicks the GoDraw control. This keeps the splash interactive.
        });
    }
}