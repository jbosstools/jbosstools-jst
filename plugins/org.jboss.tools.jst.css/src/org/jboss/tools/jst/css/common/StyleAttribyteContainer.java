/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.common;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.w3c.dom.Element;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class StyleAttribyteContainer extends StyleContainer {

	public static final String STYLE_ATTRIBUTE_NAME = "style"; //$NON-NLS-1$

	private Element element;

	public StyleAttribyteContainer(Element element) {
		this.element = element;
	}

	public void applyStyleAttributes(Map<String, String> attributes) {

		StringBuffer buf = new StringBuffer();
		for (Map.Entry<String, String> me : attributes.entrySet()) {

			if ((me.getValue() != null) && (me.getValue().length() != 0))
				buf.append(me.getKey() + Constants.COLON + me.getValue()
						+ Constants.SEMICOLON);
		}

		element.setAttribute(STYLE_ATTRIBUTE_NAME, buf.toString());

	}

	public Map<String, String> getStyleAttributes() {

		String styleString = element.getAttribute(STYLE_ATTRIBUTE_NAME);

		Map<String, String> styleMap = new HashMap<String, String>();

		if ((styleString != null) && (styleString.length() > 0)) {

			String[] styles = styleString.split(Constants.SEMICOLON);
			for (String styleElement : styles) {
				String[] styleElementParts = styleElement.trim().split(
						Constants.COLON);
				if ((styleElementParts != null)
						&& (styleElementParts.length == 2)
						&& Util.searchInElement(styleElementParts[0],
								CSSConstants.CSS_STYLES_MAP)) {

					styleMap.put(styleElementParts[0], styleElementParts[1]);
				}
			}

		}

		return styleMap;
	}

	public Object getStyleObject() {
		return element;
	}

}
