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

import java.util.HashMap;

/**
 * 
 * ParserListener for input style string
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class ParserListener implements IListener {

    private HashMap<String, String> map;

    public ParserListener(HashMap<String, String> map) {
	this.map = map;
    }

    public void nextElement(String name, String value) {
	map.put(name, value);
    }

    public HashMap<String, String> getMap() {
	return map;
    }
}