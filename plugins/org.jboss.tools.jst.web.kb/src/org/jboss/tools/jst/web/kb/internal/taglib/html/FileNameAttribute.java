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
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.EnumerationProposalType;

/**
 * @author Alexey Kazakov
 */
public class FileNameAttribute extends HtmlAttribute {

	/**
	 * @param name
	 * @param description
	 */
	public FileNameAttribute(String name, String description, String[] values) {
		super(name, description, values);
	}

	@Override
	protected CustomProposalType createProposalType() {
		return new EnumerationProposalType() {
			@Override
			public TextProposal[] getProposals(KbQuery query) {
				if(params==null) {
					return EMPTY_PROPOSAL_LIST;
				}
				String value = query.getValue();
				List<TextProposal> proposals = new ArrayList<TextProposal>();
				for (int i = 0; i < params.length; i++) {
					String text = params[i].getValue();
					boolean createProposal = false;
					String fileName = text;
					int lastSl = text.lastIndexOf('/');
					if(lastSl>-1) {
						fileName = fileName.substring(lastSl+1);
					}
					if(text.startsWith(value)) {
						createProposal = true;
					} else {
						createProposal =  fileName.startsWith(value);
					}
					if(createProposal) {
						int rel = fileName == text?TextProposal.R_XML_ATTRIBUTE_VALUE:TextProposal.R_XML_ATTRIBUTE_VALUE-10;
						TextProposal proposal = new TextProposal();
						proposal.setLabel(text);
						proposal.setReplacementString(text);
						proposal.setAlternateMatch(fileName);
						proposal.setPosition(text.length());
						proposal.setImageDescriptor(EnumerationProposalType.IMAGE);
						proposal.setContextInfo(text);
						proposal.setRelevance(rel);
						proposals.add(proposal);
					}
				}
				return proposals.toArray(new TextProposal[0]);
			}
		};
	}
}