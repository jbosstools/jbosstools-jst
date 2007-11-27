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
package org.jboss.tools.jst.web.ui.navigator;

import org.eclipse.core.resources.IProject;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.ui.navigator.TreeViewerModelListenerImpl;
import org.jboss.tools.common.model.ui.views.navigator.NavigatorContentProvider;

public class WebProjectsContentProvider extends NavigatorContentProvider {

	protected TreeViewerModelListenerImpl createListener() {
		return new WebProjectsTreeListener();
	}

	protected String getFilteredTreeName(XModel model) {
		String nature = model.getProperties().getProperty("nature");
		IModelNature n = EclipseResourceUtil.getModelNature((IProject)model.getProperties().get("project"));
		if(nature != null && n != null && !n.getID().equals(nature)) {
			nature = n.getID();
			model.getProperties().setProperty("nature", nature);
		}
		if(nature != null && nature.indexOf("struts") >= 0) {
			return "StrutsProjects";
		} else if(nature != null && nature.indexOf("jsf") >= 0) {
			return "JSFProjects";
		} else {
			return "JSFProjects";
		}
	}

}
