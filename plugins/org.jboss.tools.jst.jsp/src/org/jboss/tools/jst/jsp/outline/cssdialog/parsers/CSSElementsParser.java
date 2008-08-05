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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.xml.sax.SAXException;

public class CSSElementsParser {

    private String FILE_NAME = "cssdialog/cssElements.xml";

    private BaseListener listener;

    private SAXParserFactory fact;

    private SAXParser saxParser;

    public CSSElementsParser() {

	try {
	    fact = SAXParserFactory.newInstance();
	    saxParser = fact.newSAXParser();

	} catch (ParserConfigurationException e) {
	    JspEditorPlugin.getPluginLog().logError(e);
	} catch (SAXException e) {
	    JspEditorPlugin.getPluginLog().logError(e);
	}
    }

    public void parse() {
	try {
	    InputStream is = JspEditorPlugin.getDefault().getBundle().getResource(FILE_NAME).openStream();
	    saxParser.parse(is, listener);
	} catch (SAXException e) {
	    JspEditorPlugin.getPluginLog().logError(e);
	} catch (IOException e) {
	    JspEditorPlugin.getPluginLog().logError(e);
	}

    }

    public void setListener(BaseListener listener) {
	this.listener = listener;
    }
}