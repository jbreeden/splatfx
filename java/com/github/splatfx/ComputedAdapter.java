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

import java.util.Map;
import java.util.HashMap;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;

public class ComputedAdapter extends ObjectBinding implements Computed {
    private boolean isEvaluating = false;
    private final Map<Observable, Boolean> bindings = new HashMap<>();
    private final Callback1 callback;
    
    public ComputedAdapter(Callback1 callback){
        this.callback = callback;
        this.computeValue();
    }
    
    @Override
    protected final Object computeValue() {
        if (isEvaluating) {
            System.err.println("JSFX Error: Cycle detected in dependency graph.");
            return null;
        }
        isEvaluating = true;
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        markAllDependenciesInactive();
        registrar.registerDependent(this);
        Object result = callback.call(null);
        registrar.unregisterDependent(this);
        unbindFromInactiveDependencies();
        isEvaluating = false;
        return result;
    }
    
    @Override
    public Object getValue() {
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.getValue();
    }
    
    @Override
    public void addDependency(Observable observable) {
        if(observable == this) return;
        if(!bindings.keySet().contains(observable)){
            // Must put the observable in the bindings list before binding
            // to avoid infinite recursion
            // (Calling bind triggers a getValue, 
            //  which registers a dependency, 
            //  which calls addDependency, 
            //  which calls bind...)
            bindings.put(observable, true);
            this.bind(observable);
        } else {
            bindings.put(observable, true);
        }
    }
    
    private void markAllDependenciesInactive() {
        Object[] currentBindings = bindings.keySet().toArray();
        for (Object binding : currentBindings) {
            bindings.put((Observable)binding, false);
        }
    }
    
    private void unbindFromInactiveDependencies() {
        Object[] currentBindings = bindings.keySet().toArray();
        for (Object observable : currentBindings) {
            Boolean isActive = bindings.get((Observable)observable);
            if (null != isActive && false == isActive) {
                unbind((Observable)observable);
                bindings.remove((Observable)observable);
            }
        }
    }
}
