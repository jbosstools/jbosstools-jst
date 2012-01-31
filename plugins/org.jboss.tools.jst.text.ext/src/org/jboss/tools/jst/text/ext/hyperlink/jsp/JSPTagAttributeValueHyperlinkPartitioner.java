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
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkPartitionRecognizer;
import org.jboss.tools.common.text.ext.hyperlink.xml.XMLTagAttributeValueHyperlinkPartitioner;

/**
 * @author Jeremy
 */
public abstract class JSPTagAttributeValueHyperlinkPartitioner extends XMLTagAttributeValueHyperlinkPartitioner implements IHyperlinkPartitionRecognizer {
	protected String getAxis(IDocument document, int offset) {
		return JSPRootHyperlinkPartitioner.computeAxis(document, offset) + "/"; //$NON-NLS-1$
	}
}
