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
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.jsp;

import org.eclipse.jface.text.IDocument;
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
public class JSPTaglibHyperlinkPartitioner extends JSPElementAttributeValueHyperlinkPartitioner implements IHyperlinkPartitionRecognizer {
	public static final String JSP_TAGLIB_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_TAGLIB"; //$NON-NLS-1$

	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, int offset, IHyperlinkRegion superRegion) {
		if(document == null || superRegion == null) return null;
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(document);
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			if (n instanceof Attr) n = ((Attr)n).getOwnerElement();
			if (!(n instanceof Element)) return null;
			if (!"jsp:directive.taglib".equals(n.getNodeName())) return null; //$NON-NLS-1$

			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);
			if(start < 0) return null;
			
			String axis = getAxis(document, offset);
			String contentType = superRegion.getContentType();
			String type = JSP_TAGLIB_PARTITION;
			
			return new HyperlinkRegion(start, end - start, axis, contentType, type);
		} finally {
			smw.dispose();
		}
	}

	/**
	 * @see com.ibm.sse.editor.extensions.hyperlink.IHyperlinkPartitionRecognizer#recognize(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	public boolean recognize(IDocument document, int offset, IHyperlinkRegion region) {
		if(document == null || region == null) return false;
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(document);
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			if (n instanceof Attr) n = ((Attr)n).getOwnerElement();
			if (!(n instanceof Element)) return false;
			return ("jsp:directive.taglib".equals(n.getNodeName())); //$NON-NLS-1$
		} finally {
			smw.dispose();
		}
	}
}
