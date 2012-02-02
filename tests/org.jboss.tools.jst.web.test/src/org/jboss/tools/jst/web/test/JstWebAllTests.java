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
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.test";
	public static final String PROJECT_NAME = "testWebProject";
	public static final String PROJECT_PATH = "/projects/" + PROJECT_NAME;

	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebAllTests.class.getName());
		suite.addTestSuite(WebMetaModelTest.class);
		suite.addTestSuite(WebContentAssistProviderTest.class);
//		suite.addTestSuite(BuilderTest.class);
		suite.addTest(new WebValidationTestSetup(new TestSuite(
				WebUtilTest.class,
				WebAppHelperTest.class,
				WebXMLValidationTest.class)));
		suite.addTest(new WebUtil2TestSetup(new TestSuite(
				WebUtil2Test.class)));
		return suite;
	}
}