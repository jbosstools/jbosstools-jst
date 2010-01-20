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

import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.kb.IFaceletPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author Jeremy
 *
 */
@SuppressWarnings("restriction")
public class FaceletPageContectAssistProcessor extends JspContentAssistProcessor {
	private static final String JSFC_ATTRIBUTE_NAME = "jsfc"; //$NON-NLS-1$
	private boolean replaceJsfcTags;

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

	/**
	 * Calculates and adds the tag proposals to the Content Assist Request object
	 * The method is to be overridden here because xhtml allows to use EL-s inside a text region
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		TextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		addTagNameProposals(contentAssistRequest, childPosition, true);
		addAttributeValueELPredicateProposals(contentAssistRequest);
	}
	
	/**
	 * Calculates and adds the EL proposals to the Content Assist Request object
	 */
	@Override
	protected void addTextELProposals(ContentAssistRequest contentAssistRequest) {
		if (!isJsfProject())
			return;
		
		TextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix == null || !prefix.isELStarted()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(true, "#{}", //$NON-NLS-1$ 
					contentAssistRequest.getReplacementBeginPosition(), 
					0, 2, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_NewELExpression, null, 
					JstUIMessages.FaceletPageContectAssistProcessor_NewELExpressionTextInfo, TextProposal.R_TAG_INSERTION + 1);
			
			contentAssistRequest.addProposal(proposal);
			return;
		}
		String matchString = "#{" + prefix.getText(); //$NON-NLS-1$
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;

		int beginChangeOffset = prefix.getStartOffset() + prefix.getOffset();

		KbQuery kbQuery = createKbQuery(Type.TEXT, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());

		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];

			int replacementOffset = beginChangeOffset;
			int replacementLength = prefix.getLength();
			String replacementString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			int cursorPosition = replacementString.length();
			
			if (!prefix.isELClosed()) {
				replacementString += "}"; //$NON-NLS-1$
			}

			Image image = textProposal.getImage();

			// JBIDE-512, JBIDE-2541 related changes ===>>>
//			String displayString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			String displayString = textProposal.getLabel();
			if (displayString == null)
				displayString = textProposal.getReplacementString() == null ? replacementString : textProposal.getReplacementString();

			// <<<=== JBIDE-512, JBIDE-2541 related changes
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = TextProposal.R_JSP_JSF_EL_VARIABLE_ATTRIBUTE_VALUE;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}

		if (prefix.isELStarted() && !prefix.isELClosed()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal("}", //$NON-NLS-1$
					getOffset(), 0, 1, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_CloseELExpression, 
					null, JstUIMessages.JspContentAssistProcessor_CloseELExpressionInfo, TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE);

			contentAssistRequest.addProposal(proposal);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#addAttributeNameProposals(org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest)
	 */
	@Override
	protected void addAttributeNameProposals(
			ContentAssistRequest contentAssistRequest) {
		super.addAttributeNameProposals(contentAssistRequest);
		this.replaceJsfcTags = true;
		super.addAttributeNameProposals(contentAssistRequest);
		this.replaceJsfcTags = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#addAttributeValueProposals(org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest)
	 */
	@Override
	protected void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {
		super.addAttributeValueProposals(contentAssistRequest);
		this.replaceJsfcTags = true;
		super.addAttributeValueProposals(contentAssistRequest);
		this.replaceJsfcTags = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#getTagName(org.w3c.dom.Node)
	 */
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
}