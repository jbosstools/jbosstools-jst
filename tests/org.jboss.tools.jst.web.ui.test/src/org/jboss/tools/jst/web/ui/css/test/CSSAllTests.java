/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.web.ui.css.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author Sergey Dzmitrovich
 * 
 *         To import other projects add names of imported projects to array the
 *         same way as AbstractCSSViewTest.IMPORT_PROJECT_NAME
 */
public class CSSAllTests {

	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.ui.test";

	public static final String RESOURCE_PATH = "projects/"; //$NON-NLS-1$

	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for CSS views"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(CSSViewTest.class);
		suite.addTestSuite(InputFractionalValueTest_JBIDE4790.class);
		
		/* yradtsevich: Commented because it is randomly failing https://issues.jboss.org/browse/JBIDE-12656.
		 * Please uncomment when JBIDE-4791 will be fixed. */
		// suite.addTestSuite(SelectionLosingByPropertySheet_JBIDE4791.class);
		
		suite.addTestSuite(ExtendingCSSViewTest_JBIDE4850.class);
		suite.addTestSuite(NotCompletedCSS_JBIDE4677.class);
		suite.addTestSuite(IncorrectPageAfterSelectionTest_JBIDE4849.class);
		suite.addTestSuite(CaseSensitiveTest_JBIDE4940.class);
		suite.addTestSuite(CSSStyleDialogTest.class);
		suite.addTestSuite(CssClassNewWizardTest.class);
		// $JUnit-END$

		return new ProjectImportTestSetup(
				suite,
				PLUGIN_ID,
				new String[] { RESOURCE_PATH + CSSViewTest.IMPORT_PROJECT_NAME },
				new String[] { AbstractCSSViewTest.IMPORT_PROJECT_NAME });
	}
}
