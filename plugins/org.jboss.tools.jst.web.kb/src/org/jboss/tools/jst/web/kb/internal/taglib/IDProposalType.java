package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IDProposalType extends CustomProposalType {
	static String ID = "id"; //$NON-NLS-1$
	static String QUOTE_1 = "'"; //$NON-NLS-1$
	static String QUOTE_2 = "\""; //$NON-NLS-1$
	Set<String> idList = new TreeSet<String>();

	@Override
	protected void init(IPageContext context) {
		idList.clear();
		IDocument document = context.getDocument();
		IStructuredModel sModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
		try {
			if (sModel != null) {
				Document sd = (sModel instanceof IDOMModel) ? ((IDOMModel) sModel).getDocument() : null;
				if(sd != null) {
					Element root = sd.getDocumentElement();
					collectIDs(root);
				}
			}
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
		
		
	}

	private void collectIDs(Element element) {
		String id = element.getAttribute(ID);
		if(id != null && id.length() > 0) idList.add(id);
		NodeList cs = element.getChildNodes();
		for (int i = 0; i < cs.getLength(); i++) {
			Node n = cs.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				collectIDs((Element)n);
			}
		}
	}

	@Override
	public TextProposal[] getProposals(KbQuery query) {
		String v = query.getValue();
		String txt = query.getText();
		if(txt.startsWith(QUOTE_1) || txt.startsWith(QUOTE_2)) txt = txt.substring(1);
		if(txt.endsWith(QUOTE_1) || txt.endsWith(QUOTE_2)) txt = txt.substring(0, txt.length() - 1);
		int offset = v.length();
		int b = v.lastIndexOf(',');
		if(b < 0) b = 0; else b += 1;
		String tail = txt.substring(offset);
		int e = tail.indexOf(',');
		if(e < 0) e = txt.length(); else e += offset;
		String prefix = v.substring(b);
			
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String text: idList) {
			if(text.startsWith(prefix)) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(text);
				proposal.setReplacementString(text);
				proposal.setPosition(b + text.length());
				
				proposal.setStart(b);
				proposal.setEnd(e);
				
				proposals.add(proposal);
			}
		}
		
		return proposals.toArray(new TextProposal[0]);
	}

}
