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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.impl.Libs;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.internal.scanner.ScannerException;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.internal.taglib.ELFunction;
import org.jboss.tools.jst.web.kb.taglib.IELFunction;
import org.jboss.tools.jst.web.kb.taglib.IFunctionLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.test.util.JUnitUtils;

public class KbModelWithSeveralJarCopiesTest extends TestCase {

	IProject project = null;
	boolean makeCopy = true;

	public KbModelWithSeveralJarCopiesTest() {
		super("Kb Model Test");
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel4");
		assertNotNull("Can't load TestKbModel", project); //$NON-NLS-1$
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

	public void testKbModelWithSeveralJarCopies() {
		IKbProject kbProject = getKbProject();
		
		IFile f = project.getFile("WebContent/WEB-INF/inputUserName.xhtml");
		assertNotNull(f);

		ITagLibrary[] ls = kbProject.getTagLibraries("http://java.sun.com/jsf/html");
		assertEquals("There should be 2 libraries for http://java.sun.com/jsf/html", 2, ls.length);
		Set<String> paths = new HashSet<String>();
		
		for (ITagLibrary l: ls) {
			String path = l.getSourcePath().toString();
			paths.add(path);
			XModelObject o = (XModelObject)((KbObject)l).getId();
			XModelObject fs = FileSystemsHelper.getLibs(o.getModel()).getLibrary(path);
			assertNotNull(fs);
			XModelObject o1 = fs.getChildByPath("META-INF/html_basic.tld");
			assertTrue(o == o1);
		}
		assertEquals("There should be 2 different paths for 2 libraries.", 2, paths.size());
	}

}