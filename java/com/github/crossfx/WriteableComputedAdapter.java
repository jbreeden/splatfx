/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.crossfx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;

public class WriteableComputedAdapter extends SimpleObjectProperty implements Computed {
    private final Callback1 writeCallback;
    private final ComputedAdapter readComputed;
    
    public WriteableComputedAdapter(Callback1 readCallback, Callback1 writeCallback){
        super(readCallback.call(null));
        this.writeCallback = writeCallback;
        this.readComputed = new ComputedAdapter(readCallback);
        
        this.readComputed.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                WriteableComputedAdapter.this.updateValue(newValue);
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
