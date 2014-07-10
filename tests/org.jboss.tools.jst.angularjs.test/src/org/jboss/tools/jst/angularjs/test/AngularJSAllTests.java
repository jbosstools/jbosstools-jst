/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.test;

import org.jboss.tools.test.util.ProjectImportTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AngularJSAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AngularJSAllTests.class.getName());
		
		TestSuite s = new TestSuite("Ionic Palette content");
		s.addTestSuite(IonicPaletteTest.class);
		suite.addTest(
				new ProjectImportTestSetup(s,
				"org.jboss.tools.jst.web.ui.test",
				new String[] { "projects/SimpleProject", }, //$NON-NLS-1$
				new String[] { "SimpleProject" })); //$NON-NLS-1$
		
		return suite;
	}
}