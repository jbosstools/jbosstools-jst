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
package org.jboss.tools.jst.web.ui.internal.editor.contentassist.computers;

import java.util.List;
import java.util.Map;

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * EL Proposal computer for JSP pages
 * 
 * @author Victor Rubezhny
 *
 */
public class JspELCompletionProposalComputer extends XmlELCompletionProposalComputer {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.internal.editor.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		return createContext(PageContextFactory.JSP_PAGE_CONTEXT_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.internal.editor.contentassist.JspContentAssistProcessor#getContext()
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

	@Override
	protected int getTagInsertionBaseRelevance() {
		return TextProposal.R_TAG_INSERTION;
	}
}