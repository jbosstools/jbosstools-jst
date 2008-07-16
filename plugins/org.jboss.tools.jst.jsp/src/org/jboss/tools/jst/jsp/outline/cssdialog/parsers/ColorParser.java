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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

import org.xml.sax.SAXException;

/**
 * 
 * Parser for parse colors elements
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class ColorParser {

    private String FILE_NAME = "colors.xml";

    private ColorParserListener listener;

    private SAXParserFactory fact;

    private SAXParser saxParser;

    /**
     * Constructor for Color parser
     */
    public ColorParser() {

	try {
	    fact = SAXParserFactory.newInstance();
	    saxParser = fact.newSAXParser();

	} catch (ParserConfigurationException e) {
	    // TODO Evgeny Zheleznyakov remove all printStackTrace
	    e.printStackTrace();
	} catch (SAXException e) {
	    // TODO Evgeny Zheleznyakov remove all printStackTrace
	    e.printStackTrace();
	}
    }

    /**
     * Parse the content of the file specified as XML using the specified
     */
    public void parse() {

	String pluginPath = JspEditorPlugin.getPluginCSSDialogResourcePath();
	IPath pluginFile = new Path(pluginPath);
	String path = pluginFile.append(FILE_NAME).toFile().getAbsolutePath();
	File file = new File(path);
	if (!file.exists())
	    throw new RuntimeException();

	try {
	    saxParser.parse(file, listener);
	} catch (SAXException e) {
	    // TODO Evgeny Zheleznyakov remove all printStackTrace
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Evgeny Zheleznyakov remove all printStackTrace
	    e.printStackTrace();
	}

    }

    /**
     * Method for set listener
     * 
     * @param listener
     *                ColorParseListener
     */
    public void setListener(ColorParserListener listener) {
	this.listener = listener;
    }
}