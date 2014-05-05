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

package com.github.splatfx;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author jared
 */
public class ControllerAdapter {
    private SplatFxmlLoader fxmlLoader;
    private Callback1 onSetFxmlLoaderCallback;
    private final Map<String, Callback1> methods = new HashMap<>();
    private final Map<String, ControllerAdapter> nestedControllers = new HashMap<>();
    
    // Using `addMethod` instead of `methods.put` from JavaScript
    // allows the runtime (rhino or nashorn) to convert the callback function
    // to a Callback1 object
    public void addMethod(String name, Callback1 callback){
        methods.put(name, callback);
    }
    
    public boolean hasMethod(String name){
        return methods.containsKey(name);
    }
    
    public Callback1 getMethod(String name){
        return methods.get(name);
    }
    
    public Set<String> getMethodNames(){
        return methods.keySet();
    }
    
    public void addNestedController(String name, ControllerAdapter controller){
        nestedControllers.put(name, controller);
    }
    
    public boolean hasNestedController(String name){
        return nestedControllers.containsKey(name);
    }
    
    public ControllerAdapter getNestedController(String name){
        return nestedControllers.get(name);
    }
    
    public void onSetFxmlLoader(Callback1 callback){
        this.onSetFxmlLoaderCallback = callback;
    }
    
    public void setFxmlLoader(SplatFxmlLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
        if(null != onSetFxmlLoaderCallback){
            onSetFxmlLoaderCallback.call(this.fxmlLoader);
        }
    }
    
    public SplatFxmlLoader getFxmlLoader(){
        return this.fxmlLoader;
    }
}
