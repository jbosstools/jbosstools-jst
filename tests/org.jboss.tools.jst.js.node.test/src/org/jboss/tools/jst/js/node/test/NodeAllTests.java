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
package org.jboss.tools.jst.js.node.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodeAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(NodeAllTests.class.getName());
		TestSuite s = new TestSuite("Node content"); //$NON-NLS-1$
		s.addTestSuite(NodeCoreTestCase.class);
		s.addTestSuite(NodeUITestCase.class);
		suite.addTest(
				new ProjectImportTestSetup(s,
				"org.jboss.tools.jst.js.node.test", //$NON-NLS-1$
				new String[] { "projects/TestProject" }, //$NON-NLS-1$
				new String[] { "TestProject" })); //$NON-NLS-1$

		return suite;
	}
}