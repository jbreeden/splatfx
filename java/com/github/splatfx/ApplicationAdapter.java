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

import javafx.stage.Stage;

/**
 * ApplicationAdapter provides a callback-based alternative to extending
 * the javafx.application.Application class. This makes it easier for some
 * alternative JVM languages with poor inheritance utilities but simple SAM
 * conversion to construct and launch a JavaFX application.
 */
public class ApplicationAdapter extends javafx.application.Application {
  private static Callback1 onStartAction;
  
  public static void setOnStart(Callback1 onStartAction){
    ApplicationAdapter.onStartAction = onStartAction;
  }
  
  @Override
  public void start(Stage stage){
    onStartAction.call(stage);
  }
}