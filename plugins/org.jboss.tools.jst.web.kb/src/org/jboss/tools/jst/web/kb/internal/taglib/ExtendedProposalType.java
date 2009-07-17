package org.jboss.tools.jst.web.kb.internal.taglib;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;

public class ExtendedProposalType extends CustomProposalType {

	public ExtendedProposalType() {}

	@Override
	public TextProposal[] getProposals(KbQuery query) {
		return new TextProposal[0];
	}

	@Override
	protected void init(IPageContext context) {
	}

}
