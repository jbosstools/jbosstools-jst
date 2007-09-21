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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.ui.*;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ChangeTimeStampActionDelegate implements IWorkbenchWindowActionDelegate {
	static Set<String> warFilesToTouch = new HashSet<String>();
	static Set<String> earFilesToTouch = new HashSet<String>();
	
	static {
		warFilesToTouch.add("web.xml");
		earFilesToTouch.add("manifest.mf");
	}
	
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
		boolean isEJB = J2EEProjectUtilities.isEJBProject(project);
		
		if(!isEar) {
			IProject[] ps = J2EEProjectUtilities.getReferencingEARProjects(project);
			for (int i = 0; i < ps.length; i++) {
				fs.addAll(getFilesToTouch(ps[i]));
			}
		}
		if(isEar) {
			List l = J2EEProjectUtilities.getAllProjectFiles(project);
			for (int i = 0; i < l.size(); i++) {
				IFile f = (IFile)l.get(i);
				String n = f.getName().toLowerCase();
				if(earFilesToTouch.contains(n)) fs.add(f);
			}
		}
		if(isWar) {
			List l = J2EEProjectUtilities.getAllProjectFiles(project);
			for (int i = 0; i < l.size(); i++) {
				IFile f = (IFile)l.get(i);
				String n = f.getName().toLowerCase();
				if(warFilesToTouch.contains(n)) fs.add(f);
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
			f.touch(new NullProgressMonitor());			
		}
	}

	public void dispose() {
		window = null;
	}
	
}
