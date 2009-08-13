package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType;

public class IDResourceElement extends AttributeValueResource {
	IPageContext pageContext;
	CustomProposalType type;
	KbQuery kbQuery;

	public IDResourceElement(String name, ModelElement parent, IPageContext pageContext, CustomProposalType type, KbQuery kbQuery) {
		super(name, parent);
		this.pageContext = pageContext;
		this.type = type;
		this.kbQuery = kbQuery;
	}

	public ModelElement[] getChildren() {
		if(type == null || pageContext == null || kbQuery == null) {
			return new ModelElement[0];
		}
		TextProposal[] ps = type.getProposals(kbQuery, pageContext);
		ModelElement[] result = ps == null ? new ModelElement[0] : new ModelElement[ps.length];
		if(ps != null) for (int i = 0; i < ps.length; i++) {
			String s = ps[i].getReplacementString();
			result[i] = new EnumerationElement(s, this);			
		}
		return result;
	}

}
