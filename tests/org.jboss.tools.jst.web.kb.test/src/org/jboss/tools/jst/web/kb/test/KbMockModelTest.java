/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.internal.events.InternalBuilder;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.internal.scanner.ScannerException;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.test.util.JUnitUtils;
import org.jboss.tools.test.util.JobUtils;

public class KbMockModelTest extends TestCase {

	IProject project = null;
	boolean makeCopy = true;

	public KbMockModelTest() {
		super("Kb Model Test");
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel3");
		assertNotNull("Can't load TestKbModel3", project); //$NON-NLS-1$
	}

	public void testInternalMethod() throws Exception {
		//see KbProjectFactory.setProjectToBuilder
		Method m = InternalBuilder.class.getDeclaredMethod("setBuildConfig", new Class[]{IBuildConfiguration.class});
		assertNotNull(m);
	}

	public void testMockModel() {
		IKbProject kbProject = KbProjectFactory.getKbProject(project, true, true);
		assertNull(kbProject);
	
		boolean b = KbProject.checkKBBuilderInstalled(project);
		assertFalse(b);
	
		JobUtils.waitForIdle(2000);
		kbProject = KbProjectFactory.getKbProject(project, true, false);
		assertNotNull(kbProject);

		ITagLibrary[] ls = kbProject.getTagLibraries();
		
		assertTrue(ls.length > 0);
	}

}