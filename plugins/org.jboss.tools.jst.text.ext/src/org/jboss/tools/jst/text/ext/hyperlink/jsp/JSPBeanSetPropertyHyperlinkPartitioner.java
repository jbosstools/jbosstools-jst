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

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkPartitionRecognizer;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Jeremy
 */
public class JSPBeanSetPropertyHyperlinkPartitioner extends AbstractHyperlinkPartitioner implements IHyperlinkPartitionRecognizer {
	public static final String JSP_BEAN_SET_PROPERTY_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_BEAN_SET_PROPERTY"; //$NON-NLS-1$

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
			if (n == null || !(n instanceof Attr)) return null;
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);

			String axis = getAxis(document, offset);
			String contentType = superRegion.getContentType();
			String type = JSP_BEAN_SET_PROPERTY_PARTITION;

			return new HyperlinkRegion(start, end - start, axis, contentType, type);
		} finally {
			smw.dispose();
		}
	}
	
	protected String getAxis(IDocument document, int offset) {
		return JSPRootHyperlinkPartitioner.computeAxis(document, offset) + "/"; //$NON-NLS-1$
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
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);
			
			if (start < 0 || start > offset|| end < offset) 
				return false;

			return true;
		} finally {
			smw.dispose();
		}
	}
}
