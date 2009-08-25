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
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ConverterIDProposalType extends ModelProposalType {

	private static final String IMAGE_NAME = "EnumerationProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

	public ConverterIDProposalType() {}

	@Override
	public TextProposal[] getProposals(KbQuery query) {
		String v = query.getValue();
		
		List<Object> list = provider.getList(xModel, getListID(), v, new Properties());
		Set<String> idList = new TreeSet<String>();
		if(list != null) {
			for (Object o: list) idList.add(o.toString());
		}
			
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String text: idList) {
			if(text.startsWith(v)) {
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
		
		return proposals.toArray(new TextProposal[0]);
	}

	protected String getListID() {
		return IWebPromptingProvider.JSF_CONVERTER_IDS;
	}

}
