package com.gabriel.property.event;

import com.gabriel.property.property.Property;


public interface PropertyEventListener {

    
    public void onPropertyUpdated(Property property);

    
    public void onPropertyAdded(Property property);
}
