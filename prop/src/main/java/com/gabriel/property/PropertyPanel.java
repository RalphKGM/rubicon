package com.gabriel.property;

import com.gabriel.property.cell.*;
import com.gabriel.property.event.EventDispatcher;
import com.gabriel.property.event.PropertyEventListener;
import com.gabriel.property.property.*;
import com.gabriel.property.exception.PropertyNotSupportedException;
import com.gabriel.property.property.selection.SelectionProperty;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.List;

public class PropertyPanel extends JTable {
    private PropertyOptions options;
    private PropertyModel propertyModel;
    private EventDispatcher eventDispatcher;
    private List<AbstractCellComponent> cellComponents;
    private List<Property> properties;

    private SelectionCellComponent selectionCellComponent;
    
    public PropertyPanel(PropertyOptions options) {
        this.options = options;
        this.propertyModel = new PropertyModel(options.getHeaders());
        this.eventDispatcher = new EventDispatcher();
        this.cellComponents = new ArrayList<>();
        this.properties = new ArrayList<>();

        
        setModel(propertyModel);
        setRowHeight(options.getRowHeight());
        getTableHeader().setReorderingAllowed(false);
    }

    
    public void addProperty(Property property, AbstractCellComponent cellComponent) {
        propertyModel.addRow(new Object[]{property.getName(), property.getValue()});
        cellComponents.add(cellComponent);
        properties.add(property);
        cellComponent.init(options, eventDispatcher);
        eventDispatcher.dispatchPropertyAddedEvent(property);
    }

    
    public void addProperty(Property property) throws PropertyNotSupportedException {
        if (property instanceof IntegerProperty) {
            addProperty(property, new IntegerCellComponent((IntegerProperty) property));
        } else if (property instanceof LongProperty) {
            addProperty(property, new LongCellComponent((LongProperty) property));
        } else if (property instanceof DoubleProperty) {
            addProperty(property, new DoubleCellComponent((DoubleProperty) property));
        } else if (property instanceof FloatProperty) {
            addProperty(property, new FloatCellComponent((FloatProperty) property));
        } else if (property instanceof StringProperty) {
            addProperty(property, new StringCellComponent((StringProperty) property));
        } else if (property instanceof ColorProperty) {
            addProperty(property, new ColorCellComponent((ColorProperty) property));
        } else if (property instanceof BooleanProperty) {
            addProperty(property, new BooleanCellComponent((BooleanProperty) property));
        } else if (property instanceof SelectionProperty) {
            selectionCellComponent = new SelectionCellComponent((SelectionProperty) property);
            addProperty(property, selectionCellComponent);
        } else if (property instanceof ActionProperty) {
            addProperty(property, new ActionCellComponent((ActionProperty) property));
        } else {
            throw new PropertyNotSupportedException(property);
        }
    }

    
    public void removeProperty(Property property) {
        removeProperty(properties.indexOf(property));
    }

    
    public void removeProperty(int row) {
        cellComponents.remove(row);
        properties.remove(row);
        propertyModel.removeRow(row);
    }

    
    public void clear() {
        propertyModel.clear();
        cellComponents.clear();
        properties.clear();
    }

    
    public void addEventListener(PropertyEventListener eventListener) {
        eventDispatcher.addEventListener(eventListener);
    }

    
    public void removeEventListener(PropertyEventListener eventListener) {
        eventDispatcher.removeEventListener(eventListener);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (column == 1) {
            return cellComponents.get(row);
        }

        return super.getCellEditor(row, column);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 1) {
            return cellComponents.get(row);
        }

        return super.getCellRenderer(row, column);
    }
    public SelectionCellComponent getSelectionCellComponent(){
        return selectionCellComponent;
    }
}
