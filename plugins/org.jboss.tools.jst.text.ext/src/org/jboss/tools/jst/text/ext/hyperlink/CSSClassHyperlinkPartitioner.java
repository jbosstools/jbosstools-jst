/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
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
 * 
 * @author Sergey Dzmitrovich
 *
 */
public class CSSClassHyperlinkPartitioner extends
		AbstractHyperlinkPartitioner implements IHyperlinkPartitionRecognizer {

	public static final String CSS_CLASS_PARTITION = "org.jboss.tools.common.text.ext.CSS_CLASS"; //$NON-NLS-1$
	private static final String CSS_CLASS_TOKEN = "/class/"; //$NON-NLS-1$
	private static final String EXCLUSION_TOKEN = "jsp:usebean/class/"; //$NON-NLS-1$

	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument,
	 *      com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, int offset,
			IHyperlinkRegion superRegion) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null)
				return null;

			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			if (n == null || !(n instanceof Attr))
				return null;

			String axis = getAxis(document, offset);
			String contentType = superRegion.getContentType();
			String type = getPartitionType(axis);

			IRegion r = getRegion(document, offset);
			if (r == null)
				return null;

			return new HyperlinkRegion(r.getOffset(), r.getLength(), axis, contentType, type);
		} finally {
			smw.dispose();
		}
	}

	protected String getPartitionType(String axis) {
		return CSS_CLASS_PARTITION;
	}

	public boolean recognize(IDocument document, int offset, IHyperlinkRegion region) {
		if (region.getAxis() != null
				&& region.getAxis().toLowerCase().endsWith(CSS_CLASS_TOKEN) 
				&& !region.getAxis().toLowerCase().endsWith(EXCLUSION_TOKEN)) // Fix for JBIDE-5056
			return true;
		return false;
	}

	/**
	 * 
	 * @param offset
	 * @return
	 */
	protected IRegion getRegion(IDocument document, int offset) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null)
				return null;

			Node n = Utils.findNodeForOffset(xmlDocument, offset);

			if (n == null || !(n instanceof Attr))
				return null;
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);
			if (start > offset)
				return null;

			String attrText = document.get(start, end - start);

			StringBuffer sb = new StringBuffer(attrText);
			// find start of css class
			int bStart = offset - start;
			while (bStart >= 0) {
				if (!Character.isJavaIdentifierPart(sb.charAt(bStart))
						&& sb.charAt(bStart) != '_' && sb.charAt(bStart) != '-'
						&& sb.charAt(bStart) != '.') {
					bStart++;
					break;
				}

				if (bStart == 0)
					break;
				bStart--;
			}
			// find end of css class
			int bEnd = offset - start;
			while (bEnd < sb.length()) {
				if (!Character.isJavaIdentifierPart(sb.charAt(bEnd))
						&& sb.charAt(bEnd) != '_' && sb.charAt(bEnd) != '-'
						&& sb.charAt(bEnd) != '.')
					break;
				bEnd++;
			}

			final int propStart = bStart + start;
			final int propLength = bEnd - bStart;

			if (propStart > offset || propStart + propLength < offset)
				return null;
			return new Region(propStart, propLength);
		} catch (BadLocationException x) {
			// ignore
			return null;
		} finally {
			smw.dispose();
		}
	}
}
