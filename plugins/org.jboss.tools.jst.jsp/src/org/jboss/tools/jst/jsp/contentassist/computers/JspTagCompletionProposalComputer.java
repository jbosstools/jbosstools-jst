/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
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
import java.util.Map;

import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.jsp.contentassist.ELPrefixUtils.ELTextRegion;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * Tag Proposal computer for JSP pages
 * 
 * @author Victor V. Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class JspTagCompletionProposalComputer extends XmlTagCompletionProposalComputer {
	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		return createContext(PageContextFactory.JSP_PAGE_CONTEXT_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#getContext()
	 */
	@Override
	public IPageContext getContext() {
		return (IPageContext)super.getContext();
	}

	/**
	 * Returns URI string for the prefix specified using the namespaces collected for 
	 * the {@link IPageContext} context.
	 * Important: The context must be created using createContext() method before using this method.
	 * 
	 * @param prefix
	 * @return
	 */
	@Override
	public String getUri(String prefix) {
		if (prefix == null)
			return null;
		
		Map<String, List<INameSpace>> nameSpaces = getContext().getNameSpaces(getOffset());
		if (nameSpaces == null || nameSpaces.isEmpty())
			return null;
		
		for (List<INameSpace> nameSpace : nameSpaces.values()) {
			for (INameSpace n : nameSpace) {
				if (prefix.equals(n.getPrefix())) {
					return n.getURI();
				}
			}
		}
		return null;
	}

	/**
	 * Calculates and adds the tag proposals to the Content Assist Request object
	 * The method is to be overridden here because jsp disallows to use EL-s inside a text region
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition,
			CompletionProposalInvocationContext context) {
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		ELTextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		addTagNameProposals(contentAssistRequest, childPosition, true, context);
	}

	/**
	 * Calculates and adds the EL proposals to the Content Assist Request object
	 */
	@Override
	protected void addTextELProposals(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		// No EL proposals are to be added here
	}
}