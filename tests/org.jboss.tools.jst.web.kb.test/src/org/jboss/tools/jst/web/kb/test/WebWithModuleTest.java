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
package org.jboss.tools.jst.web.kb.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.impl.FileSystemsImpl;
import org.jboss.tools.common.model.filesystems.impl.Libs;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.test.util.JUnitUtils;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class WebWithModuleTest extends TestCase {

	protected IProject project = null;
	protected boolean makeCopy = true;

	public WebWithModuleTest() {
		super("MyFaces Kb Model Test");
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("webapp");
		assertNotNull("Can't load webapp", project); //$NON-NLS-1$
	}

	/**
	 * webapp project has kb nature, and depends on utility project without kb nature.
	 * In this case we add sources of 'utility' to file systems of 'webapp'.
	 * 
	 * Check that file systems created but no links added to 'webapp'.
	 * 
	 * Warning: If we decide to stop adding sources of one project to other project, 
	 * this test should be removed.
	 * 
	 * @throws CoreException
	 */
	public void testWebProject() throws CoreException {
		KbProjectFactory.getKbProject(project, true);
		XModelObject o = EclipseResourceUtil.createObjectForResource(project);
		XModelObject libsrc = null;
		XModelObject f = FileSystemsHelper.getFileSystems(o.getModel());
		Libs libs = ((FileSystemsImpl)f).getLibs();
		libs.requestForUpdate();
		libs.update();
		
		//Check that sources of 'utility' are loaded by model of 'webapp'.
		XModelObject[] fs = f.getChildren();
		for (XModelObject s: fs) {
			String name = s.getAttributeValue("name");
			if(name.equals("lib-src")) {
				libsrc = s;
			}
		}
		assertNotNull(libsrc);
		XModelObject q = libsrc.getChildByPath("foo/bar/Dummy.java");
		assertNotNull(q);		

		//Check that no links is added to 'webapp'
		IResource[] ms = project.members();
		for (IResource m: ms) {
			assertFalse(m.isLinked());
		}
		
	}

}