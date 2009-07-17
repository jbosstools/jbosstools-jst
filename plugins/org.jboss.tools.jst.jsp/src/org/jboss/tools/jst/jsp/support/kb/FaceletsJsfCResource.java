/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.support.kb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.jst.jsp.contentassist.FaceletsHtmlContentAssistProcessor;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.kb.KbQuery;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * @author igels
 */
public class FaceletsJsfCResource implements KbDinamicResource {

	private WtpKbConnector kbConnector;

	public FaceletsJsfCResource(WtpKbConnector kbConnector) {
		super();
		this.kbConnector = kbConnector;
	}

	/**
	 * @see org.jboss.tools.common.kb.KbDinamicResource#setConstraint(java.lang.String, java.lang.String)
	 */
	public void setConstraint(String name, String value) {
	}

	/**
	 * @see org.jboss.tools.common.kb.KbDinamicResource#clearConstraints()
	 */
	public void clearConstraints() {
	}

	private static final Collection<KbProposal> emptyProposalList = new ArrayList<KbProposal>(0);

	/**
	 * @see org.jboss.tools.common.kb.KbDinamicResource#queryProposal(java.lang.String)
	 */
	public Collection<KbProposal> queryProposal(String query) {
		if(query.indexOf(KbQuery.ATTRIBUTE_SEPARATOR)>-1) {
			return emptyProposalList;
		}
		// trim first spaces
		while(true) {
			if(query.startsWith(" ")) { //$NON-NLS-1$
				query = query.substring(1);
			} else {
				break;
			}
		}
		String kbQuery = KbQuery.TAG_SEPARATOR + query;
		Collection proposals = null;
		ArrayList<KbProposal> proposalsWithoutHtmlTags = null;
		try {
			proposals = kbConnector.getProposals(kbQuery);
			if(proposals==null) {
				return emptyProposalList;
			}
			proposalsWithoutHtmlTags = new ArrayList<KbProposal>(proposals.size());
			for (Iterator iter = proposals.iterator(); iter.hasNext();) {
				KbProposal proposal = (KbProposal) iter.next();
				if(!ignoreProposal(proposal)) {
					proposal.setReplacementString(proposal.getLabel());
					proposal.setPosition(proposal.getLabel().length());
					proposal.setRelevance(10000);
					proposalsWithoutHtmlTags.add(proposal);
				}
			}
		} catch (KbException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		return proposalsWithoutHtmlTags;
	}

	private boolean ignoreProposal(KbProposal proposal) {
		return proposal.getLabel().startsWith(FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart) || proposal.getLabel().indexOf(':')<0;
	}

	/**
	 * @see org.jboss.tools.common.kb.KbDinamicResource#getType()
	 */
	public String getType() {
		return KbDinamicResource.FACELETS_JSFC_TYPE;
	}

	/**
	 * @see org.jboss.tools.common.kb.KbResource#getInputStream()
	 */
	public InputStream getInputStream() {
		return null;
	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
	public boolean equals(Object o) {
		if(!(o instanceof FaceletsJsfCResource)) return false;
		FaceletsJsfCResource other = (FaceletsJsfCResource)o;
		return other.getType().equals(getType()) && other.kbConnector == kbConnector;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getType().hashCode() + kbConnector.hashCode();
	}
}