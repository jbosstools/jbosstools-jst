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

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.w3c.dom.Node;

abstract public class AbstractXMLContentAssistProcessor extends AbstractContentAssistProcessor {
	private static final char[] PROPOSAL_AUTO_ACTIVATION_CHARS = new char[] {
		'<', '=', '"', '\'', '.'
	};
	
	private IDocument fDocument;
	private int fDocumentPosition;
	private ELContext fContext;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		this.fDocument = (viewer == null ? null : viewer.getDocument());
		this.fDocumentPosition = offset;
		this.fContext = createContext();
		
		System.out.println("AbstractXMLContentAssistProcessor: computeCompletionProposals() invoked");
		try {
			return super.computeCompletionProposals(viewer, offset);
		} finally {
			System.out.println("AbstractXMLContentAssistProcessor: computeCompletionProposals() exited");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		this.fDocument = (viewer == null ? null : viewer.getDocument());
		this.fDocumentPosition = offset;
		this.fContext = createContext();
		
		return super.computeContextInformation(viewer, offset);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
	 */
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return PROPOSAL_AUTO_ACTIVATION_CHARS;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#getContextInformationAutoActivationCharacters()
	 */
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return super.getContextInformationAutoActivationCharacters();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#getContextInformationValidator()
	 */
	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return super.getContextInformationValidator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage();
	}
	
	

	/* the methods to be overriden in derived classes */
	
	protected void addAttributeNameProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addAttributeNameProposals() invoked");
	}

	protected void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {

		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();

		// Find the attribute region and name for which this position should
		// have a value proposed
		IStructuredDocumentRegion open = node.getFirstStructuredDocumentRegion();
		ITextRegionList openRegions = open.getRegions();
		int i = openRegions.indexOf(contentAssistRequest.getRegion());
		if (i < 0) {
			return;
		}
		ITextRegion nameRegion = null;
		while (i >= 0) {
			nameRegion = openRegions.get(i--);
			if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
				break;
			}
		}

		// the name region is REQUIRED to do anything useful
		if (nameRegion != null) {
			// Retrieve the declaration
			CMElementDeclaration elementDecl = getCMElementDeclaration(node);

			// String attributeName = nameRegion.getText();
			String attributeName = open.getText(nameRegion);
			String currentValue = node.getAttributes().getNamedItem(attributeName).getNodeValue();
			String currentValueText = ((IDOMAttr)node.getAttributes().getNamedItem(attributeName)).getValueRegionText();
			ITextRegion currentValueRegion = ((IDOMAttr)node.getAttributes().getNamedItem(attributeName)).getValueRegion();
			
			
			
			ITextRegion invokeRegion = contentAssistRequest.getRegion();
			int pos = contentAssistRequest.getRegion().getStart();
			int replBegin = contentAssistRequest.getReplacementBeginPosition();
			int invokeRegionEnd = invokeRegion.getStart() + invokeRegion.getLength();
			IDOMAttr attrNode = (IDOMAttr)node.getAttributes().getNamedItem(attributeName);
			int valueRegionStartOffset = attrNode.getValueRegionStartOffset();
			ITextRegion eqRegion = attrNode.getEqualRegion();
			int eqRegionEnd= eqRegion.getStart() + eqRegion.getLength();
			int attrValueEnd=eqRegionEnd + currentValueText.length();
			int attrTextEnd = invokeRegion.getTextEnd();
			// attrNode.getValueRegionText()
			System.out.println("AbstractXMLContentAssistProcessor: addAttributeValueProposals() invoked");
		}
		else {
			setErrorMessage(UNKNOWN_CONTEXT);
		}
	}
	
	protected void addCommentProposal(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addCommentProposal() invoked");
	}
	
	protected void addDocTypeProposal(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addDocTypeProposal() invoked");
	}
	
	protected void addEmptyDocumentProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addEmptyDocumentProposals() invoked");
	}
	
	protected void addEndTagNameProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addEndTagNameProposals() invoked");
	}
	
	protected void addEndTagProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addEndTagProposals() invoked");
	}
	
	protected void addEntityProposals(ContentAssistRequest contentAssistRequest, int documentPosition, ITextRegion completionRegion, IDOMNode treeNode) {
		System.out.println("AbstractXMLContentAssistProcessor: addEntityProposals() invoked");
	}
	
	protected void addPCDATAProposal(String nodeName, ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addPCDATAProposal() invoked");
	}
	
	protected void addStartDocumentProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addStartDocumentProposals() invoked");
	}
	
	protected void addTagCloseProposals(ContentAssistRequest contentAssistRequest) {
		System.out.println("AbstractXMLContentAssistProcessor: addTagCloseProposals() invoked");
	}
	
	protected void addTagInsertionProposals(ContentAssistRequest contentAssistRequest, int childPosition) {
		System.out.println("AbstractXMLContentAssistProcessor: addTagInsertionProposals() invoked");
	}
	
	protected void addTagNameProposals(ContentAssistRequest contentAssistRequest, int childPosition) {
		System.out.println("AbstractXMLContentAssistProcessor: addTagNameProposals() invoked");
	}
	
	protected ContentAssistRequest computeCompletionProposals(int documentPosition, String matchString, ITextRegion completionRegion, IDOMNode treeNode, IDOMNode xmlnode) {
		ContentAssistRequest contentAssistRequest = super.computeCompletionProposals(documentPosition, matchString, completionRegion, treeNode, xmlnode);
		
		String regionType = completionRegion.getType();
		IStructuredDocumentRegion sdRegion = getStructuredDocumentRegion(documentPosition);

		/*
		 * Jeremy: Add attribute name proposals before  empty tag close
		 */
		if ((xmlnode.getNodeType() == Node.ELEMENT_NODE) || (xmlnode.getNodeType() == Node.DOCUMENT_NODE)) {
			if (regionType == DOMRegionContext.XML_EMPTY_TAG_CLOSE) {
				addAttributeNameProposals(contentAssistRequest);
			}
		}
		return contentAssistRequest;
	}
	
	abstract protected ELContext createContext();

	protected ELContext getContext() {
		return this.fContext;
	}
	
	protected int getOffset() {
		return this.fDocumentPosition;
	}
	
	protected IDocument getDocument() {
		return this.fDocument;
	}
	
	protected IFile getResource() {
		IStructuredModel sModel = StructuredModelManager.getModelManager().getExistingModelForRead(fTextViewer.getDocument());
		try {
			if (sModel != null) {
				String baseLocation = sModel.getBaseLocation();
				IPath location = new Path(baseLocation).makeAbsolute();
				IFile resource = FileBuffers.getWorkspaceFileAtLocation(location);
				return resource;
			}
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
		return null;
	}

	ELResolver[] getELResolvers(IResource resource) {
		ELResolverFactoryManager elrfm = ELResolverFactoryManager.getInstance();
		return elrfm.getResolvers(resource);
	}
}