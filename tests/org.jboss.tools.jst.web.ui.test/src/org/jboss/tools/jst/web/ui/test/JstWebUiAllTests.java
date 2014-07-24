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

import org.jboss.tools.common.base.test.model.XProjectImportTestSetUp;
import org.jboss.tools.jst.web.ui.editor.test.ca.CAPaletteHTML5TemplatesTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.CAPaletteJQM13TemplatesTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.CAPaletteJQM14TemplatesTest;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author eskimo
 *
 */
public class JstWebUiAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebUiAllTests.class.getName());

		TestSuite s = new TestSuite("Palette content");
		s.addTestSuite(CAPaletteJQM14TemplatesTest.class);
		s.addTestSuite(CAPaletteJQM13TemplatesTest.class);
		s.addTestSuite(CAPaletteHTML5TemplatesTest.class);
		s.addTestSuite(JBossToolsEditorTest.class);
		s.addTestSuite(MobilePaletteInsertPositionTest.class);
		s.addTestSuite(InsertJSCSSPaletteEntryTest.class);
		s.addTestSuite(InsertJSCSS14PaletteEntryTest.class);
		s.addTestSuite(PaletteManagerTest.class);
		s.addTestSuite(HTML5PaletteWizardTest.class);
		s.addTestSuite(NewJQueryMobilePaletteWizardTest.class);
		s.addTestSuite(NewJQueryMobile13PaletteWizardTest.class);
		s.addTestSuite(JQueryMobileVersionSwitchPaletteTest.class);
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

		s = new TestSuite("Palette CA templates");
		s.addTestSuite(HTML5PaletteCATest.class);
		s.addTestSuite(JQueryPaletteCATest.class);
		s.addTestSuite(JQuery14PaletteCATest.class);
		suite.addTest(new XProjectImportTestSetUp(s,
				"org.jboss.tools.jst.jsp.base.test",
				new String[]{"projects/TestKbModel"},
				new String[]{"TestKbModel"}));
		return suite;
	}
}