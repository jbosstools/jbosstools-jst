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
package org.jboss.tools.jst.web.kb.internal.taglib.html.angular;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.EnumerationProposalType;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
abstract public class NgClassAttribute extends HtmlAttribute {

	protected static final String NAME = "class";

	public NgClassAttribute() {
		super(NAME, null);
	}

	abstract protected boolean checkComponent(KbQuery query);
	abstract protected Set<IDirective> getDirectives(KbQuery query);

	@Override
	protected CustomProposalType createProposalType() {
		return new EnumerationProposalType() {
			@Override
			public TextProposal[] getProposals(KbQuery query) {
				if(!checkComponent(query)) {
					return EMPTY_PROPOSAL_LIST;
				}
				List<TextProposal> proposals = new ArrayList<TextProposal>();
				for (IDirective directive : getDirectives(query)) {
					if(directive.checkAttribute(query)) {
						// Do not use getValue() because it trims the string and removes opening quote char, but all the characters 
						// (including whitespaces and quotes) are valuable here
						String strValue = query.getStringQuery();
						String value = query.getValue();
						if(strValue.endsWith(" ")) {
							value = value + " ";
						}
						String originValue = "";
						int s = value.lastIndexOf(';');
						int s1 = value.lastIndexOf(' ');
						if(s1>s) {
							s = s1;
						}
						if(s>-1) {
							originValue = value.substring(0, s+1);
							if(!originValue.endsWith(" ")) {
								originValue = originValue + " ";
							}
							value = value.length()==s+1?"":value.substring(s+1, value.length()).trim();
						}
						if(directive.getStartText().startsWith(value) && (originValue.equals(value) || originValue.indexOf(directive.getStartText())==-1)) {
							String text = directive.getStartText() + directive.getEndText();
							TextProposal proposal = new TextProposal();
							proposal.setLabel(text);
							proposal.setReplacementString(originValue + text);
							proposal.setAlternateMatch(directive.getStartText());
							proposal.setImageDescriptor(EnumerationProposalType.IMAGE);
							proposal.setContextInfo(directive.getDescription());
							proposals.add(proposal);
						}
					}
				}
				return proposals.toArray(new TextProposal[0]);
			}
		};
	}
}