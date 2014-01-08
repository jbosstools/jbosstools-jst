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
package org.jboss.tools.jst.web.kb.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;

/**
 * @author Alexey Kazakov
 */
public class JQueryRecognizerTest extends TestCase {

	protected IProject testProject;

	@Override
	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
			assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$
		}
	}

	public void testJQM13() {
		assertVersion("jQueryMobile13.html", JQueryMobileVersion.JQM_1_3);
	}

	public void testJQM14() {
		assertVersion("jQueryMobile14.html", JQueryMobileVersion.JQM_1_4);
	}

	public void testJQMDefault() {
		assertVersion("jQueryMobileDefault.html", JQueryMobileVersion.JQM_1_4);
	}

	private void assertVersion(String fileName, JQueryMobileVersion version) {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/" + fileName));
		assertTrue(file.exists());
		ELContext context = PageContextFactory.createPageContext(file);
		assertNotNull(context);

		JQueryMobileRecognizer r = new JQueryMobileRecognizer();
		String v = r.getVersion(context);
		assertEquals(version.toString(), v);
	}
}