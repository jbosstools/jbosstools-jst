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
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.IFaceletTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * @author Alexey Kazakov
 */
public class FacetNameProposalType extends CustomProposalType {

	private static final String IMAGE_NAME = "EnumerationProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

	private ELContext context;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		if (!(context instanceof IPageContext))
			return EMPTY_PROPOSAL_LIST;
		
		String[] parentTags = query.getParentTags();
		if(parentTags.length>1) {
			String parentTag = parentTags[parentTags.length-2];
			if(parentTag.contains(":")) { //$NON-NLS-1$
				KbQuery newQuery = new KbQuery();
				newQuery.setMask(false);
				newQuery.setType(KbQuery.Type.TAG_NAME);
				newQuery.setValue(parentTag);
				PageProcessor.getInstance().getComponents(query, (IPageContext)context);
				ITagLibrary[] libs = ((IPageContext)context).getLibraries();
				for (ITagLibrary l : libs) {
					if(l instanceof IFaceletTagLibrary) {
						//TODO
					}
				}
			}
		}
		List<String> facetNames = new ArrayList<String>();
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String facetName : facetNames) {
			if(facetName.startsWith(query.getValue())) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(facetName);
				proposal.setReplacementString(facetName);
				proposal.setPosition(facetName.length());
				if(ICON==null) {
					ICON = ImageDescriptor.createFromFile(WebKbPlugin.class, IMAGE_NAME).createImage();
				}
				proposals.add(proposal);
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#init(org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	protected void init(ELContext context) {
		this.context = context;
	}
}