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
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * Parser for parse colors elements
 * 
 * --==Singleton==--
 * 
 * @author Evgeny Zheleznyakov
 *
 */
public class ColorParser {

    private SAXParserFactory fact;

    private SAXParser saxParser;
    
    private HashMap<String, String> map;
    
    private static ColorParser colorParser = null;
    
    private static final String NODE_NAME_COLOR = "color"; //$NON-NLS-1$

    private static final String NODE_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

    private static final String NODE_ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$
    
    private static final String FILE_NAME = "cssdialog/colors.xml"; //$NON-NLS-1$

    public static ColorParser getInstance() {
    	return ColorParserHolder.INSTANCE;
    }
    /**
     * Constructor for Color parser
     */
    private ColorParser() {

		try {
		    fact = SAXParserFactory.newInstance();
		    saxParser = fact.newSAXParser();
		} catch (ParserConfigurationException e) {
		    JspEditorPlugin.getPluginLog().logError(e);
		} catch (SAXException e) {
		    JspEditorPlugin.getPluginLog().logError(e);
		}

	    parse();
    }

    /**
     * Parse the content of the file specified as XML using the specified
     */
    private void parse() {
    	
		try {
		    InputStream is = JspEditorPlugin.getDefault().getBundle().getResource(FILE_NAME).openStream();
		    saxParser.parse(is, new DefaultHandler()
		    {
		        public void startElement(String uri, String localName, String nodeName,
		        	    Attributes attrs) {
	
		        	if (nodeName.trim().equalsIgnoreCase(NODE_NAME_COLOR)) {
		        	    getMap().put(attrs.getValue(NODE_ATTRIBUTE_NAME).trim().toUpperCase(),
		        		    attrs.getValue(NODE_ATTRIBUTE_VALUE).trim());
		        	}
		         }
		    });
		} catch (SAXException e) {
		    JspEditorPlugin.getPluginLog().logError(e);
		} catch (IOException e) {
		    JspEditorPlugin.getPluginLog().logError(e);
		}
    }
	/**
	 * @return the map
	 */
	public HashMap<String, String> getMap() {
		if(map==null) {
			map = new HashMap<String, String>();
		}
		return map;
	}
	
	private static class ColorParserHolder {
		private static final ColorParser INSTANCE = new ColorParser(); 
	}
}
