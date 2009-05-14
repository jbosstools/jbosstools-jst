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
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;

/**
 * Abstract implementation of IAttribute
 * @author Alexey Kazakov
 */
public abstract class AbstractAttribute implements IAttribute {

	protected String description;
	protected String name;
	protected boolean preferable;
	protected boolean required;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isPreferable()
	 */
	public boolean isPreferable() {
		return preferable;
	}

	/**
	 * @param preferable the preferable to set
	 */
	public void setPreferable(boolean preferable) {
		this.preferable = preferable;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isRequired()
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		ELResolver[] resolvers = context.getElResolvers();
		for (int i = 0; i < resolvers.length; i++) {
			proposals.addAll(resolvers[i].getCompletions(query.getValue(), false, query.getValue().length(), context));
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}
}