/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.text.ext.hyperlink.jsp;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkPartitionRecognizer;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Jeremy
 */
public class JSPXmlNsHyperlinkPartitioner extends AbstractHyperlinkPartitioner implements IHyperlinkPartitionRecognizer {
	public static final String JSP_XMLNS_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_XMLNS"; //$NON-NLS-1$

	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, int offset, IHyperlinkRegion superRegion) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			if (!(n instanceof IDOMAttr)) return null; 
			IDOMAttr xmlnsAttr = (IDOMAttr)n;
			if (xmlnsAttr.getName() == null || !xmlnsAttr.getName().startsWith("xmlns:")) return null; //$NON-NLS-1$
			Element rootElem = xmlnsAttr.getOwnerElement();
			if (!(rootElem.getNodeName().equals("jsp:root") || rootElem.getNodeName().equalsIgnoreCase("html"))) return null; //$NON-NLS-1$ //$NON-NLS-2$
			String xmlns = xmlnsAttr.getValueRegionText();
			String axis = getAxis(document, superRegion);
			String contentType = superRegion.getContentType();
			String type = JSP_XMLNS_PARTITION;
			int start = Utils.getValueStart(xmlnsAttr);
			int end = Utils.getValueEnd(xmlnsAttr);

			String text = document.get(start, end - start);
			StringBuffer sb = new StringBuffer(text);

			//find start and end of property value
			int bStart = 0;
			int bEnd = text.length() - 1;

			while (bStart < bEnd && (Character.isWhitespace(sb.charAt(bStart)) 
					|| sb.charAt(bStart) == '\"' || sb.charAt(bStart) == '\"')) { 
				bStart++;
			}
			while (bEnd > bStart && (Character.isWhitespace(sb.charAt(bEnd)) 
					|| sb.charAt(bEnd) == '\"' || sb.charAt(bEnd) == '\"')) { 
				bEnd--;
			}
			bEnd++;

			final int propStart = bStart + start;
			final int propLength = bEnd - bStart;
			
			if (propStart > offset || propStart + propLength < offset) return null;
			
			return new HyperlinkRegion(propStart, propLength, axis, contentType, type);
		} catch (BadLocationException e) {
			return null;
		} finally {
			smw.dispose();
		}
	}

	/**
	 * @see com.ibm.sse.editor.extensions.hyperlink.IHyperlinkPartitionRecognizer#recognize(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	public boolean recognize(IDocument document, int offset, IHyperlinkRegion region) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			if (!(n instanceof Attr)) return false; 
			Attr xmlnsAttr = (Attr)n;
			if (xmlnsAttr.getName() == null || !xmlnsAttr.getName().startsWith("xmlns:")) return false; //$NON-NLS-1$
			Element rootElem = xmlnsAttr.getOwnerElement();
			if (!(rootElem.getNodeName().equals("jsp:root") || rootElem.getNodeName().equalsIgnoreCase("html"))) return false; //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		} finally {
			smw.dispose();
		}
	}
		
	protected String getAxis(IDocument document, IHyperlinkRegion superRegion) {
		if (superRegion.getAxis() == null || superRegion.getAxis().length() == 0) {
			return JSPRootHyperlinkPartitioner.computeAxis(document, superRegion.getOffset()) + "/"; //$NON-NLS-1$
		}
		return superRegion.getAxis();
	}
}
