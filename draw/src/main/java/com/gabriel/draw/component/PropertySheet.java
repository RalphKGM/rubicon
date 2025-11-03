package com.gabriel.draw.component;

import com.gabriel.drawfx.ShapeMode;
import com.gabriel.property.PropertyOptions;
import com.gabriel.property.PropertyPanel;
import com.gabriel.property.cell.SelectionCellComponent;
import com.gabriel.property.property.*;
import com.gabriel.property.property.selection.Item;
import com.gabriel.property.property.selection.SelectionProperty;

import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PropertySheet extends PropertyPanel {
    PropertyPanel propertyTable;
    private SelectionProperty<ShapeMode> shapeProp;
    
    // Pre-create items for shape mode selection
    private final Item<ShapeMode> RectangleItem = new Item<>(ShapeMode.Rectangle, "Rectangle");
    private final Item<ShapeMode> EllipseItem = new Item<>(ShapeMode.Ellipse, "Ellipse");
    private final Item<ShapeMode> LineItem = new Item<>(ShapeMode.Line, "Line");
    private final Item<ShapeMode> TextItem = new Item<>(ShapeMode.Text, "Text");
    private final Item<ShapeMode> SelectItem = new Item<>(ShapeMode.Select, "Select");
    private final Item<ShapeMode> ImageItem = new Item<>(ShapeMode.Image, "Image");


    public void setShapeProp(ShapeMode shapeMode ){
        SelectionCellComponent  selectionComponent =  propertyTable.getSelectionCellComponent();
        if (shapeMode ==ShapeMode.Rectangle) {
            selectionComponent.setCellEditorValue(RectangleItem);
        } else if (shapeMode == ShapeMode.Ellipse) {
            selectionComponent.setCellEditorValue(EllipseItem);
        } else if (shapeMode == ShapeMode.Line) {
            selectionComponent.setCellEditorValue(LineItem);
        } else if (shapeMode == ShapeMode.Select) {
            selectionComponent.setCellEditorValue(SelectItem);
        } else if (shapeMode == ShapeMode.Image) {
            selectionComponent.setCellEditorValue(ImageItem);
        } else if (shapeMode == ShapeMode.Text) {
            selectionComponent.setCellEditorValue(TextItem);
        }
    }

    public PropertySheet(PropertyOptions options){
    super(options);
    shapeProp = new SelectionProperty<ShapeMode>(
        "Current Shape",
        new ArrayList<>(Arrays.asList(
            new Item<ShapeMode>(ShapeMode.Rectangle, "Rectangle"),
            new Item<ShapeMode>(ShapeMode.Ellipse, "Ellipse"),
            new Item<ShapeMode>(ShapeMode.Line, "Line"),
            new Item<ShapeMode>(ShapeMode.Text, "Text"),
            new Item<ShapeMode>(ShapeMode.Select, "Select")
        ))
    );
    }

    public void populateTable(AppService appService) {
        propertyTable = this;
        propertyTable.clear();
        Shape shape = appService.getSelectedShape();
        String objectType;
        ShapeMode currentMode = appService.getShapeMode();
        
        if (shape == null) {
            objectType = "Drawing";
        } else {
            // For any selected shape, show generic object type "Shape"
            objectType = "Shape";
            // Update currentMode based on the selected shape type so the "Current Shape" selector reflects it
            if (shape instanceof com.gabriel.draw.model.Image) {
                currentMode = ShapeMode.Image;
            } else if (shape instanceof com.gabriel.draw.model.Rectangle) {
                currentMode = ShapeMode.Rectangle;
            } else if (shape instanceof com.gabriel.draw.model.Ellipse) {
                currentMode = ShapeMode.Ellipse;
            } else if (shape instanceof com.gabriel.draw.model.Line) {
                currentMode = ShapeMode.Line;
            } else if (shape instanceof com.gabriel.draw.model.Text) {
                currentMode = ShapeMode.Text;
            }
        }

        // Add basic properties
        StringProperty targetProp = new StringProperty("Object Type", objectType);
        propertyTable.addProperty(targetProp);

        // Shape mode selection (single property)
        shapeProp = new SelectionProperty<ShapeMode>(
            "Current Shape",
            new ArrayList<>(Arrays.asList(
                RectangleItem,
                EllipseItem,
                LineItem,
                ImageItem,
                TextItem,
                SelectItem
            ))
        );
        propertyTable.addProperty(shapeProp);

        // Set selection editor to reflect current mode (based on selected shape or app state)
        setShapeProp(currentMode);
        shapeProp.setValue(currentMode);


        ColorProperty currentColorProp = new ColorProperty("Fore color", appService.getColor());
        propertyTable.addProperty(currentColorProp);

        ColorProperty currentFillProp = new ColorProperty("Fill color",  appService.getFill());
        propertyTable.addProperty(currentFillProp);

        ColorProperty currentStartColorProp = new ColorProperty("Start color",  appService.getStartColor());
        propertyTable.addProperty(currentStartColorProp);

        ColorProperty currentEndColorProp = new ColorProperty("End color",  appService.getEndColor());
        propertyTable.addProperty(currentEndColorProp);

    IntegerProperty startx = new IntegerProperty("Start x", appService.getStartX());
        propertyTable.addProperty(startx );

    IntegerProperty starty = new IntegerProperty("Start y", appService.getStarty());
        propertyTable.addProperty(starty );

    IntegerProperty endx = new IntegerProperty("End x", appService.getEndx());
        propertyTable.addProperty(endx );

    IntegerProperty endy = new IntegerProperty("End y", appService.getEndy());
        propertyTable.addProperty(endy );

        BooleanProperty isGradientProp = new BooleanProperty("IsGradient",  appService.isGradient() );
        propertyTable.addProperty(isGradientProp);

    BooleanProperty isVisibleProp = new BooleanProperty("IsVisible",  appService.isVisible() );
        propertyTable.addProperty(isVisibleProp);

        IntegerProperty lineThicknessProp = new IntegerProperty("Line Thickness", appService.getThickness());
        propertyTable.addProperty(lineThicknessProp);

        IntegerProperty xlocProp = new IntegerProperty("X Location", appService.getXLocation());
        propertyTable.addProperty(xlocProp);

        IntegerProperty ylocProp = new IntegerProperty("Y Location", appService.getYLocation());
        propertyTable.addProperty(ylocProp);

    IntegerProperty width = new IntegerProperty("Width", appService.getWidth());
        propertyTable.addProperty(width );

    IntegerProperty height = new IntegerProperty("Height", appService.getHeight());
        propertyTable.addProperty(height);

        

        StringProperty stringProp = new StringProperty("Text", appService.getText());
        propertyTable.addProperty(stringProp);

        stringProp = new StringProperty("Image", appService.getImageFilename());
        propertyTable.addProperty(stringProp);

        Font font = appService.getFont();

        stringProp = new StringProperty("Font family", font.getFamily());
        propertyTable.addProperty(stringProp);

        IntegerProperty intProp = new IntegerProperty("Font style", font.getStyle());
        propertyTable.addProperty(intProp);

        intProp = new IntegerProperty("Font size", font.getSize());
        propertyTable.addProperty(intProp);

    
    }
}
