/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc.
 *******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist.xpl;

import org.eclipse.jst.jsp.ui.internal.contentassist.JSPDummyContentAssistProcessor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;

/**
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public class JSPBaseContentAssistProcessor extends
		JSPDummyContentAssistProcessor {

	public JSPBaseContentAssistProcessor() {
		super();
	}

	public void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {
		IDOMNode node = (IDOMNode)contentAssistRequest.getNode();
		String tagName = node.getNodeName();
		
		
		// Find the attribute name for which this position should have a value
		IStructuredDocumentRegion open = node.getFirstStructuredDocumentRegion();
		ITextRegionList openRegions = open.getRegions();
		int i = openRegions.indexOf(contentAssistRequest.getRegion());
		if (i < 0) {
			return;
		}
		ITextRegion nameRegion = null;
		while(i >= 0) {
			nameRegion = openRegions.get(i--);
			if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
				break;
			}
		}

		String attributeName = null;
		if(nameRegion != null) {
			attributeName = open.getText(nameRegion);
		}

		String currentValue = null;
		int offset = -1;
		String strippedValue = null;
		if (contentAssistRequest.getRegion().getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {
			currentValue = contentAssistRequest.getText();
		} else {
			currentValue = ""; //$NON-NLS-1$
		}

		String matchString = null;
		
		// JBIDE-2334: Do not need any fix-ups here 
		
		// fixups
//		if (currentValue.length() > StringUtils.strip(currentValue).length() && (currentValue.startsWith("\"") || currentValue.startsWith("'")) && contentAssistRequest.getMatchString().length() > 0) {
//			matchString = currentValue.substring(0, contentAssistRequest.getMatchString().length());
//			strippedValue = currentValue;
//			offset = contentAssistRequest.getMatchString().length();
//	} else {
			matchString = currentValue.substring(0, contentAssistRequest.getMatchString().length());
			strippedValue = StringUtils.strip(currentValue);
			offset = contentAssistRequest.getMatchString().length();
//		}
		addFaceletAttributeValueProposals(
				contentAssistRequest,tagName,node, attributeName,matchString,strippedValue,offset,currentValue);
	}

	protected void addFaceletAttributeValueProposals(ContentAssistRequest contentAssistRequest, String tagName, IDOMNode node, String attributeName, String matchString, String strippedValue, int offset, String currentValue) {
		
	}
	
}
