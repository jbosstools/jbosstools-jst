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
package org.jboss.tools.jst.web.kb.internal.proposal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Alexey Kazakov
 */
public class NameSpaceProposalType extends ModelProposalType {

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		if (!isReadyToUse()) {
			return EMPTY_PROPOSAL_LIST;
		}
		String rQuery = getPassiveQueryPart(query.getValue());
		Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		List<Object> beanList = provider.getList(xModel, WebPromptingProvider.JSF_GET_TAGLIBS, "", null); //$NON-NLS-1$
		Iterator<Object> itr = beanList.iterator();
		while (itr.hasNext()) {
			sorted.add(itr.next().toString());
		}
		for (String text : sorted) {
			if(rQuery.length() == 0 || text.startsWith(rQuery)) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(text);
				proposal.setReplacementString(text);
				proposals.add(proposal);
				proposal.setPosition(proposal.getReplacementString().length());
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}

	private String getPassiveQueryPart(String query) {
		if (query == null || query.trim().length() == 0) {
			return ""; //$NON-NLS-1$
		}
		return query;
	}
}