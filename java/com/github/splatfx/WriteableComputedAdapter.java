/*
 * Copyright (c) 2014 Jared Breeden
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  This particular file is
 * subject to the "Classpath" exception as provided in the LICENSE file
 * that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/*
 * EXPERIMENTAL & UNSUPPORTED
 * This will probably be removed in favor of other libraries
 * that already solve the data binding issue. splatFX will focus
 * on providing a convenient FXML Loader for alternative JVM languages.
 */

package com.github.splatfx;

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
