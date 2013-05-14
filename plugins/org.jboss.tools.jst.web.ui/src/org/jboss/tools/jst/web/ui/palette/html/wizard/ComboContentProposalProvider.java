package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.swt.widgets.Combo;
import org.jboss.tools.common.model.ui.attribute.AttributeContentProposalProviderFactory;

public class ComboContentProposalProvider extends AbstractContentProposalProvider {
	String[] values = new String[0];

	public ComboContentProposalProvider(Combo combo) {
		values = combo.getItems();
		registerContentAssist(combo);
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> result = new ArrayList<IContentProposal>();
		String prefix = contents.substring(0, position);
		for (String v: values) {
			if(v.startsWith(prefix)) {
				IContentProposal cp = AttributeContentProposalProviderFactory.makeContentProposal(v, v, v);
				result.add(cp);
			}
		}
		return result.toArray(new IContentProposal[0]);
	}

}
