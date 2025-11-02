package com.gabriel.property.property;

import com.gabriel.property.validator.Validator;


public interface Property<T> {

    
    public String getName();

    
    public T getValue();

    
    public void setValue(T value);

    
    public Validator getValidator();
}
