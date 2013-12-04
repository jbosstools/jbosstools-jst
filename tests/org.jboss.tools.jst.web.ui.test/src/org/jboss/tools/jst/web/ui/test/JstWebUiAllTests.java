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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jst.web.ui.css.test.CSSAllTests;
import org.jboss.tools.jst.web.ui.editor.test.JstJspAllTests;
import org.jboss.tools.jst.web.ui.openon.test.JstTextExtAllTests;
import org.jboss.tools.test.util.ProjectImportTestSetup;


/**
 * @author eskimo
 *
 */
public class JstWebUiAllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebUiAllTests.class.getName());

		suite.addTest(JstJspAllTests.suite());

		suite.addTest(CSSAllTests.suite());

		suite.addTest(JstTextExtAllTests.suite());

		TestSuite s = new TestSuite("Palette content");
		s.addTestSuite(JBossToolsEditorTest.class);
		s.addTestSuite(InsertJSCSSPaletteEntryTest.class);
		s.addTestSuite(NewJQueryMobilePaletteWizardTest.class);
		s.addTestSuite(PaletteContentsTest.class);
		s.addTestSuite(FormPropertySheetPageTest.class);
		s.addTestSuite(SwitchPaletteTest.class);
		suite.addTest(
				new ProjectImportTestSetup(s,
				"org.jboss.tools.jst.web.ui.test",
				new String[] { "projects/SimpleProject", }, //$NON-NLS-1$
				new String[] { "SimpleProject" })); //$NON-NLS-1$

		s = new TestSuite("DnD");
		s.addTestSuite(DnDImageTest.class);
		suite.addTest(
				new ProjectImportTestSetup(s,
				"org.jboss.tools.jst.web.ui.test",
				new String[] { "projects/WebProject", }, //$NON-NLS-1$
				new String[] { "WebProject" })); //$NON-NLS-1$

		suite.addTestSuite(ConfigurationBlockTest.class);
		suite.addTestSuite(WebViewsTest.class);
		suite.addTestSuite(WebWizardsTest.class);
		suite.addTestSuite(JstWebUiPreferencesPagesTest.class);
		suite.addTestSuite(PaletteFilterTest.class);
	
		return suite;
	}
}