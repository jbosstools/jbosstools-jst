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
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.xml.XMLLinkHyperlinkPartitioner;

/**
 * @author Jeremy
 */
public class JSPLinkHyperlinkPartitioner extends XMLLinkHyperlinkPartitioner {
	public static final String JSP_LINK_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_LINK"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.common.text.ext.hyperlink.XMLLinkHyperlinkPartitioner#getPartitionType()
	 */
	protected String getPartitionType() {
		return JSP_LINK_PARTITION;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sse.editor.extensions.hyperlink.IHyperlinkPartitionRecognizer#recognize(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	public boolean recognize(IDocument document, int offset, IHyperlinkRegion region) {
		return true;
	}

	protected String getAxis(IDocument document, int offset) {
		return JSPRootHyperlinkPartitioner.computeAxis(document, offset) + "/"; //$NON-NLS-1$
	}
}
