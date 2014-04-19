/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.crossfx;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author jared
 */
public class EventHandlerAdapter implements EventHandler{

    private final Callback1 callback;
    
    public EventHandlerAdapter(Callback1 callback){
        this.callback = callback;
    }
    
    @Override
    public void handle(Event t) {
        callback.call(t);
    }
}
