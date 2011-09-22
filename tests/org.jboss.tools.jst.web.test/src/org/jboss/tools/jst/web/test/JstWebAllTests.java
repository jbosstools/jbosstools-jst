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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstWebAllTests {

	public static final String PROJECT_NAME = "testWebProject";
	private static final String PROJECT_PATH = "/projects/" + PROJECT_NAME;

	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebAllTests.class.getName());
		suite.addTestSuite(WebContentAssistProviderTest.class);
		suite.addTestSuite(BuilderTest.class);
		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				WebUtilTest.class),
				"org.jboss.tools.jst.web.test", //$NON-NLS-1$
				PROJECT_PATH, //$NON-NLS-1$
				PROJECT_NAME));
		return suite;
	}
}