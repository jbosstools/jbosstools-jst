/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.js.npm.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmAllTests {
	
	public static final int DEFAULT_TIMEOUT = 5000;

	public static Test suite() {
		TestSuite suite = new TestSuite(NpmAllTests.class.getName());
		TestSuite s = new TestSuite("npm content"); //$NON-NLS-1$
		s.addTestSuite(NpmCoreTestCase.class);
		s.addTestSuite(NpmUITestCase.class);
		suite.addTest(
				new ProjectImportTestSetup(s,
				"org.jboss.tools.jst.js.npm.test", //$NON-NLS-1$
				new String[] { "projects/TestNpmProject" }, //$NON-NLS-1$
				new String[] { "TestNpmProject" })); //$NON-NLS-1$

		return suite;
	}
}