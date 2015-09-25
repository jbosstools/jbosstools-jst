/*************************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.jst.web.kb.internal;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.util.EclipseJavaUtil;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class AbstractKbProjectExtension implements IKbProjectExtension {
	protected IProject project = null;

	protected boolean isBuilt = false;
	protected boolean isStorageResolved = false;

	protected Set<IKbProjectExtension> dependsOn = new HashSet<IKbProjectExtension>();
	protected Set<IKbProjectExtension> usedBy = new HashSet<IKbProjectExtension>();

	public AbstractKbProjectExtension() {}

	@Override
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public Set<IKbProjectExtension> getDependentProjects() {
		return usedBy;
	}

	@Override
	public Set<IKbProjectExtension> getUsedProjects() {
		return dependsOn;
	}

	public synchronized boolean dependsOnOtherProjects() {
		return !dependsOn.isEmpty();
	}

	public Set<IKbProjectExtension> getUsedProjects(boolean hierarchy) {
		if(hierarchy) {
			if(dependsOn.isEmpty()) return dependsOn;
			Set<IKbProjectExtension> result = new HashSet<IKbProjectExtension>();
			getAllUsedProjects(result);
			return result;
		} else {
			return dependsOn;
		}
	}

	void getAllUsedProjects(Set<IKbProjectExtension> result) {
		for (IKbProjectExtension n: dependsOn) {
			if(result.contains(n)) continue;
			result.add(n);
			((AbstractKbProjectExtension)n).getAllUsedProjects(result);
		}
	}

	
	@Override
	public void addUsedProject(final IKbProjectExtension project) {
		if(dependsOn.contains(project)) return;
		addUsedProjectInternal(project);
		project.addDependentProject(this);
		if(!project.isStorageResolved()) {
			resolveUsedProjectInJob(project);
		}
	}

	protected void resolveUsedProjectInJob(IKbProjectExtension project) {
	}

	protected synchronized void addUsedProjectInternal(IKbProjectExtension project) {
		dependsOn.add(project);
	}

	@Override
	public void addDependentProject(IKbProjectExtension project) {
		usedBy.add(project);
	}

	@Override
	public void removeUsedProject(IKbProjectExtension project) {
		AbstractKbProjectExtension p = (AbstractKbProjectExtension)project;
		if(!dependsOn.contains(p)) return;
		p.usedBy.remove(this);
		synchronized (this) {
			dependsOn.remove(p);
		}
	}

	/**
	 * Convenience method.
	 * 
	 * @param qualifiedName
	 * @return
	 */
	public IType getType(String qualifiedName) {
		IJavaProject jp = EclipseUtil.getJavaProject(getProject());
		if(jp == null) return null;
		try {
			return EclipseJavaUtil.findType(jp, qualifiedName);
		} catch (JavaModelException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * Returns true, if model is fully loaded.
	 * 
	 * @return
	 */
	@Override
	public boolean isStorageResolved() {
		return isStorageResolved;
	}

	@Override
	public void resolve() {
		resolveStorage(true);
	}

	public void resolveStorage(boolean load) {
		if(isStorageResolved) return;
		if(load) {
			load();
		} else {
			isStorageResolved = true;
		}
	}

	public void load() {
		if(isStorageResolved) return;
		isStorageResolved = true;
		build();

		postponeFiring();
		try {		
			//Use kb storage for dependent projects.
			loadProjectDependenciesFromKBProject();
		} finally {
			fireChanges();
		}
	}

	/**
	 * Create builder instance and build the project explicitly.
	 */
	protected abstract void build();

	public void postponeFiring() {
		//override
	}

	public void fireChanges() {
		//override
	}

	public abstract void update(boolean updateDependent);

	public void reloadProjectDependencies() {
		dependsOn.clear();
		usedBy.clear();
		loadProjectDependenciesFromKBProject();
	}

	protected void loadProjectDependenciesFromKBProject() {
		Element root = null;
		File file = getKBStorageFile();
		if(file != null && file.isFile()) {
			root = XMLUtilities.getElement(file, null);
			if(root != null) {
				loadProjectDependencies(root);
			}
		}		
	}
	
	private File getKBStorageFile() {
		IPath path = WebKbPlugin.getDefault().getStateLocation();
		File file = new File(path.toFile(), "projects/" + project.getName() + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return file;
	}
	
	private void loadProjectDependencies(Element root) {
		Element dependsOnElement = XMLUtilities.getUniqueChild(root, "depends-on-projects"); //$NON-NLS-1$
		if(dependsOnElement != null) {
			Element[] paths = XMLUtilities.getChildren(dependsOnElement, "project"); //$NON-NLS-1$
			for (int i = 0; i < paths.length; i++) {
				String p = paths[i].getAttribute("name"); //$NON-NLS-1$
				if(p == null || p.trim().length() == 0) continue;
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(p);
				if(project == null || !project.isAccessible()) continue;
				IKbProjectExtension sp = loadWithFactory(project, false);
				if(sp != null) {
					addUsedProjectInternal(sp);
					sp.addDependentProject(this);
				}
			}
		}

		Element usedElement = XMLUtilities.getUniqueChild(root, "used-by-projects"); //$NON-NLS-1$
		if(usedElement != null) {
			Element[] paths = XMLUtilities.getChildren(usedElement, "project"); //$NON-NLS-1$
			for (int i = 0; i < paths.length; i++) {
				String p = paths[i].getAttribute("name"); //$NON-NLS-1$
				if(p == null || p.trim().length() == 0) continue;
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(p);
				if(project == null || !project.isAccessible()) continue;
				IKbProjectExtension sp = loadWithFactory(project, false);
				if(sp != null) {
					addDependentProject(sp);
				}
			}
		}
	
	}

	protected abstract IKbProjectExtension loadWithFactory(IProject project, boolean resolve);

	public void dispose() {
	}
}
