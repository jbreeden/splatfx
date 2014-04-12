package com.github.jsfx;

import java.util.Set;
import java.util.HashSet;
import javafx.beans.Observable;

public class JsDependencyRegistrar {
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
    private static JsDependencyRegistrar instance = null;
    public static JsDependencyRegistrar instance() {
        if (instance == null){
            instance = new JsDependencyRegistrar();
        }
        return instance;
    }
}
