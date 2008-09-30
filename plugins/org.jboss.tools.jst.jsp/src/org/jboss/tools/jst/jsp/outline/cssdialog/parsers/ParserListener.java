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

import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * 
 * ParserListener for input style string
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class ParserListener implements IListener {

    private StyleAttributes attributes;

    public ParserListener(StyleAttributes styleAttributes) {
	this.attributes = styleAttributes;
    }

    public void nextElement(String name, String value) {
	attributes.addAttribute(name, value);
    }

    public StyleAttributes getStyleAttributes() {
	return attributes;
    }
}