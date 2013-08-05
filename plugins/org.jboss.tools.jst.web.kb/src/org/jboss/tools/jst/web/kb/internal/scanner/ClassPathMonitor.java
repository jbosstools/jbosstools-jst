/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.common.model.XJob;
import org.jboss.tools.common.model.XJob.XRunnable;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.ext.AbstractClassPathMonitor;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;

/**
 * Monitors class path of project and loads kb components of it.
 *  
 * @author Viacheslav Kabanovich
 */
public class ClassPathMonitor extends AbstractClassPathMonitor<KbProject> {

	/**
	 * Creates instance of class path for kb project
	 * @param project
	 */
	public ClassPathMonitor(KbProject project) {
		this.project = project;
	}
	
	/**
	 * Initialization of inner model.
	 */
	public void init() {
		model = InnerModelHelper.createXModel(project.getProject());
		super.init();
	}
	
	public IProject getProjectResource() {
		return project.getProject();
	}

	boolean isProcessed = false;

	/**
	 * Loads kb components from items recently added to class path. 
	 */
	public void process() {
		isProcessed = true;
		try {
			doProcess();
		} finally {
			isProcessed = false;
		}
	}

	protected void doProcess() {
		if(paths == null) {
			ModelPlugin.getDefault().logError("Failed to process class path in kb builder for project " + project);
			return;
		}
		for (String p: syncProcessedPaths()) {
			project.pathRemoved(new Path(p));
		}
		for (int i = 0; i < paths.size(); i++) {
			String p = paths.get(i);
			if(!requestForLoad(p)) continue;

			LibraryScanner scanner = new LibraryScanner();

			String fileName = new File(p).getName();
			if(EclipseResourceUtil.SYSTEM_JAR_SET.contains(fileName)) continue;

			XModelObject o = FileSystemsHelper.getLibs(model).getLibrary(p);
			if(o == null) continue;
			
			LoadedDeclarations c = null;
			try {
				if(scanner.isLikelyComponentSource(o)) {
					c = scanner.parse(o, new Path(p), project);
				}
			} catch (ScannerException e) {
				WebModelPlugin.getDefault().logError(e);
			}
			if(c == null) {
				c = new LoadedDeclarations();
			}
			componentsLoaded(c, new Path(p));
		}
		
		validateProjectDependencies();
	}
	
	public void waitProcess() {
		int count = 0;
		while(isProcessed) {
			try {
				synchronized (this) {
					wait(100);
				}
				count++;
				if(count >= 50) {
					String message = "Failed to wait for class path build";
					WebKbPlugin.getDefault().logWarning(message, new Exception(message));
					break;
				}
			} catch (InterruptedException e) {
				WebKbPlugin.getDefault().logError(e);
				break;
			}
		}
	}
	
	public void validateProjectDependencies() {
		List<KbProject> ps = null;
		
		try {
			ps = getKbProjects(project.getProject());
		} catch (CoreException e) {
			WebModelPlugin.getDefault().logError(e);
		}
		if(ps != null) {
			Set<KbProject> set = project.getKbProjects();
			Set<KbProject> removable = new HashSet<KbProject>();
			removable.addAll(set);
			removable.removeAll(ps);
			ps.removeAll(set);
			for (KbProject p : ps) {
				project.addKbProject(p);
			}
			for (KbProject p : removable) {
				project.removeKbProject(p);
			}
		}
	}

	public boolean hasToUpdateProjectDependencies() {
		try {
			List<KbProject> ps = getKbProjects(project.getProject());
			Set<KbProject> set = project.getKbProjects();
			if(set.size() != ps.size()) {
				return true;
			}
			for (KbProject p: ps) {
				if(!set.contains(p)) return true;
			}
		} catch (CoreException e) {
			WebModelPlugin.getDefault().logError(e);
		}
		return false;
	}

	void componentsLoaded(LoadedDeclarations c, IPath path) {
		if(c == null) return;
		project.registerComponents(c, path);
	}

	List<KbProject> getKbProjects(IProject project) throws CoreException {
		List<KbProject> list = new ArrayList<KbProject>();
		if(project.isAccessible() && project.hasNature(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(es[i].getPath().lastSegment());
					if(p == null || !p.isAccessible()) continue;
					KbProject.checkKBBuilderInstalled(p);
					IKbProject sp = KbProjectFactory.getKbProject(p, false);
					if(sp != null) list.add((KbProject)sp);
				}
			}
			
		}
		return list;
	}

	public void build() {
		waitProcess();
		if(update()) {
			process();
		} else if(hasToUpdateProjectDependencies()) {
			validateProjectDependencies();
		}
	}

	public void pathsChanged(List<String> paths) {
		super.pathsChanged(paths);
		if(project.isStorageResolved()) {
			XJob.addRunnableWithPriority(new XRunnable() {
				
				public void run() {
					if(!isProcessed && update()) {
//						System.out.println("Running " + getId());
						process();
					}					
				}
				
				public String getId() {
					return "Update class path of kb project " + project.getProject().getName(); //$NON-NLS-1$
				}
			});
		}
	}
}