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

import javafx.beans.property.SimpleObjectProperty;

public class ObservableAdapter extends SimpleObjectProperty {
    
    public ObservableAdapter(Object initialValue){
        super(initialValue);
    }
    
    public ObservableAdapter(String initialValue){
        super(initialValue);
    }
    
    @Override
    public Object get(){
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.get();
    }
    
    @Override
    public Object getValue(){
        DependencyRegistrar registrar = DependencyRegistrar.instance();
        registrar.registerDependency(this);
        return super.getValue();
    }
}
