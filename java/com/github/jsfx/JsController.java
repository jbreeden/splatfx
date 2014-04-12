/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.jsfx;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author jared
 */
public class JsController {
    private JsFXMLLoader fxmlLoader;
    private JsCallback1 onSetFxmlLoaderCallback;
    private final Map<String, JsCallback1> methods = new HashMap<>();
    private final Map<String, JsController> nestedControllers = new HashMap<>();
    
    // Using `addMethod` instead of `methods.put` from JavaScript
    // allows the runtime (rhino or nashorn) to convert the callback function
    // to a JsCallback1 object
    public void addMethod(String name, JsCallback1 callback){
        methods.put(name, callback);
    }
    
    public boolean hasMethod(String name){
        return methods.containsKey(name);
    }
    
    public JsCallback1 getMethod(String name){
        return methods.get(name);
    }
    
    public Set<String> getMethodNames(){
        return methods.keySet();
    }
    
    public void addNestedController(String name, JsController controller){
        nestedControllers.put(name, controller);
    }
    
    public boolean hasNestedController(String name){
        return nestedControllers.containsKey(name);
    }
    
    public JsController getNestedController(String name){
        return nestedControllers.get(name);
    }
    
    public void onSetFxmlLoader(JsCallback1 callback){
        this.onSetFxmlLoaderCallback = callback;
    }
    
    public void setFxmlLoader(JsFXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
        if(null != onSetFxmlLoaderCallback){
            onSetFxmlLoaderCallback.call(this.fxmlLoader);
        }
    }
    
    public JsFXMLLoader getFxmlLoader(){
        return this.fxmlLoader;
    }
}
