package com.gabriel.draw.service;

import com.gabriel.draw.command.ReplaceImageCommand;
import com.gabriel.draw.component.FileTypeFilter;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.command.CommandService;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ImageFileService {
    private String showImageFileChooser(String currentFilename) {
        String homeFolder;
        if(currentFilename == null) {
            homeFolder = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
        } else {
            File file = new File(currentFilename);
            homeFolder = file.getPath();
        }

        JFileChooser fileChooser = new JFileChooser(homeFolder);
        FileTypeFilter pngTypeFilter = new FileTypeFilter("png", "PNG Image Documents");
        FileTypeFilter jpgTypeFilter = new FileTypeFilter("jpg", "JPEG Image Documents");
        FileTypeFilter gifTypeFilter = new FileTypeFilter("gif", "GIF Image Documents");
        fileChooser.addChoosableFileFilter(pngTypeFilter);
        fileChooser.addChoosableFileFilter(jpgTypeFilter);
        fileChooser.addChoosableFileFilter(gifTypeFilter);
        
        int result = fileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void setImage(Drawing drawing) {
        String filename = showImageFileChooser(drawing.getImageFilename());
        if (filename != null) {
            drawing.setImageFilename(filename);
            drawing.setShapeMode(ShapeMode.Image);
        }
    }

    public void setImageForSelectedShape(Drawing drawing) {
        com.gabriel.draw.model.Image selectedImage = (com.gabriel.draw.model.Image)drawing.getSelectedShape();
        String prevFilename = selectedImage.getImageFilename();
        String newFilename = showImageFileChooser(prevFilename);
        if (newFilename != null && !newFilename.equals(prevFilename)) {
            ReplaceImageCommand cmd = new ReplaceImageCommand(selectedImage, prevFilename, newFilename, drawing);
            com.gabriel.drawfx.command.CommandService.ExecuteCommand(cmd);
        }
    }

}
