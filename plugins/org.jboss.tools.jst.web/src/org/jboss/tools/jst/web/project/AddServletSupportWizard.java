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
package org.jboss.tools.jst.web.project;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.ClassPathUpdate;
import org.jboss.tools.jst.web.*;

public class AddServletSupportWizard implements SpecialWizard {
	XModel model;
	ClassPathUpdate update;

	public void setObject(Object object) {
		update = (ClassPathUpdate)object;
		model = update.getModel();
	}

	public int execute() {
		try {
			addServletSupport();
		} catch (XModelException e) {
			//TODO probably wizard should throw XModelException?
			ModelPlugin.getPluginLog().logError(e);
			return 1;
		}
		return 0;
	}

	private void addServletSupport() throws XModelException {
		XModelObject web = model.getByPath("Web"); //$NON-NLS-1$
		if(web == null) return;
		String servletVersion = web.getAttributeValue("servlet version"); //$NON-NLS-1$
		if (servletVersion == null || "".equals(servletVersion)) { //$NON-NLS-1$
			servletVersion = WebPreference.DEFAULT_SERVLET_VERSION.getValue();
			model.changeObjectAttribute(web, "servlet version", servletVersion); //$NON-NLS-1$
		}
		String[] jars = WebUtils.getServletLibraries(getTemplatesBase(), servletVersion);
		if(web.getChildren(WebModuleConstants.ENTITY_WEB_MODULE).length == 0 &&
		   web.getChildren("WebJSFModule").length == 0) { //$NON-NLS-1$
			return;
		}
		for (int i = 0; i < jars.length; i++) {
			IPath jarPath = new Path(jars[i]);
			IPath variablePath = JavaCore.getClasspathVariable(jarPath.segment(0));
			IClasspathEntry entry = null;
			if (variablePath == null)
				entry = update.createNewClasspathEntry(jarPath, IClasspathEntry.CPE_LIBRARY);
			else 
				entry = update.createNewClasspathEntry(jarPath, IClasspathEntry.CPE_VARIABLE);
			update.registerEntry(entry);
		}
	}

	public String getTemplatesBase() {
		return WebModelPlugin.getTemplateStateLocation() + "templates"; //$NON-NLS-1$
	}

}
