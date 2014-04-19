package com.github.crossfx;

import javafx.beans.Observable;

public interface Computed {
    public Object getValue();
    public void addDependency(Observable observable);
}
