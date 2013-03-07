/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.proposal;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 *
 */
public class JQueryMobileProposalType extends CustomProposalType {

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType#init(org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	protected void init(IPageContext context) {
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		return new TextProposal[0];
	}
}