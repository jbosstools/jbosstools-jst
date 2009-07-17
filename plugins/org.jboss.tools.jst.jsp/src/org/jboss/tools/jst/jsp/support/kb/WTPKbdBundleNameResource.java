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
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ui.IEditorInput;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class WTPKbdBundleNameResource extends WTPKbAbstractModelResource {
	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLES;

	public WTPKbdBundleNameResource(IEditorInput editorInput) {
		super(editorInput);
	}
	
	public boolean isReadyToUse() {
		return (fProvider != null && fXModel != null);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbDinamicResource#queryProposal(java.lang.String)
	 */
	public Collection<KbProposal> queryProposal(String query) {
		Collection<KbProposal> proposals = new ArrayList<KbProposal>();
		if (!isReadyToUse()) return proposals;
		List sourceList = fProvider.getList(fXModel, SUPPORTED_ID, "", null); //$NON-NLS-1$
		
		if (sourceList != null && !sourceList.isEmpty()) {
			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			Iterator it = sourceList.iterator();
			while(it.hasNext()) sorted.add(it.next().toString());
			Iterator i = sorted.iterator();
			while(i.hasNext()) {
				String text = (String)i.next();
				if (text.toLowerCase().startsWith(query.toLowerCase())) {
					if (proposals == null) proposals = new ArrayList<KbProposal>(1);
					KbProposal proposal = new KbProposal();
					proposal.setLabel(text);
					proposal.setReplacementString(text);
					proposal.setPosition(text.length());
					proposal.setImage(JspEditorPlugin.getDefault().getImage(JspEditorPlugin.CA_JSF_MESSAGES_IMAGE_PATH));
					proposals.add(proposal);
				}
			}
		}
		return proposals;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbDinamicResource#getType()
	 */
	public String getType() { return KbDinamicResource.BUNDLE_NAME_TYPE; }

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.kb.KbResource#getInputStream()
	 */
	public InputStream getInputStream() { return null; }
	
	public String toString () { return "WTPKbdBundleNameResource"; } //$NON-NLS-1$

	public XModel getXModel() { return fXModel; }
	public String getSupportedID () { return SUPPORTED_ID; }

/*	public boolean equals (Object obj) {
		if (obj == null || !(obj instanceof WTPKbdBundleNameResource)) return false;
		WTPKbdBundleNameResource resource = (WTPKbdBundleNameResource)obj;
		
		return (resource.getSupportedID() == SUPPORTED_ID && resource.getXModel() == fXModel);
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