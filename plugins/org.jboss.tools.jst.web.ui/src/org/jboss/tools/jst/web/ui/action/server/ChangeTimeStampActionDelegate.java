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
package org.jboss.tools.jst.web.ui.action.server;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class ChangeTimeStampActionDelegate implements IWorkbenchWindowActionDelegate {

	protected IWorkbenchWindow window;
	String tooltip = null;

	IProject project = null;

	protected String getActionPath() {
		return "SaveActions.ChangeTimeStamp";  //$NON-NLS-1$
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if(project == null && action.isEnabled()) action.setEnabled(false);
		if(tooltip == null) tooltip = action.getToolTipText();
		if(!(selection instanceof IStructuredSelection)) return;
		Object o = ((IStructuredSelection)selection).getFirstElement();
		IProject p = getProject(o);
		if(p == null) return;
		project = p;
		action.setEnabled(computeEnabled());
		if(project == null) {
			action.setToolTipText(tooltip);	
		} else {
			action.setToolTipText(WebUIMessages.CHANGE_TIME_STAMP + project.getName());
		}
	}

	IProject getProject(Object selection) {
		if(selection instanceof IResource) {
			return ((IResource)selection).getProject();
		} else if(selection instanceof IAdaptable) {
			Object r = ((IAdaptable)selection).getAdapter(IResource.class);
			return r instanceof IResource ? ((IResource)r).getProject() : null;
		}
		return null;
	}

	protected boolean computeEnabled() {
		if(project == null || !project.isAccessible()) return false;
		boolean isWar = J2EEProjectUtilities.isDynamicWebProject(project);
		boolean isEar = J2EEProjectUtilities.isEARProject(project);
		boolean isEJB = J2EEProjectUtilities.isEJBProject(project);
		return isEar || isEJB || isWar;
	}

	public void run(IAction action) {
		try {
			WebUtils.changeTimeStamp(project);
		} catch (Exception e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
	}

	public void dispose() {
		window = null;
	}
}