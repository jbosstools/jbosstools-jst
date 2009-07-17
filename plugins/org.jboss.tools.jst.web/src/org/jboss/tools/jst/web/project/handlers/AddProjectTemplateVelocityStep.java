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
package org.jboss.tools.jst.web.project.handlers;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import org.jboss.tools.common.meta.action.impl.MultistepWizardStep;
import org.jboss.tools.common.model.util.XModelObjectUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.project.helpers.ProjectTemplate;

public class AddProjectTemplateVelocityStep extends MultistepWizardStep {
	static String ATTR_NAME = "velocity templates"; //$NON-NLS-1$
	IProject root;

	public void reset() {
		root = null;
	}

	public String getStepImplementingClass() {
		return "org.jboss.tools.jst.web.ui.wizards.project.AddProjectTemplateVelocityView"; //$NON-NLS-1$
	}
	
	public void init() {
		IProject p = ((AddProjectTemplateSupport)support).getSelectedProject();
		if(p == root) return;
		root = p;
		if(root == null || !root.isOpen()) return;
		StringBuffer sb = new StringBuffer();
		IResource r = p.findMember("WebContent/WEB-INF/web.xml"); //$NON-NLS-1$
		if(r != null) {
			sb.append(r.getFullPath().toString());
		}
		r = p.findMember("ant/build.xml"); //$NON-NLS-1$
		if(r != null) {
			if(sb.length() > 0) sb.append(';');
			sb.append(r.getFullPath().toString());
		}
		support.setAttributeValue(id, ATTR_NAME, sb.toString());
	}
	
	void createPreprocessingFile(File target) {
		String list = support.getAttributeValue(id, ATTR_NAME);
		if(list == null || list.length() == 0) return;
		String[] s = XModelObjectUtil.asStringArray(list);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++) {
			String start = "/" + root.getName() + "/"; //$NON-NLS-1$ //$NON-NLS-2$
			if(s[i].startsWith(start)) {
				sb.append(s[i].substring(start.length())).append("\n"); //$NON-NLS-1$
			}
		}
		File f = new File(target, ProjectTemplate.PREPROCESSING);
		FileUtil.writeFile(f, sb.toString());
	}
	
}
