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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.ui.*;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;

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
	
	List<IFile> getFilesToTouch(IProject project) {
		List<IFile> fs = new ArrayList<IFile>();
		if(project == null || !project.isAccessible()) return fs;
		boolean isWar = J2EEProjectUtilities.isDynamicWebProject(project);
		boolean isEar = J2EEProjectUtilities.isEARProject(project);
		
		boolean isReferencedByEar = false;
		if(!isEar) {
			IProject[] ps = J2EEProjectUtilities.getReferencingEARProjects(project);
			for (int i = 0; i < ps.length; i++) {
				fs.addAll(getFilesToTouch(ps[i]));
				isReferencedByEar = true;
			}
		}
		if(isEar) {
			IVirtualComponent component = ComponentCore.createComponent(project);
			IPath path = component.getRootFolder().getProjectRelativePath();
			IFile f = project.getFile(path.append("META-INF").append("application.xml"));
			if(f != null && f.exists()) {
				fs.add(f);
			}
		}
		if(isWar && !isReferencedByEar) {
			IVirtualComponent component = ComponentCore.createComponent(project);
			IPath path = component.getRootFolder().getProjectRelativePath();
			IFile f = project.getFile(path.append("WEB-INF").append("web.xml"));
			if(f != null && f.exists()) {
				fs.add(f);
			}
		}
		return fs;
	}	

	public void run(IAction action) {
		try {
			doRun();
		} catch (Exception e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
	}

	protected void doRun() throws Exception {
		if(project == null || !project.isAccessible()) return;
		List<IFile> fs = getFilesToTouch(project);
		for (int i = 0; i < fs.size(); i++) {
			IFile f = (IFile)fs.get(i);
			f.setLocalTimeStamp(System.currentTimeMillis());
			f.touch(new NullProgressMonitor());	// done so deployers/listeners can detect the actual change.		
		}
	}

	public void dispose() {
		window = null;
	}
	
}
