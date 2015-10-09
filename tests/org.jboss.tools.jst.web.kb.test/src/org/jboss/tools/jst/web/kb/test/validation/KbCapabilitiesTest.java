/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test.validation;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilderMarker;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.jboss.tools.jst.web.kb.internal.scanner.UsedJavaProjectCheck;
import org.jboss.tools.jst.web.kb.preferences.ELSeverityPreferences;
import org.jboss.tools.jst.web.kb.preferences.KBSeverityPreferences;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ResourcesUtils;

import junit.framework.TestCase;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class KbCapabilitiesTest extends TestCase {
	String project1Name = "kbProjectCapabilities1";
	String project2Name = "kbProjectCapabilities2";
	IProject project1;
	IProject project2;

	public KbCapabilitiesTest() {}

	@Override
	public void setUp() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);

		NullProgressMonitor monitor = new NullProgressMonitor();
		project1 = ResourcesPlugin.getWorkspace().getRoot().getProject(project1Name);
		project1.create(monitor);
		project1.open(monitor);
		addJavaNature(project1, new Path("build"), new Path("src"), null, monitor);

		project2 = ResourcesPlugin.getWorkspace().getRoot().getProject(project2Name);
		project2.create(monitor);
		project2.open(monitor);
		addJavaNature(project2, new Path("build"), new Path("src"), project1, monitor);
		WebKbPlugin.enableKB(project2, monitor);
		
		ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}

	public void testCapabilities() throws Exception {
		KbProject p2 = (KbProject)KbProjectFactory.getKbProject(project2, false, true);
		assertNotNull(p2);

		new UsedJavaProjectCheck().check(p2);
		IMarker[] ms = KbBuilderMarker.getOwnedMarkers(project2, KbBuilderMarker.KIND_DEPENDS_ON_NON_KB_POJECTS);
		assertEquals(1, ms.length);

		modifyPreference(project2, JavaCore.DISABLED);
		new UsedJavaProjectCheck().check(p2);
		ms = KbBuilderMarker.getOwnedMarkers(project2, KbBuilderMarker.KIND_DEPENDS_ON_NON_KB_POJECTS);
		assertNull(ms);

		modifyPreference(project2, JavaCore.ENABLED);
		new UsedJavaProjectCheck().check(p2);
		ms = KbBuilderMarker.getOwnedMarkers(project2, KbBuilderMarker.KIND_DEPENDS_ON_NON_KB_POJECTS);
		assertEquals(1, ms.length);
	}

	@Override
	protected void tearDown() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
		for (String name: new String[]{project2Name, project1Name}) {
			ResourcesUtils.deleteProject(name);
			JobUtils.waitForIdle();
		}
		ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}

	private void modifyPreference(IProject project, String value) throws CoreException {
		EclipsePreferences ps = (EclipsePreferences)KBSeverityPreferences.getInstance().getProjectPreferences(project);
				ELSeverityPreferences.getInstance().getProjectPreferences(project);
		ps.put(KBSeverityPreferences.ENABLE_BLOCK_PREFERENCE_NAME, value);
		TestUtil._waitForValidation(project);
	}

	private static void addJavaNature(IProject project, IPath outputLocation, IPath srcLocation, 
			IProject denendsOn, IProgressMonitor monitor) throws Exception {
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] {JavaCore.NATURE_ID});
		ICommand builderCmd = project.getDescription().newCommand();
		builderCmd.setBuilderName(JavaCore.BUILDER_ID);
		description.setBuildSpec(new ICommand[] {builderCmd});
		project.setDescription(description, monitor);

		IJavaProject javaProject = JavaCore.create(project);
		project.getFolder(outputLocation).create(IFolder.FORCE, true, monitor);
		project.getFolder(srcLocation).create(IFolder.FORCE, true, monitor);
		
		IClasspathEntry srcEntry = JavaCore.newSourceEntry(project.getFullPath().append(srcLocation));
		IClasspathEntry jreEntry = JavaCore.newContainerEntry(new Path(JavaRuntime.JRE_CONTAINER));
		IClasspathEntry projectEntry = denendsOn == null ? null : JavaCore.newProjectEntry(denendsOn.getFullPath());
		if(projectEntry == null) {
			javaProject.setRawClasspath(new IClasspathEntry[]{srcEntry, jreEntry}, 
				monitor);
		} else {
			javaProject.setRawClasspath(new IClasspathEntry[]{srcEntry, jreEntry, projectEntry}, 
					monitor);
		}
		javaProject.setOutputLocation(project.getFullPath().append(outputLocation), monitor);
		javaProject.save(monitor, true);
	}
}
