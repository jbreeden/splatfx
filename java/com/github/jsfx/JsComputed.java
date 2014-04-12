package com.github.jsfx;

import java.util.Map;
import java.util.HashMap;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;

public class JsComputed extends ObjectBinding implements Computed {
    private boolean isEvaluating = false;
    private final Map<Observable, Boolean> bindings = new HashMap<>();
    private final JsCallback1 callback;
    
    public JsComputed(JsCallback1 callback){
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
        JsDependencyRegistrar registrar = JsDependencyRegistrar.instance();
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
        JsDependencyRegistrar registrar = JsDependencyRegistrar.instance();
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
