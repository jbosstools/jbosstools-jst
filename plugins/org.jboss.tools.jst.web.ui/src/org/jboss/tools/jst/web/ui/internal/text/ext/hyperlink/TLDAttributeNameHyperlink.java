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
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.text.IRegion;
import org.jboss.tools.common.text.ext.hyperlink.JumpToHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jeremy
 *
 */
public class TLDAttributeNameHyperlink extends JumpToHyperlink {
	protected String getDestinationAxis() {
		return "/taglib/tag/attribute/name/"; //$NON-NLS-1$
	}
	
	protected NodeList getRootElementsToSearch (IRegion region) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;

			Node n = Utils.findNodeForOffset(xmlDocument, region.getOffset());

			Element tagElement = null;
			while(n != null && tagElement == null) {
				n = n.getParentNode();
				if (n instanceof Element && validAxis(n, "/taglib/tag/")) { //$NON-NLS-1$
					tagElement = (Element)n;
				}
			}
			if (tagElement == null)
				return null;
			
			return tagElement.getChildNodes();
		} finally {
			smw.dispose();
		}
	}
	
	private boolean validAxis(Node n, String validAxisEnding) {
		if (validAxisEnding == null || validAxisEnding.lastIndexOf('/') == -1) return false;
		StringTokenizer st = new StringTokenizer(validAxisEnding, "/"); //$NON-NLS-1$
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		if (tokens.size() == 0) return false;
		Node currentElement = n;
		for (int i = tokens.size() - 1; i >= 0; i--) {
			if (currentElement == null || !(currentElement instanceof Element))
				return false;
			String token = (String)tokens.get(i);
			if (!token.equals(currentElement.getNodeName()))
				return false;
			currentElement = currentElement.getParentNode();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		return Messages.BrowseToTLDAttributeNameDeclaration;
	}

}
