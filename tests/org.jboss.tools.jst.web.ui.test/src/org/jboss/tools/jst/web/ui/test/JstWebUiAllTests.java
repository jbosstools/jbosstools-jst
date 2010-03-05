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
package org.jboss.tools.jst.web.ui.test;

import org.jboss.tools.test.util.ProjectImportTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author eskimo
 *
 */
public class JstWebUiAllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebUiAllTests.class.getName());
		suite.addTestSuite(WebViewsTest.class);
		suite.addTestSuite(WebWizardsTest.class);
		suite.addTestSuite(JstWebUiPreferencesPagesTest.class);
		suite.addTest(new ProjectImportTestSetup(new TestSuite(JSPProblemMarkerResolutionTest.class),
				"org.jboss.tools.jst.web.ui.test",
				new String[]{"projects/test_jsf_project"},
				new String[]{"test_jsf_project"}));

		return suite;
	}
}
