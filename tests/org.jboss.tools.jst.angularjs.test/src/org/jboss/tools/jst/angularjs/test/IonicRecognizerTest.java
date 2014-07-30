/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.angularjs.internal.ionic.IonicRecognizer;
import org.jboss.tools.jst.web.kb.PageContextFactory;

/**
 * @author Alexey Kazakov
 */
public class IonicRecognizerTest extends TestCase {

	protected IProject testProject;

	@Override
	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
			assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$
		}
	}

	public void testIonic() {
		assertTagLib("ionic.html", true);
	}

	public void testIonicWOLibs() {
		assertTagLib("ionicWOLibs.html", false);
	}

	public void testOtherTagLib() {
		assertTagLib("otherTagLib.html", false);
	}

	public void testTempalete() {
		assertTagLib("template.html", true);
	}

	private void assertTagLib(String fileName, boolean loaded) {
		IFile file = testProject.getFile(new Path("WebContent/pages/ionic/" + fileName));
		assertTrue(file.exists());
		ELContext context = PageContextFactory.createPageContext(file);
		assertNotNull(context);

		IonicRecognizer r = new IonicRecognizer();
		assertEquals(loaded, r.shouldBeLoaded(null, context));
	}
}