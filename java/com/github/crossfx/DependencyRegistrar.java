package com.github.crossfx;

import java.util.Set;
import java.util.HashSet;
import javafx.beans.Observable;

public class DependencyRegistrar {
    Set<Computed> listeners = new HashSet<Computed>();
    
    public void registerDependent(Computed computed){
        listeners.add(computed);
    }
    
    public void unregisterDependent(Computed computed) {
        listeners.remove(computed);
    }
    
    public void registerDependency(Observable observable) {
        for (Computed c : listeners) {
            c.addDependency(observable);
        }
    }
    
    // TODO: Ponder thread-safe implementation...
    private static DependencyRegistrar instance = null;
    public static DependencyRegistrar instance() {
        if (instance == null){
            instance = new DependencyRegistrar();
        }
        return instance;
    }
}
