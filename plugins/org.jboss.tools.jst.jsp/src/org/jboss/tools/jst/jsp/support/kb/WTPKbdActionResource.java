/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.support.kb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ui.IEditorInput;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class WTPKbdActionResource extends WTPKbAbstractModelResource {
	private String fPath;
	public static String SUPPORTED_ID = WebPromptingProvider.JSF_VIEW_ACTIONS;

	public WTPKbdActionResource(IEditorInput editorInput, WTPTextJspKbConnector connector) {
		super(editorInput);
		if(fXModelObject != null) {
			fPath = XModelObjectLoaderUtil.getResourcePath(fXModelObject);
		}
	}
	
	public boolean isReadyToUse() {
		return (fProvider != null && fXModelObject != null && fPath != null);
	}
	
	/*
	 * @see org.jboss.tools.common.kb.KbDinamicResource#queryProposal(java.lang.String)
	 */
	public Collection<KbProposal> queryProposal(String query) {
		Collection<KbProposal> proposals = new ArrayList<KbProposal>();
		if (!isReadyToUse()) return proposals;
		Properties view = new Properties();
		view.put(IWebPromptingProvider.VIEW_PATH, fPath); 

		List<Object> sourceList = fProvider.getList(fXModelObject.getModel(), SUPPORTED_ID, "", view); //$NON-NLS-1$

		if (sourceList != null && !sourceList.isEmpty()) {
			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			Iterator it = sourceList.iterator();
			while(it.hasNext()) sorted.add(it.next().toString());
			Iterator i = sorted.iterator();
			while(i.hasNext()) {
				String text = (String)i.next();
				if (text.trim().length() > 0 && text.toLowerCase().startsWith(query.toLowerCase())) {
					if (proposals == null) proposals = new ArrayList<KbProposal>(1);
					KbProposal proposal = new KbProposal();
					proposal.setLabel(text);
					proposal.setReplacementString(text);
					proposal.setPosition(text.length());
					proposal.setImage(JspEditorPlugin.getDefault().getImage(JspEditorPlugin.CA_JSF_ACTION_IMAGE_PATH));
					proposals.add(proposal);
				}
			}
		}
		return proposals;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbDinamicResource#getType()
	 */
	public String getType() { return KbDinamicResource.VIEW_ACTIONS_TYPE; }

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbResource#getInputStream()
	 */
	public InputStream getInputStream() { return null; }
	
	public String toString () { return "WTPKbdActionResource"; } //$NON-NLS-1$

	public String getSupportedID () { return SUPPORTED_ID; }
	
	public String getPath() {
		return fPath;
	}

/*	public boolean equals (Object obj) {
		if (obj == null || !(obj instanceof WTPKbdActionResource)) return false;
		WTPKbdActionResource resource = (WTPKbdActionResource)obj;
		
		return (resource.getSupportedID() == SUPPORTED_ID && 
			resource.getXModelObject() == fXModelObject && 
			(fPath != null && fPath.equalsIgnoreCase(resource.getPath())));
	}
*/
	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbDinamicResource#setConstraint(java.lang.String, java.lang.String)
	 */
	public void setConstraint(String name, String value) {
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbDinamicResource#clearConstraint(java.lang.String)
	 */
	public void clearConstraints() {
	}
}
