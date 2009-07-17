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
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.meta.action.impl.MultistepWizardStep;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;

public class AddProjectTemplateResourcesStep extends MultistepWizardStep {
	Set<String> excludedResources = new HashSet<String>();
	IProject root;

	public void reset() {
		root = null;
		excludedResources.clear();
	}

	public String getStepImplementingClass() {
		return "org.jboss.tools.jst.web.ui.wizards.project.AddProjectTemplateResourcesView"; //$NON-NLS-1$
	}
	
	public void init() {
		IProject p = ((AddProjectTemplateSupport)support).getSelectedProject();
		if(p == root) return;
		excludedResources.clear();
		root = p;
		if(root == null || !root.isOpen()) return;
		try {
			IResource[] rs = root.members();
			for (int i = 0; i < rs.length; i++) {
				String n = rs[i].getName();
				if(n.startsWith(".") && !n.equals(IModelNature.PROJECT_FILE_NANE)) { //$NON-NLS-1$
					excludedResources.add(rs[i].getFullPath().toString());
				}
			}
	        IResource r = root.findMember("WebContent/WEB-INF/lib"); //$NON-NLS-1$
	        if(r != null) excludedResources.add(r.getFullPath().toString());
	        r = root.findMember("WebContent/WEB-INF/classes"); //$NON-NLS-1$
	        if(r != null) excludedResources.add(r.getFullPath().toString());
		} catch (CoreException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	void matchResources(IContainer r, Set included) throws CoreException {
		String path = r.getFullPath().toString();
		if(!included.contains(path)) {
			excludedResources.add(path);
			return;
		}
		IResource[] rs = r.members();
		for (int i = 0; i < rs.length; i++) {
			if(rs[i] instanceof IContainer) {
				matchResources((IContainer)rs[i], included);
			} else {
				path = rs[i].getFullPath().toString();
				if(!included.contains(path)) {
					excludedResources.add(path);
				}
			}
		}
		
	}
	
	public Set getExcludedResources() {
		return excludedResources;
	}
	
	public void setSelectedResources(Set list) {
		excludedResources.clear();
		try {
			matchResources(root, list);
		} catch (CoreException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	Set<String> getExcludedFiles() {
		Set<String> set = new HashSet<String>();
		Iterator it = excludedResources.iterator();
		while(it.hasNext()) {
			String path = it.next().toString();
			IResource r = root.getParent().findMember(path);
			if(r != null) {
				File f = r.getLocation().toFile();
				try {
					set.add(f.getCanonicalPath());
				} catch (IOException e) {
					WebModelPlugin.getPluginLog().logError(e);
				}
			}
		}
		return set;
	}
	
    public void copyProjectToTemplate(File target, File source, XModel model) {
    	final Set set = getExcludedFiles();
    	FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				String path = null;
				try {
					path = pathname.getCanonicalPath();
				} catch (IOException e) {
					WebModelPlugin.getPluginLog().logError(e);
				}
				return path != null && !set.contains(path);
			}    		
    	};
    	
    	FileUtil.copyDir(source, target, true, true, true, filter);
    }

}
