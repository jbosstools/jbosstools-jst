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

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.taglib.IComponent;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibAttribute extends AbstractAttribute {

	protected boolean extended = true;
	protected String defaultValue;
	protected CustomProposalType[] proposals;
	protected CustomTagLibComponent parentComponent;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isExtended()
	 */
	@Override
	public boolean isExtended() {
		return extended;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#setExtended(boolean)
	 */
	@Override
	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttribute#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		CustomProposalType[] types = getProposals();
		if(types.length==0) {
			return EMPTY_PROPOSAL_LIST;
		}
		if(types.length==1) {
			return types[0].getProposals(query, context);
		}
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (int i = 0; i < types.length; i++) {
			TextProposal[] typeProposals = types[i].getProposals(query, context);
			for (int j = 0; j < typeProposals.length; j++) {
				proposals.add(typeProposals[j]);
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}

	/**
	 * @return the proposals
	 */
	public CustomProposalType[] getProposals() {
		if(proposals==null) {
			proposals = new CustomProposalType[0];
		}
		return proposals;
	}

	/**
	 * @param proposals the proposals to set
	 */
	public void setProposals(CustomProposalType[] proposals) {
		this.proposals = proposals;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getComponent()
	 */
	@Override
	public IComponent getComponent() {
		return parentComponent;
	}

	/**
	 * @param parentComponent the parent component to set
	 */
	public void setParentComponent(CustomTagLibComponent parentComponent) {
		this.parentComponent = parentComponent;
	}
}