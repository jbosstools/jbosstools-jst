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
package org.jboss.tools.jst.web.ui.internal.css.common;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.jboss.tools.jst.web.ui.internal.css.dialog.common.CSSConstants;
import org.jboss.tools.jst.web.ui.internal.css.dialog.common.Util;
import org.jboss.tools.jst.web.ui.internal.editor.util.Constants;
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
		return CSSStyleManager.getStyleAttributes(element
				.getAttribute(STYLE_ATTRIBUTE_NAME));
	}

	public Object getStyleObject() {
		return element;
	}

	@Override
	public void addNodeListener(INodeAdapter adapter) {
		addNodeAdapter(element, adapter);
		
	}

	@Override
	public void removeNodelListener(INodeAdapter adapter) {
		removeNodeAdapter(element, adapter);
		
	}

}
