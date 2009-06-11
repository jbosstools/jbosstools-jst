/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import java.util.Map;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IFaceletPageContext;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.FaceletPageContextImpl;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FaceletPageContectAssistProcessor extends JspContentAssistProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#createContext()
	 */
	@Override
	protected IPageContext createContext() {
		IPageContext superContext = super.createContext();
		
		
		FaceletPageContextImpl context = new FaceletPageContextImpl();
		context.setResource(superContext.getResource());
		context.setElResolvers(superContext.getElResolvers());
		setVars(context);

		context.setResourceBundles(superContext.getResourceBundles());
		context.setDocument(getDocument());
		setNameSpaces(superContext, context);
		context.setLibraries(getTagLibraries(context));
		
//		IFaceletPageContext getParentContext();
//		Map<String, String> getParams();

		return context;
	}

	protected void setNameSpaces(IPageContext superContext, FaceletPageContextImpl context) {
		IStructuredModel sModel = StructuredModelManager
									.getModelManager()
									.getExistingModelForRead(getDocument());
		if (superContext != null) {
			IRegion region = new Region (0, getDocument().getLength());
			Map<String, INameSpace> nameSpaces = superContext.getNameSpaces(getOffset());
			for (String prefix : nameSpaces.keySet()) {
				context.addNameSpace(region, nameSpaces.get(prefix));
			}
		}
			
		try {
			if (sModel == null)
				return;

			Document xmlDocument = (sModel instanceof IDOMModel) ? ((IDOMModel) sModel)
					.getDocument()
					: null;

			if (xmlDocument == null)
				return;

			Node n = findNodeForOffset(xmlDocument, getOffset());
			while (n != null) {
				if (!(n instanceof Element)) {
					if (n instanceof Attr) {
						n = ((Attr) n).getOwnerElement();
					} else {
						n = n.getParentNode();
					}
					continue;
				}

				NamedNodeMap attrs = n.getAttributes();
				for (int j = 0; attrs != null && j < attrs.getLength(); j++) {
					Attr a = (Attr) attrs.item(j);
					String name = a.getName();
					if (name.startsWith("xmlns:")) {
						final String prefix = name.substring("xmlns:".length());
						final String uri = a.getValue();
						if (prefix != null && prefix.trim().length() > 0 &&
								uri != null && uri.trim().length() > 0) {

							int start = ((IndexedRegion)n).getStartOffset();
							int length = ((IndexedRegion)n).getLength();
							
							IDOMElement domElement = (n instanceof IDOMElement ? (IDOMElement)n : null);
							if (domElement != null) {
								start = domElement.getStartOffset();
								length = (domElement.hasEndTag() ? 
											domElement.getEndStructuredDocumentRegion().getEnd() :
												domElement.getLength());
								
							}

							Region region = new Region(start, length);
							INameSpace nameSpace = new INameSpace(){
								public String getURI() {
									return uri.trim();
								}
								public String getPrefix() {
									return prefix.trim();
								}
							};
							context.addNameSpace(region, nameSpace);
						}
					}
				}

				n = n.getParentNode();
			}

			return;
		} finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
	}
	
	
	@Override
	protected IFaceletPageContext getContext() {
		return (IFaceletPageContext)super.getContext();
	}
	
	

	/**
	 * Calculates and adds the EL proposals to the Content Assist Request object
	 */
	@Override
	protected void addTextELProposals(ContentAssistRequest contentAssistRequest) {
		// TODO Auto-generated method stub
		System.out.println("FaceletPageContectAssistProcessor: addTextELProposals() invoked");
		try {
			String matchString = getELPrefix();
			String query = matchString;
			if (query == null)
				query = "";
			String stringQuery = matchString;
					
			KbQuery kbQuery = createKbQuery(Type.TEXT, query, stringQuery);
			TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());
			
			for (int i = 0; proposals != null && i < proposals.length; i++) {
				TextProposal textProposal = proposals[i];
				
				System.out.println("Tag Text EL proposal [" + (i + 1) + "/" + proposals.length + "]: " + textProposal.getReplacementString());
				
				String replacementString = textProposal.getReplacementString();
				
				int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
				int replacementLength = contentAssistRequest.getReplacementLength();
				int cursorPosition = getCursorPositionForProposedText(replacementString);
				Image image = textProposal.getImage();
				String displayString = (textProposal.getLabel() == null ? replacementString : textProposal.getLabel());
				IContextInformation contextInformation = null;
				String additionalProposalInfo = textProposal.getContextInfo();
				int relevance = textProposal.getRelevance() + 10000;
				
				CustomCompletionProposal proposal = new CustomCompletionProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
				contentAssistRequest.addProposal(proposal);
			}
		} finally {
			System.out.println("FaceletPageContectAssistProcessor: addTextELProposals() exited");
		}
	}

}
