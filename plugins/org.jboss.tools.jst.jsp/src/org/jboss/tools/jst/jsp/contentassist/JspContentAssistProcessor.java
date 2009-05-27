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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.kb.taglib.TagLibriryManager;
import org.w3c.dom.Document;

public class JspContentAssistProcessor extends XmlContentAssistProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.XmlContentAssistProcessor#createContext()
	 */
	@Override
	protected IPageContext createContext() {
		ELContext superContext = super.createContext();
		
		IFile file = getResource();
		
		JspContextImpl context = new JspContextImpl();
		context.setResource(superContext.getResource());
		context.setElResolvers(superContext.getElResolvers());
		setVars(context);
		context.setResourceBundles(getResourceBundles());
		context.setDocument(getDocument());
		setNameSpaces(context);
		context.setLibraries(getTagLibraries(context));
		
		return context;
	}

	protected void setNameSpaces(JspContextImpl context) {
		IStructuredModel sModel = StructuredModelManager
									.getModelManager()
									.getExistingModelForRead(getDocument());
		try {
			if (sModel == null) 
				return;
			
			Document xmlDocument = (sModel instanceof IDOMModel) ? 
							((IDOMModel) sModel).getDocument() : 
								null;

			if (xmlDocument == null)
				return;

			TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(getDocument());
			List trackers = (manager == null? null : manager.getCMDocumentTrackers(getOffset()));
			for (int i = 0; trackers != null && i < trackers.size(); i++) {
				TaglibTracker tt = (TaglibTracker)trackers.get(i);
				final String prefix = tt.getPrefix();
				final String uri = tt.getURI();
				if (prefix != null && prefix.trim().length() > 0 &&
						uri != null && uri.trim().length() > 0) {
						
					IRegion region = new Region(0, getDocument().getLength());
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

			return;
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
	}

	private static final ITagLibrary[] EMPTY_LIBRARIES = new ITagLibrary[0];
	/**
	*/
	
	protected ITagLibrary[] getTagLibraries(IPageContext context) {
		Map<String, INameSpace> nameSpaces =  context.getNameSpaces(getOffset());
		if (nameSpaces == null || nameSpaces.isEmpty())
			return EMPTY_LIBRARIES;
		
		List<ITagLibrary> tagLibraries = new ArrayList<ITagLibrary>();
		for (INameSpace nameSpace : nameSpaces.values()) {
			ITagLibrary[] libs = TagLibriryManager.getLibraries(context.getResource().getProject(), nameSpace.getURI());
			if (libs != null && libs.length > 0) {
				for (ITagLibrary lib : libs) {
					tagLibraries.add(lib);
				}
			}
		} 
		return (tagLibraries.isEmpty() ? EMPTY_LIBRARIES :
				(ITagLibrary[])tagLibraries.toArray(new ITagLibrary[tagLibraries.size()]));
	}
	
	protected IResourceBundle[] getResourceBundles() {
		// TODO
		return null;
	}
	
	@Override
	protected IPageContext getContext() {
		return (IPageContext)super.getContext();
	}

	@Override
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		// TODO Auto-generated method stub
		System.out.println("JspContentAssistProcessor: addTagNameProposals() invoked");
		String matchString = contentAssistRequest.getMatchString();
		String query = matchString;
		if (query == null)
			query = "";
		if (query.indexOf(KbQuery.PREFIX_SEPARATOR) > -1) {
			query = matchString.substring(query.indexOf(KbQuery.PREFIX_SEPARATOR) + 1);
		}
		String stringQuery = "<" + matchString;
				
		KbQuery kbQuery = createKbQuery(Type.TAG_NAME, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());
		
		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			System.out.println("Tag Name proposal [" + (i + 1) + "/" + proposals.length + "]: " + textProposal.getReplacementString());
			
			String replacementString = textProposal.getReplacementString() + ">";
			
			int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
			int replacementLength = contentAssistRequest.getReplacementLength();
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = textProposal.getImage();
			String displayString = textProposal.getLabel();
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance() + 10000;
			
			
//			cursorAdjustment = proposedText.length() +
							// 1;
							// proposedText += "></" +
							// getRequiredName(parent, elementDecl) + ">";
							// //$NON-NLS-2$//$NON-NLS-1$
			
			
			CustomCompletionProposal proposal = new CustomCompletionProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
			contentAssistRequest.addProposal(proposal);
		}
		return;
	}

}