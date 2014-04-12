package com.github.jsfx;

import javafx.beans.property.SimpleObjectProperty;

public class JsObservable extends SimpleObjectProperty {
    
    public JsObservable(Object initialValue){
        super(initialValue);
    }
    
    public JsObservable(String initialValue){
        super(initialValue);
    }
    
    @Override
    public Object get(){
        JsDependencyRegistrar registrar = JsDependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.get();
    }
    
    @Override
    public Object getValue(){
        JsDependencyRegistrar registrar = JsDependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.getValue();
    }
}
