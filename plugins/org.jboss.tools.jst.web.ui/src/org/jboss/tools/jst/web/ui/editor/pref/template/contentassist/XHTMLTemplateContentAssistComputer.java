/*******************************************************************************
 * Copyright (c) 2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.pref.template.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer;
import org.jboss.tools.jst.web.ui.editor.pref.template.TemplateContextTypeIdsXHTML;
import org.w3c.dom.Node;

/**
 * XHTML Content Assist Computer for Templates, a computer which adds templates
 * proposals to CA
 * 
 * @author Jeremy
 * 
 */
@SuppressWarnings("restriction")
public class XHTMLTemplateContentAssistComputer implements ICompletionProposalComputer {
	protected IPreferenceStore fPreferenceStore = null;
	protected boolean isXHTML = false;
	private XHTMLTemplateCompletionProcessor fTemplateProcessor = null;

	
	public XHTMLTemplateContentAssistComputer() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	public List computeCompletionProposals(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
		List<String> fContextTypes = getContentTypes(context.getViewer(), 
					context.getInvocationOffset());
  		addTemplates(context.getViewer(), result, fContextTypes,
				context.getInvocationOffset());
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List computeContextInformation(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {
		return new ArrayList();
	}

	public String getErrorMessage() {
		return null;
	}

	/**
	 * <p>default is to do nothing</p>
	 * 
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
		//default is to do nothing
	}

	/**
	 * <p>default is to do nothing</p>
	 * 
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#sessionStarted()
	 */
	public void sessionStarted() {
		//default is to do nothing
	}

	
	/**
	 * Function for culculation content type depending on position
	 * @param textViewer
	 * @param documentPosition
	 * @return List of content types
	 * 
	 * @author mareshkau
	 */
	private List<String> getContentTypes(ITextViewer textViewer, int documentPosition){
		List<String> fContextTypes = new ArrayList<String>();
		//this should be added in any case
		fContextTypes.add(TemplateContextTypeIdsXHTML.ALL);
		IDocument document = textViewer.getDocument();
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(document);
			Node node = (Node) model.getIndexedRegion(documentPosition);
			
			if(node==null||node.getNodeType()==Node.DOCUMENT_NODE){
				fContextTypes.add(TemplateContextTypeIdsXHTML.NEW);
			}
			//commented by Maksim Areshkau, because even on attribute we get type ELEMENT_NODE
//			else if(node.getNodeType()==Node.ATTRIBUTE_NODE){
//				fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE);
//				fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE_VALUE);
//			}
		else if(node.getNodeType()==Node.ELEMENT_NODE ||node.getNodeType()==Node.TEXT_NODE) {
				fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE);
				fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE_VALUE);
				fContextTypes.add(TemplateContextTypeIdsXHTML.TAG);
			}
			
		}
		finally{
			if (model != null) {
				model.releaseFromRead();
			}
		}
//		fContextTypes.add(TemplateContextTypeIdsXHTML.TAG);
//		fContextTypes.add(TemplateContextTypeIdsXHTML.NEW);
//		fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE);
//		fContextTypes.add(TemplateContextTypeIdsXHTML.ATTRIBUTE_VALUE);
		return fContextTypes;
	}

	/**
	 * Adds templates to the list of proposals
	 * 
	 * @param contentAssistRequest
	 * @param context
	 * @param startOffset
	 */
	private void addTemplates(ITextViewer fTextViewer,
			List<ICompletionProposal> contentAssistRequest,
			List<String> fTemplateContexts, int startOffset) {

		if (contentAssistRequest == null) {
			return;
		}

		// if already adding template proposals for a certain context type, do
		// not add again
		if (getTemplateCompletionProcessor() != null) {
			for (String context : fTemplateContexts) {
				getTemplateCompletionProcessor().setContextType(context);
				ICompletionProposal[] proposals = getTemplateCompletionProcessor()
						.computeCompletionProposals(fTextViewer, startOffset);
				for (int i = 0; i < proposals.length; ++i) {
					contentAssistRequest.add(proposals[i]);
				}
			}
		}
	}

	private XHTMLTemplateCompletionProcessor getTemplateCompletionProcessor() {
		if (this.fTemplateProcessor == null) {
			this.fTemplateProcessor = new XHTMLTemplateCompletionProcessor();
		}
		return this.fTemplateProcessor;
	}

}
