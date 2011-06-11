/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;

/**
 * @author Alexey Kazakov
 */
public class StaticAttribute implements IAttribute {

	protected String description;
	protected String name;
	protected boolean required;
	protected IComponent component;

	public StaticAttribute(IComponent component, String name) {
		this.component = component;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		return EMPTY_PROPOSAL_LIST;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isRequired()
	 */
	public boolean isRequired() {
		return required;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isPreferable()
	 */
	public boolean isPreferable() {
		return isRequired();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isExtended()
	 */
	public boolean isExtended() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getComponent()
	 */
	public IComponent getComponent() {
		return component;
	}
}