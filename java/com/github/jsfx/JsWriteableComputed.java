/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.jsfx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;

public class JsWriteableComputed extends SimpleObjectProperty implements Computed {
    private final JsCallback1 writeCallback;
    private final JsComputed readComputed;
    
    public JsWriteableComputed(JsCallback1 readCallback, JsCallback1 writeCallback){
        super(readCallback.call(null));
        this.writeCallback = writeCallback;
        this.readComputed = new JsComputed(readCallback);
        
        this.readComputed.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                JsWriteableComputed.this.updateValue(newValue);
            }
        });
    }
    
    @Override
    public void setValue(Object newValue) {
        // Intercept setValue calls and pass argument through the write callback
        this.writeCallback.call(newValue);
    }
    
    @Override
    public void set(Object newValue) {
        // Intercept set calls and pass argument through the write callback
        this.writeCallback.call(newValue);
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
    
    @Override
    public void addDependency(Observable observable) {
        this.readComputed.addDependency(observable);
    }
    
    private void updateValue(Object newValue) {
        // When updateValue is called from the readComputed's change listener,
        // then we can update the perceived value of this computed through the
        // SimpleObjectProperty.set method
        super.set(newValue);
    }
}
