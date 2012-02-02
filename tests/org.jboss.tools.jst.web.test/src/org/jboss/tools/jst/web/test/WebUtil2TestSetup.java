/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.test;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ResourcesUtils;

/**
 * @author Viacheslav Kabanovich
 */
public class WebUtil2TestSetup extends TestSetup {
	static String PROJECT_NAME = "testProjectRootAsWebContent"; //$NON-NLS-1$
	static String PROJECT_PATH = "/projects/" + PROJECT_NAME; //$NON-NLS-1$

	protected IProject project;

	public WebUtil2TestSetup(Test test) {
		super(test);
	}

	@Override
	protected void setUp() throws Exception {
		project = getTestProject();
		boolean state = ResourcesUtils.setBuildAutomatically(false);
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
		TestUtil._waitForValidation(project);
		ResourcesUtils.setBuildAutomatically(state);
	}

	public IProject getTestProject() {
		if(project==null) {
			try {
				project = findTestProject();
				if(project==null || !project.exists()) {
					project = ResourcesUtils.importProject(JstWebAllTests.PLUGIN_ID, PROJECT_PATH);
					TestUtil._waitForValidation(project);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Can't import test project: " + e.getMessage()); //$NON-NLS-1$
			}
		}
		return project;
	}

	public static IProject findTestProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
	}

	@Override
	protected void tearDown() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
		project.delete(true, true, null);
		JobUtils.waitForIdle();
		ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}
}