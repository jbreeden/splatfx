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

import javafx.event.Event;
import javafx.event.EventHandler;

/*
 * Trivial EventHandler wrapper.
 * TODO: Can this be removed?
 * I believe this was helping one of the target JavaScript engines
 * at some point, but may have been refactored beyond the point of
 * uselessness.
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
