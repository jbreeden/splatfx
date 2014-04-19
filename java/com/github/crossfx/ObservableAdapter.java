package com.github.crossfx;

import javafx.beans.property.SimpleObjectProperty;

public class ObservableAdapter extends SimpleObjectProperty {
    
    public ObservableAdapter(Object initialValue){
        super(initialValue);
    }
    
    public ObservableAdapter(String initialValue){
        super(initialValue);
    }
    
    @Override
    public Object get(){
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.get();
    }
    
    @Override
    public Object getValue(){
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.getValue();
    }
}
