/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.github.splatfx.sun.javafx.fxml;

import java.net.URL;

/**
 * An element in a parse trace, as returned by
 * {@link com.github.splatfx.javafx.fxml.FXMLLoader#impl_getParseTrace()}.
 *
 * @treatAsPrivate
 * @since JavaFX 2.1
 */
public class ParseTraceElement {
    private URL location;
    private int lineNumber;

    public ParseTraceElement(URL location, int lineNumber) {
        this.location = location;
        this.lineNumber = lineNumber;
    }

    public URL getLocation() {
        return location;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return ((location == null) ? "?" : location.getPath()) + ": " + lineNumber;
    }
}
