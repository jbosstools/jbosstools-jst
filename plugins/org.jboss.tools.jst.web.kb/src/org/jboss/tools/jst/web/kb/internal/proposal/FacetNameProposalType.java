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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.Facet;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.IFacesConfigTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.kb.taglib.TagLibraryManager;

/**
 * @author Alexey Kazakov
 */
public class FacetNameProposalType extends CustomProposalType {

	private static final String IMAGE_NAME = "EnumerationProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

	private IPageContext context;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		String[] parentTags = query.getParentTags();
		Set<String> facetNames = new HashSet<String>();
		if(parentTags.length>1) {
			String parentTag = parentTags[parentTags.length-2];
			KbQuery newQuery = new KbQuery();
			newQuery.setMask(false);
			newQuery.setType(KbQuery.Type.TAG_NAME);
			newQuery.setValue(parentTag);
			newQuery.setOffset(query.getOffset());
			IComponent[] components = PageProcessor.getInstance().getComponents(newQuery, context);
			Set<String> types = new HashSet<String>();
			for (int i = 0; i < components.length; i++) {
				String type = components[i].getComponentType();
				if(type!=null) {
					types.add(type.trim());
				}
			}
			if(!types.isEmpty()) {
				ITagLibrary[] libs = TagLibraryManager.getLibraries(context.getResource().getProject());
				for (ITagLibrary lib : libs) {
					if(lib instanceof IFacesConfigTagLibrary) {
						for (String type : types) {
							IComponent comp = lib.getComponentByType(type);
							if(comp!=null) {
								Facet[] facets = comp.getFacets();
								if(facets!=null) {
									for (int i = 0; i < facets.length; i++) {
										facetNames.add(facets[i].getName());
									}
								}
							}
						}
					}
				}
			}
		}
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
				proposal.setImage(ICON);
				proposals.add(proposal);
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#init(org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	protected void init(IPageContext context) {
		this.context = context;
	}
}