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
package org.jboss.tools.jst.web.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.test.util.JUnitUtils;

import junit.framework.TestCase;

public class KbModelTest extends TestCase {

	TestProjectProvider provider = null;
	IProject project = null;
	boolean makeCopy = true;

	public KbModelTest() {
		super("Kb Model Test");
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.web.test",
				null,"TestKbModel" ,true);
		project = provider.getProject();
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		this.project.build(IncrementalProjectBuilder.FULL_BUILD, null);
	}

	private IKbProject getKbProject() {
		IKbProject kbProject = null;
		try {
			kbProject = (IKbProject)project.getNature(IKbProject.NATURE_ID);
		} catch (Exception e) {
			JUnitUtils.fail("Cannot get seam nature.",e);
		}
		return kbProject;
	}

	public void testXMLScanner() {
		IKbProject kbProject = getKbProject();
		ITagLibrary[] ls = kbProject.getTagLibraries();
//		System.out.println("Libraries found=" + ls.length);
//		for (int i = 0; i < ls.length; i++) {
//			System.out.println(ls[i] + ":=>" + ls[i].getComponents().length + " " + ls[i].getURI());
//		}
	}

	public void testKbProjectObjects() {
		
	}

	public void testXMLSerialization() {
		
	}

	public void testCleanBuild() {

	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
}
