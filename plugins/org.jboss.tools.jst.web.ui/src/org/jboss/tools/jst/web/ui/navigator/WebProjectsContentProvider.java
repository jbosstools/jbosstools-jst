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
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.ui.navigator.TreeViewerModelListenerImpl;
import org.jboss.tools.common.model.ui.preferences.DecoratorPreferencesListener;
import org.jboss.tools.common.model.ui.views.navigator.NavigatorContentProvider;

public class WebProjectsContentProvider extends NavigatorContentProvider {
	DecoratorPreferencesListener decoratorListener = null;

	protected TreeViewerModelListenerImpl createListener() {
		return new WebProjectsTreeListener();
	}

	protected String getFilteredTreeName(XModel model) {
		String nature = model.getProperties().getProperty("nature"); //$NON-NLS-1$
		IModelNature n = EclipseResourceUtil.getModelNature((IProject)model.getProperties().get("project")); //$NON-NLS-1$
		if(nature != null && n != null && !n.getID().equals(nature)) {
			nature = n.getID();
			model.getProperties().setProperty("nature", nature); //$NON-NLS-1$
		}
		if(nature != null && nature.indexOf("struts") >= 0) { //$NON-NLS-1$
			return "StrutsProjects"; //$NON-NLS-1$
		} else if(nature != null && nature.indexOf("jsf") >= 0) { //$NON-NLS-1$
			return "JSFProjects"; //$NON-NLS-1$
		} else {
			return "JSFProjects"; //$NON-NLS-1$
		}
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		if(viewer instanceof StructuredViewer) {
			if(decoratorListener == null) {
				decoratorListener = new DecoratorPreferencesListener();
				decoratorListener.init();
			}
			decoratorListener.setViewer((StructuredViewer)viewer);
		}
	}
	
	public void dispose() {
		if(decoratorListener != null) {
			decoratorListener.dispose();
			decoratorListener = null;
		}
		super.dispose();
	}
}
