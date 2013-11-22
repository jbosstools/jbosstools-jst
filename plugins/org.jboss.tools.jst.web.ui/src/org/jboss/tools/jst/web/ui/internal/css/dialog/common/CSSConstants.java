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
package org.jboss.tools.jst.web.ui.internal.css.dialog.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.css.dialog.parsers.BaseListener;
import org.jboss.tools.jst.web.ui.internal.css.dialog.parsers.CSSParser;
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

	public static final Map<String, ArrayList<String>> CSS_STYLE_VALUES_MAP = new HashMap<String, ArrayList<String>>();

	public static final Map<String, ArrayList<String>> CSS_STYLES_MAP = new HashMap<String, ArrayList<String>>();

	public static Map<String, String> COLORS_BY_RGB;
	public static Map<String, String> COLORS_BY_NAME;

	private static final String CSS_VALUES_FILE = "resources/cssdialog/cssElementsWithCombo.xml"; //$NON-NLS-1$
	private static final String CSS_STYLES_FILE = "resources/cssdialog/cssElements.xml"; //$NON-NLS-1$
	private static final String COLORS_FILE = "resources/cssdialog/color_properties.xml"; //$NON-NLS-1$

	private static final String NODE_NAME_ELEMENTS = "elements"; //$NON-NLS-1$
	private static final String NODE_NAME_VALUE = "value"; //$NON-NLS-1$
	private static final String NODE_NAME_ELEMENT = "element"; //$NON-NLS-1$
	private static final String NODE_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

	static {

		// initialize colors
		try {

			Properties properties = new Properties();

			InputStream is = WebUiPlugin.getDefault().getBundle()
					.getResource(COLORS_FILE).openStream();
			properties.loadFromXML(is);

			is.close();
			COLORS_BY_RGB = (Map) properties;
			COLORS_BY_NAME = new TreeMap<String, String>();
			for (String rgb: COLORS_BY_RGB.keySet()) {
				String name = COLORS_BY_RGB.get(rgb);
				COLORS_BY_NAME.put(name.toLowerCase(), rgb);
			}
			

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
