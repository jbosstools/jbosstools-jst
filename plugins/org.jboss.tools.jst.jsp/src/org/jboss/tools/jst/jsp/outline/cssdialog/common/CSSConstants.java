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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.BaseListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.CSSParser;
import org.xml.sax.Attributes;

/**
 * This class contains CSS constants
 * 
 * @author dsakovich@exadel.com
 */
public class CSSConstants {
	public static final String BACKGROUND_REPEAT = "background-repeat"; //$NON-NLS-1$
	public static final String BACKGROUND_COLOR = "background-color"; //$NON-NLS-1$
	public static final String BACKGROUND_IMAGE = "background-image"; //$NON-NLS-1$
	public static final String BORDER_STYLE = "border-style"; //$NON-NLS-1$
	public static final String BORDER_WIDTH = "border-width"; //$NON-NLS-1$
	public static final String WIDTH = "width"; //$NON-NLS-1$
	public static final String HEIGHT = "height"; //$NON-NLS-1$
	public static final String BORDER_COLOR = "border-color"; //$NON-NLS-1$
	public static final String MARGIN = "margin"; //$NON-NLS-1$
	public static final String PADDING = "padding"; //$NON-NLS-1$
	public static final String FONT_SIZE = "font-size"; //$NON-NLS-1$
	public static final String FONT_STYLE = "font-style"; //$NON-NLS-1$
	public static final String FONT_WEIGHT = "font-weight"; //$NON-NLS-1$
	public static final String TEXT_DECORATION = "text-decoration"; //$NON-NLS-1$
	public static final String TEXT_ALIGN = "text-align"; //$NON-NLS-1$
	public static final String FONT_FAMILY = "font-family"; //$NON-NLS-1$
	public static final String COLOR = "color"; //$NON-NLS-1$

	public static final String STYLE = "style"; //$NON-NLS-1$
	public static final String CLASS = "class"; //$NON-NLS-1$

	public static final Map<String, ArrayList<String>> CSS_STYLE_VALUES_MAP = new HashMap<String, ArrayList<String>>();

	public static final Map<String, ArrayList<String>> CSS_STYLES_MAP = new HashMap<String, ArrayList<String>>();

	public static Map<String, String> COLORS;

	private static final String CSS_VALUES_FILE = "cssdialog/cssElementsWithCombo.xml"; //$NON-NLS-1$
	private static final String CSS_STYLES_FILE = "cssdialog/cssElements.xml"; //$NON-NLS-1$
	private static final String COLORS_FILE = "cssdialog/color_properties.xml"; //$NON-NLS-1$

	private static final String NODE_NAME_ELEMENTS = "elements"; //$NON-NLS-1$
	private static final String NODE_NAME_VALUE = "value"; //$NON-NLS-1$
	private static final String NODE_NAME_ELEMENT = "element"; //$NON-NLS-1$
	private static final String NODE_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

	static {

		// initialize colors
		try {

			Properties properties = new Properties();

			InputStream is = JspEditorPlugin.getDefault().getBundle()
					.getResource(COLORS_FILE).openStream();
			properties.loadFromXML(is);

			is.close();
			COLORS = (Map) properties;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CSSParser parser = new CSSParser();
		parser.setListener(new BaseListener(CSS_STYLE_VALUES_MAP) {
			private ArrayList<String> list = null;

			public void startElement(String uri, String localName,
					String nodeName, Attributes attrs) {
				if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS)) {
					return;
				}

				if (!nodeName.trim().equalsIgnoreCase(NODE_NAME_VALUE)) {
					String name = nodeName;
					list = new ArrayList<String>();
					map.put(name, list);
				} else {
					list.add(attrs.getValue(NODE_ATTRIBUTE_NAME));
				}
			}
		});
		parser.parse(CSS_VALUES_FILE);

		parser.setListener(new BaseListener(CSS_STYLES_MAP) {
			private ArrayList<String> list = null;

			public void startElement(String uri, String localName,
					String nodeName, Attributes attrs) {
				if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS)) {
					return;
				}

				if (!nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENT)) {
					String name = nodeName;
					list = new ArrayList<String>();
					map.put(name, list);
				} else {
					list.add(attrs.getValue(NODE_ATTRIBUTE_NAME));
				}
			}
		});
		parser.parse(CSS_STYLES_FILE);

	}
}
