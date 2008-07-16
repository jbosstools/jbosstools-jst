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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Color parser listener
 * 
 * @author ezheleznyakov
 * 
 */
public class ColorParserListener extends DefaultHandler {

    private static String NODE_NAME_COLOR = "color";

    private static String NODE_ATTRIBUTE_NAME = "name";

    private static String NODE_ATTRIBUTE_VALUE = "value";

    private static HashMap<String, String> map;

    public ColorParserListener() {
	map = new HashMap<String, String>();
    }

    public void startElement(String uri, String localName, String nodeName,
	    Attributes attrs) {

	if (nodeName.trim().equalsIgnoreCase(NODE_NAME_COLOR)) {
	    map.put(attrs.getValue(NODE_ATTRIBUTE_NAME).trim().toUpperCase(),
		    attrs.getValue(NODE_ATTRIBUTE_VALUE).trim());
	}
    }

    public static HashMap<String, String> getMap() {
	return map;
    }
}