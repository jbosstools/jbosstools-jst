/******************************************************************************* 
 * Copyright (c) 2009-2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.internal.events.InternalBuilder;
import org.eclipse.core.internal.resources.BuildConfiguration;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.tools.common.model.XJob;
import org.jboss.tools.common.model.XJob.XRunnable;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilder;
import org.jboss.tools.jst.web.kb.internal.KbProject;

public class KbProjectFactory {
	private static Set<String> accessedProjects = new HashSet<String>();

	public static IKbProject getKbProject(IProject project, boolean resolve) {
		return getKbProject(project, resolve, false);
	}

	/**
	 * Factory method creating kb project instance by project resource.
	 * Returns null if 
	 * (1) project does not exist 
	 * (2) project is closed 
	 * (3) project has no kb nature
	 * (4) creating kb project failed.
	 * @param project
	 * @param resolve if true and results of last build have not been resolved they are loaded.
	 * @return
	 */
	public static IKbProject getKbProject(IProject project, boolean resolve, boolean isNatureRequired) {
		if(project == null || !project.exists() || !project.isOpen()) return null;
		try {
			if(!project.hasNature(IKbProject.NATURE_ID)) {
				if(isNatureRequired) return null;
				String s = project.getPersistentProperty(NATURE_MOCK);
				if(s != null && "true".equals(s)) {
					return getMockKbProject(project);
				}
				return null;
			}
		} catch (CoreException e) {
			//ignore - all checks are done above
			return null;
		}
		mockProjectStore.remove(project);

		IKbProject kbProject;
			try {
				kbProject = (IKbProject)project.getNature(IKbProject.NATURE_ID);
				accessedProjects.add(project.getName());
				if(resolve) kbProject.resolve();
				return kbProject;
			} catch (CoreException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
		return null;
	}

	/**
	 * Returns true if project has KbNature and it was created by getKbProject() method.
	 * @param project
	 * @return
	 */
	public static boolean isKbProjectAccessed(IProject project) {
		return accessedProjects.contains(project.getName());
	}

	public static QualifiedName NATURE_MOCK = new QualifiedName("", IKbProject.NATURE_ID + ".mock");

	static HashMap<IProject, IKbProject> mockProjectStore = new HashMap<IProject, IKbProject>();
	static HashSet<IProject> underConstruction = new HashSet<IProject>();

	static IKbProject getMockKbProject(final IProject project) {
		IKbProject result = mockProjectStore.get(project);
		if(result != null) {
			return result;
		}
		if(underConstruction.contains(project)) {
			return null;
		}
		underConstruction.add(project);
		final KbProject mock = new KbProject();
		mock.setMock();
		mock.setProject(project);
		mockProjectStore.put(project, mock);
		if(deleteProjectListener == null) {
			ResourcesPlugin.getWorkspace().addResourceChangeListener(deleteProjectListener = new RCL());
		}
		class KbBuilderEx extends KbBuilder {
			protected KbProject getKbProject() {
				return mock;
			}
			public void build() {
				try {
					build(INCREMENTAL_BUILD, null, new NullProgressMonitor());
				} catch (CoreException e) {
					WebModelPlugin.getPluginLog().logError(e);
				}
			}
		};
		XJob.addRunnable(new XRunnable(){
			public void run() {
//				System.out.println("build begin");
				long t0 = System.currentTimeMillis();
				KbBuilderEx builder = new KbBuilderEx();
				setProjectToBuilder(builder, project);
				if(WebKbPlugin.getDefault() == null) {
					return;
				}
				builder.build();
				underConstruction.remove(project);
//				long dt = System.currentTimeMillis() - t0;
//				System.out.println("build end " + dt);
			}
		
			public String getId() {
				return "Creating Mock Kb Project";
			}
		});
		return mock;
	}

	public static void setProjectToBuilder(IncrementalProjectBuilder builder, IProject project) {
		try {
			Method m = InternalBuilder.class.getDeclaredMethod("setBuildConfig", new Class[]{IBuildConfiguration.class});
			m.setAccessible(true);
			m.invoke(builder, new BuildConfiguration(project));
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}

	private static IResourceChangeListener deleteProjectListener = null;
	private static class RCL implements IResourceChangeListener {

		public void resourceChanged(IResourceChangeEvent event) {
			if(event.getType() == IResourceChangeEvent.PRE_DELETE
					|| event.getType() == IResourceChangeEvent.PRE_CLOSE) {
				IResource resource = event.getResource();
				IProject project = (IProject)resource.getAdapter(IProject.class);
				if(project != null) {
					IKbProject p = mockProjectStore.remove(project);
					if(p instanceof KbProject) {
						((KbProject)p).dispose();
					}
				}
			}
		}
		
	}
}
