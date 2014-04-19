/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.crossfx;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author jared
 */
public class ControllerAdapter {
    private CrossFxmlLoader fxmlLoader;
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
    
    public void setFxmlLoader(CrossFxmlLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
        if(null != onSetFxmlLoaderCallback){
            onSetFxmlLoaderCallback.call(this.fxmlLoader);
        }
    }
    
    public CrossFxmlLoader getFxmlLoader(){
        return this.fxmlLoader;
    }
}
