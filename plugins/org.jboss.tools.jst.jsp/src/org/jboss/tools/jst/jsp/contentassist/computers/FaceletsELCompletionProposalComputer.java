/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.contentassist.computers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLCMDocument;
import org.eclipse.wst.html.core.internal.provisional.HTMLCMProperties;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.ssemodelquery.ModelQueryAdapter;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.jboss.tools.common.el.core.ca.ELTextProposal;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor.TextRegion;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.contentassist.AutoELContentAssistantProposal;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.kb.IFaceletPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * EL Proposal computer for XHTML pages
 * 
 * @author Jeremy
 *
 */
@SuppressWarnings("restriction")
public class FaceletsELCompletionProposalComputer extends JspELCompletionProposalComputer {
	private static final String JSFC_ATTRIBUTE_NAME = "jsfc"; //$NON-NLS-1$
	private boolean replaceJsfcTags;

	/** <code>true</code> if the document the proposal request is on is XHTML */
	protected boolean isXHTML = false;
	
	/**
	 * <p>Determine if the document is XHTML or not, then compute the proposals</p>
	 * @TODO: move the XHTML determination to XHTML computer
	 * 
	 * @see org.eclipse.wst.xml.ui.internal.contentassist.AbstractXMLCompletionProposalComputer#computeCompletionProposals(org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("rawtypes")
	public List computeCompletionProposals(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {

		try {
			//determine if the content is XHTML or not
			IndexedRegion treeNode = ContentAssistUtils.getNodeAt(context.getViewer(),
					context.getInvocationOffset());
			IDOMNode node = (IDOMNode) treeNode;
			boolean isXHTMLNode = isXHTMLNode(node);
			if(this.isXHTML != isXHTMLNode) {
				this.isXHTML = isXHTMLNode;
			}
			
			//compute the completion proposals
			return super.computeCompletionProposals(context, monitor);
		} finally {
			if(!keepState) {
				fCurrentContext = null;
			}
		}
	}

	/**
	 * Determine if this Document is an XHTML Document. Operates solely off of
	 * the Document Type declaration
	 */
	@SuppressWarnings("deprecation")
	private static boolean isXHTMLNode(Node node) {
		if (node == null) {
			return false;
		}

		Document doc = null;
		if (node.getNodeType() != Node.DOCUMENT_NODE)
			doc = node.getOwnerDocument();
		else
			doc = ((Document) node);

		if (doc instanceof IDOMDocument) {
			return ((IDOMDocument) doc).isXMLType();
		}

		if (doc instanceof INodeNotifier) {
			ModelQueryAdapter adapter = (ModelQueryAdapter) ((INodeNotifier) doc).getAdapterFor(ModelQueryAdapter.class);
			CMDocument cmdoc = null;
			if (adapter != null && adapter.getModelQuery() != null)
				cmdoc = adapter.getModelQuery().getCorrespondingCMDocument(doc);
			if (cmdoc != null) {
				// treat as XHTML unless we've got the in-code HTML content
				// model
				if (cmdoc instanceof HTMLCMDocument)
					return false;
				if (cmdoc.supports(HTMLCMProperties.IS_XHTML))
					return Boolean.TRUE.equals(cmdoc.getProperty(HTMLCMProperties.IS_XHTML));
			}
		}
		// this should never be reached
		DocumentType docType = doc.getDoctype();
		return docType != null && docType.getPublicId() != null && docType.getPublicId().indexOf("-//W3C//DTD XHTML ") == 0; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		return PageContextFactory.createPageContext(getResource(), PageContextFactory.FACELETS_PAGE_CONTEXT_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#getContext()
	 */
	@Override
	public IFaceletPageContext getContext() {
		return (IFaceletPageContext)super.getContext();
	}

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition, CompletionProposalInvocationContext context) {
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		TextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix != null && prefix.isELStarted()) {
			addTextELProposals(contentAssistRequest, context);
		} else {
			addELPredicateProposals(contentAssistRequest, getTagInsertionBaseRelevance(), true);
		}
	}
	
	protected boolean startsWithELBeginning(String text) {
		return (text != null && (text.startsWith(EL_DOLLAR_PREFIX) || text.startsWith(EL_NUMBER_PREFIX)));
	}

	@Override
	protected String getTagName(Node tag) {
		String tagName = tag.getNodeName();
		if(replaceJsfcTags) {
			// Only HTML tags
			if(tagName.indexOf(':')>0) {
				return tagName;
			}
			if (!(tag instanceof Element))
				return tagName;
			
			Element element = (Element)tag;

			NamedNodeMap attributes = element.getAttributes();
			Node jsfC = attributes.getNamedItem(JSFC_ATTRIBUTE_NAME);
			if(jsfC==null || (!(jsfC instanceof Attr))) {
				return tagName;
			}
			Attr jsfCAttribute = (Attr)jsfC;
			String jsfTagName = jsfCAttribute.getValue();
			if(jsfTagName==null || jsfTagName.indexOf(':')<1) {
				return tagName;
			}
			tagName = jsfTagName;
		}
		return tagName;
	}

	@Override
	protected int getTagInsertionBaseRelevance() {
		return XMLRelevanceConstants.R_STRICTLY_VALID_TAG_INSERTION;
	}

}
