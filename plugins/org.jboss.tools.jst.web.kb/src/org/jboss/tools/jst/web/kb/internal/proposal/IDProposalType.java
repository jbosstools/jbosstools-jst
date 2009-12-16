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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("restriction")
public class IDProposalType extends CustomProposalType {
	private static final String IMAGE_NAME = "EnumerationProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

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
		} finally {
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
		int offset = v.length();
		int b = v.lastIndexOf(',');
		if(b < 0) b = 0; else b += 1;
		String tail = v.substring(offset);
		int e = tail.indexOf(',');
		if(e < 0) e = v.length(); else e += offset;
		String prefix = v.substring(b).trim();

		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String text: idList) {
			if(text.startsWith(prefix)) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(text);
				proposal.setReplacementString(text);
				proposal.setPosition(b + text.length());
				proposal.setStart(b);
				proposal.setEnd(e);
				if(ICON==null) {
					ICON = ImageDescriptor.createFromFile(WebKbPlugin.class, IMAGE_NAME).createImage();
				}
				proposal.setImage(ICON);
				
				proposals.add(proposal);
			}
		}

		return proposals.toArray(new TextProposal[0]);
	}
}