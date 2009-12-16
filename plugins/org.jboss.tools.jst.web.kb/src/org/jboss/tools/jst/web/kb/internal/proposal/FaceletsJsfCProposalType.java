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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * @author Alexey Kazakov
 */
@SuppressWarnings("restriction")
public class FaceletsJsfCProposalType extends CustomProposalType {

	private static Image ICON;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		// trim first spaces
		String value = query.getValue();
		while(true) {
			if(value.startsWith(" ")) { //$NON-NLS-1$
				value = value.substring(1);
			} else {
				break;
			}
		}
		KbQuery kbQuery = new KbQuery();
		kbQuery.setMask(query.isMask());
		kbQuery.setType(KbQuery.Type.TAG_NAME);
		kbQuery.setValue(query.getValue());
		kbQuery.setOffset(query.getOffset());

		IComponent[] components = null;
		List<TextProposal> proposals = null;
		components = PageProcessor.getInstance().getComponents(kbQuery, context);
		if(components.length==0) {
			return EMPTY_PROPOSAL_LIST;
		}
		proposals = new ArrayList<TextProposal>();
		Map<String, List<String>> prefixes = new HashMap<String, List<String>>(); 
		for (int i = 0; i < components.length; i++) {
			ITagLibrary lib = components[i].getTagLib();
			if(ignoreTagLib(lib)) {
				continue;
			}
			List<String> pfx = prefixes.get(lib.getURI());
			if(pfx==null) {
				pfx = getPrefixes((IPageContext)context, components[i], kbQuery);
				prefixes.put(lib.getURI(), pfx);
			}
			for (String prefix : pfx) {
				TextProposal proposal = getProposal(prefix, components[i]);
				proposals.add(proposal);
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}

	private boolean ignoreTagLib(ITagLibrary lib) {
		return CustomTagLibManager.FACELETS_UI_TAG_LIB_URI.equals(lib.getURI()) || CustomTagLibManager.FACELETS_HTML_TAG_LIB_URI.equals(lib.getURI());
	}

	private TextProposal getProposal(String prefix, IComponent component) {
		TextProposal proposal = new TextProposal();
		proposal.setContextInfo(component.getDescription());
		proposal.setSource(component);
		StringBuffer label = new StringBuffer();
		label.append(prefix + KbQuery.PREFIX_SEPARATOR);
		label.append(component.getName());
		proposal.setLabel(label.toString());
		proposal.setReplacementString(proposal.getLabel());
		int position = proposal.getReplacementString().length();
		proposal.setPosition(position);
		if (ICON == null) {
			ICON = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TAG_GENERIC);
		}
		proposal.setImage(ICON);

		return proposal;
	}

	private List<String> getPrefixes(IPageContext context, IComponent component, KbQuery query) {
		List<String> prefixes = new ArrayList<String>();
		Map<String, List<INameSpace>> nameSpaces = context.getNameSpaces(query.getOffset());
		if(nameSpaces!=null) {
			List<INameSpace> nameSpace = nameSpaces.get(component.getTagLib().getURI());
			if(nameSpace!=null) {
				for (INameSpace n : nameSpace) {
					prefixes.add(n.getPrefix());
				}
			}
		}
		return prefixes;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#init(org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	protected void init(IPageContext context) {
	}
}