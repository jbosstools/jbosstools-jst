/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.test.util.ResourcesUtils;

/**
 * @author Alexey Kazakov
 */
public class WebUtilTest extends TestCase {

	private IProject project;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		project = getTestProject();
	}

	public IProject getTestProject() {
		if(project==null) {
			try {
				project = findTestProject();
				if(project==null || !project.exists()) {
					project = ResourcesUtils.importProject(JstWebAllTests.PLUGIN_ID, JstWebAllTests.PROJECT_PATH);
					TestUtil._waitForValidation(project);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Can't import CDI test project: " + e.getMessage());
			}
		}
		return project;
	}

	public static IProject findTestProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(JstWebAllTests.PROJECT_NAME);
	}

	/**
	 * See https://issues.jboss.org/browse/JBIDE-9766
	 * @throws Exception
	 */
	public void testGetWebRootFolders() throws Exception {
		assertEquals(1, WebUtils.getWebRootFolders(project).length);
		assertEquals(2, WebUtils.getWebRootFolders(project, false).length);
	}
}