/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.outline.cssdialog.parsers;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Base listener
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class BaseListener extends DefaultHandler {

    protected HashMap<String, ArrayList<String>> map;

    /**
     * 
     * Constructor for listener
     * 
     * @param map
     *                Map for writing
     */
    public BaseListener(HashMap<String, ArrayList<String>> map) {
	this.map = map;
    }

    /**
     * 
     * Getter for map
     * 
     * @return Map
     */
    public HashMap<String, ArrayList<String>> getMap() {
	return map;
    }
}