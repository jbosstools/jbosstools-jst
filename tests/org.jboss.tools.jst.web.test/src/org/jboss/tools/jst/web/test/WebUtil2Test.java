/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.test.util.ResourcesUtils;

/**
 * Tests for a project that declares its root as the web root.
 * 
 * @author Viacheslav Kabanovich
 */
public class WebUtil2Test extends TestCase {

	private IProject project;

	@Override
	protected void setUp() throws Exception {
		project = getTestProject();
	}

	public IProject getTestProject() {
		if(project==null) {
			try {
				project = WebUtil2TestSetup.findTestProject();
				if(project==null || !project.exists()) {
					project = ResourcesUtils.importProject(JstWebAllTests.PLUGIN_ID, WebUtil2TestSetup.PROJECT_PATH);
					TestUtil._waitForValidation(project);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Can't import CDI test project: " + e.getMessage()); //$NON-NLS-1$
			}
		}
		return project;
	}

	/**
	 * This test should correctly find web root in the case when it is the same as the project root. 
	 * See https://issues.jboss.org/browse/JBIDE-10739
	 * 
	 * @throws Exception
	 */
	public void testGetWebRootFolder() throws Exception {
		IContainer[] roots = WebUtils.getWebRootFolders(getTestProject());
		assertEquals(1, roots.length);
		assertEquals(getTestProject(), roots[0]);

		IPath path = org.jboss.tools.common.web.WebUtils.getFirstWebContentPath(getTestProject());
		assertEquals(getTestProject().getFullPath(), path);
	}

	/**
	 * This test should not throw exception 'Path must include project and resource name'
	 * See https://issues.jboss.org/browse/JBIDE-10739
	 * 
	 * @throws Exception
	 */
	public void testFileFromProject() throws Exception {
		IFile f = getTestProject().getFile("inputUserName.jsp"); //$NON-NLS-1$
		IFile f1 = PageContextFactory.getFileFromProject(f.getName(), f);
		assertNotNull(f1);

		f1 = PageContextFactory.getFileFromProject("abc.jsp", f);
		assertNull(f1);
	}
}