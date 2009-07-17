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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Alexey Kazakov
 */
public class ResourceBundleNameProposalType extends ModelProposalType {

	private static final String IMAGE_NAME = "MessageBundleProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#getProposals(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		if (!isReadyToUse()) {
			return EMPTY_PROPOSAL_LIST;
		}
		List<Object> sourceList = provider.getList(xModel, WebPromptingProvider.JSF_BUNDLES, "", null); //$NON-NLS-1$
		if (sourceList != null && !sourceList.isEmpty()) {
			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			Iterator<Object> it = sourceList.iterator();
			while(it.hasNext()) {
				sorted.add(it.next().toString());
			}
			for (String text : sorted) {
				if (text.toLowerCase().startsWith(query.getValue().toLowerCase())) {
					TextProposal proposal = new TextProposal();
					proposal.setLabel(text);
					proposal.setReplacementString(text);
					proposal.setPosition(text.length());
					if(ICON==null) {
						ICON = ImageDescriptor.createFromFile(WebKbPlugin.class, IMAGE_NAME).createImage();
					}
					proposal.setImage(ICON);
					proposals.add(proposal);
				}
			}
		}
		return proposals.toArray(new TextProposal[0]);
	}
}