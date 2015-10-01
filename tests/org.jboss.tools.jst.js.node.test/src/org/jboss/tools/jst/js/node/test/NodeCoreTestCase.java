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
package org.jboss.tools.jst.js.node.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jst.js.node.util.WorkbenchResourceUtil;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodeCoreTestCase extends TestCase {

	private IProject testProject;

	
	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject"); //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestBowerProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}
	
	public void testIndexHtml() {
		IFile file = testProject.getFile(new Path("WebContent/pages/index.html"));  //$NON-NLS-1$
		assertTrue(file.exists());
	}
	
	public void testFindFileRecursively() throws CoreException {
		IFile index = WorkbenchResourceUtil.findFileRecursively(testProject, "index.html"); //$NON-NLS-1$
		assertNotNull(index);
		assertTrue(index.exists());
		
		IFile nonExistingFile = WorkbenchResourceUtil.findFileRecursively(testProject, "nonExistingFile.html"); //$NON-NLS-1$
		assertNull(nonExistingFile);
	}
	
}