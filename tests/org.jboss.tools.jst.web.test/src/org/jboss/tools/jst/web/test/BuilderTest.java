/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.validation.internal.plugin.ValidationPlugin;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ResourcesUtils;

/**
 * @author Alexey Kazakov
 */
public class BuilderTest extends TestCase {

	private static final String PLUGIN_ID = "org.jboss.tools.jst.web.test";
	private static final String PROJECT_PATH = "/projects/testProject";
	private static final String JAVA_BUILDER_ID = "org.eclipse.jdt.core.javabuilder"; //$NON-NLS-1$

	protected IProject project;

	@Override
	protected void setUp() throws Exception {
		project = ResourcesUtils.importProject(PLUGIN_ID, PROJECT_PATH);
	}

	@Override
	protected void tearDown() throws Exception {
		ResourcesUtils.deleteProject(project.getName());
		JobUtils.waitForIdle();
	}

	public void testJavaProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		assertEquals(1, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testJavaWstProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		addBuilderToProject(false, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		assertEquals(2, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(1, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testWstJavaProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		addBuilderToProject(true, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		assertEquals(2, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
		assertEquals(1, getBuilderIndex(JAVA_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testJavaTestProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		assertEquals(2, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(1, getBuilderIndex(TestBuilder.ID));

		asserBuilderOrder();
	}

	public void testTestJavaProject() throws Exception {
		TestNature.APPEND_BUILDER = false;
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		assertEquals(2, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(TestBuilder.ID));
		assertEquals(1, getBuilderIndex(JAVA_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testJavaWstTestProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		addBuilderToProject(false, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(1, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
		assertEquals(2, getBuilderIndex(TestBuilder.ID));

		asserBuilderOrder();
	}

	public void testWstJavaTestProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		addBuilderToProject(true, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
		assertEquals(1, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(2, getBuilderIndex(TestBuilder.ID));

		asserBuilderOrder();
	}

	public void testTestJavaWstProject() throws Exception {
		TestNature.APPEND_BUILDER = false;
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		addBuilderToProject(false, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(TestBuilder.ID));
		assertEquals(1, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(2, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testWstTestJavaProject() throws Exception {
		TestNature.APPEND_BUILDER = false;
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		addBuilderToProject(true, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
		assertEquals(1, getBuilderIndex(TestBuilder.ID));
		assertEquals(2, getBuilderIndex(JAVA_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testTestWstJavaProject() throws Exception {
		TestNature.APPEND_BUILDER = false;
		addBuilderToProject(true, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(TestBuilder.ID));
		assertEquals(1, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
		assertEquals(2, getBuilderIndex(JAVA_BUILDER_ID));

		asserBuilderOrder();
	}

	public void testJavaTestWstProject() throws Exception {
		TestNature.APPEND_BUILDER = true;
		EclipseUtil.addNatureToProject(project, TestNature.ID);
		addBuilderToProject(false, project, ValidationPlugin.VALIDATION_BUILDER_ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(1, getBuilderIndex(TestBuilder.ID));
		assertEquals(2, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));

		asserBuilderOrder();
	}

	public void asserBuilderOrder() throws Exception {
		TestNature.APPEND_BUILDER = true;
		WebModelPlugin.addNatureToProjectWithValidationSupport(project, TestBuilder.ID, TestNature.ID);
		assertEquals(3, getBuilderNumbers());
		assertEquals(0, getBuilderIndex(JAVA_BUILDER_ID));
		assertEquals(1, getBuilderIndex(TestBuilder.ID));
		assertEquals(2, getBuilderIndex(ValidationPlugin.VALIDATION_BUILDER_ID));
	}

	public static void addBuilderToProject(boolean beginning, IProject project, String builderId) throws CoreException {
	    IProjectDescription desc = project.getDescription();
	    ICommand[] existing = desc.getBuildSpec();
	    ICommand[] cmds = new ICommand[existing.length + 1];
	    ICommand newcmd = project.getDescription().newCommand();
	    newcmd.setBuilderName(builderId);
	    if(beginning) {
		    cmds[0] = newcmd;
		    System.arraycopy(existing, 0, cmds, 1, existing.length );
	    } else {
		    cmds[existing.length] = newcmd;
		    System.arraycopy(existing, 0, cmds, 0, existing.length);
	    }
		desc.setBuildSpec(cmds);
		project.setDescription(desc, null);
	}

	public int getBuilderIndex(String builderId) throws CoreException {
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    for (int i = 0; i < commands.length; i++) {
	    	if(builderId.equals(commands[i].getBuilderName())) {
	    		return i;
	    	}
		}
	    return -1;
	}

	public int getBuilderNumbers() throws CoreException {
	    IProjectDescription desc = project.getDescription();
	    return desc.getBuildSpec().length;
	}
}