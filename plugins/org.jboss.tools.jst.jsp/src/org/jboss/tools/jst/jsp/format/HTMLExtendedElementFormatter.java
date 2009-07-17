 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.format;

import java.util.ArrayList;

import org.eclipse.wst.html.core.internal.format.HTMLElementFormatter;
import org.eclipse.wst.html.core.internal.provisional.HTMLFormatContraints;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Formatter for html elements
 * Extends HTMLElementFormatter but doesn't format EL
 * @author Alexey Kazakov
 */
public class HTMLExtendedElementFormatter extends HTMLElementFormatter {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.html.core.internal.format.HTMLElementFormatter#formatNode(org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode, org.eclipse.wst.html.core.internal.provisional.HTMLFormatContraints)
	 */
	protected void formatNode(IDOMNode node, HTMLFormatContraints contraints) {
		if (node == null) {
			return;
		}
		IDOMElement element = (IDOMElement) node;
		String oldStyleValue = null;
		Attr style = element.getAttributeNode("style");//$NON-NLS-1$
		if (style != null) {
			oldStyleValue = style.getValue();
		}
		super.formatNode(node, contraints);
		if(oldStyleValue!=null && oldStyleValue.indexOf("#{")>-1) { //$NON-NLS-1$
			style.setValue(oldStyleValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.html.core.internal.format.HTMLFormatter#formatChildNodes(org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode, org.eclipse.wst.html.core.internal.provisional.HTMLFormatContraints)
	 */
	protected void formatChildNodes(IDOMNode node, HTMLFormatContraints contraints) {
		if (node == null || !node.hasChildNodes()) {
			return;
		}
		ArrayList<Attr> styles = new ArrayList<Attr>();
		ArrayList<String> oldValues = new ArrayList<String>();
		collectAllStyleAttributesOfNodeChildren(node, styles, oldValues);
		super.formatChildNodes(node, contraints);
		for(int i=0; i<styles.size(); i++) {
			Attr style = styles.get(i);
			String oldValue = oldValues.get(i);
			style.setNodeValue(oldValue);
		}
	}

	private void collectAllStyleAttributesOfNodeChildren(Node node, ArrayList<Attr> styles, ArrayList<String> oldValues) {
		if (!node.hasChildNodes()) {
			return;
		}
		NodeList children = node.getChildNodes();
		for(int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if(child instanceof Element) {
				Attr style = ((Element)child).getAttributeNode("style"); //$NON-NLS-1$
				if(style!=null) {
					String value = style.getValue();
					if(value.indexOf("#{")>-1) { //$NON-NLS-1$
						styles.add(style);
						oldValues.add(value);
					}
				}
			}
			collectAllStyleAttributesOfNodeChildren(child, styles, oldValues);
		}
	}
}