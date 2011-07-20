/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.el;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

public class DefaultKbELProposalProcessor extends KbELProposalProcessor {
	public static final Image KB_PROPOSAL_IMAGE = 
			WebKbPlugin.getDefault().getImage(WebKbPlugin.CA_KB_IMAGE_PATH);

	public DefaultKbELProposalProcessor() {
	}

	@Override
	protected boolean isEnabled(IFile file) {
		IProject project = (file == null ? null : file.getProject());
		IKbProject kb = KbProjectFactory.getKbProject(project, false, true);
		return kb != null;
	}

	@Override
	protected Image getImage() {
		return KB_PROPOSAL_IMAGE;
	}

}
