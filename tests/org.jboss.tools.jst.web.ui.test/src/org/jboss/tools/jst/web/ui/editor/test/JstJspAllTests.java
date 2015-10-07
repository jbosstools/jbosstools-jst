/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jst.web.ui.editor.test.ca.CAMultipleCSSClassesInsertionTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.Jbide1791Test;
import org.jboss.tools.jst.web.ui.editor.test.ca.Jbide6061Test;
import org.jboss.tools.jst.web.ui.editor.test.ca.Jbide9092Test;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstCAJQMFromPaletteTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstCAOnCustomPrefixesTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstCAURIChangeTest;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstJspCANamespacesInLastChar;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstJspJbide1641Test;
import org.jboss.tools.jst.web.ui.editor.test.ca.JstJspNonAutomaticProposalInsertionTest;
import org.jboss.tools.jst.web.ui.editor.test.commands.KeyBindingsTest;
import org.jboss.tools.jst.web.ui.editor.test.quickassist.JstJspQuickAssistTest;
import org.jboss.tools.jst.web.ui.editor.test.selbar.SelectionBarTest;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.web.ui.editor.test"); //$NON-NLS-1$

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				Jbide6061Test.class,
				CAMultipleCSSClassesInsertionTest.class,
				JstCAOnCustomPrefixesTest.class,
				JstCAURIChangeTest.class/* FIXME Fails under MAC OS X 
				KeyBindingsTest.class  */),
				"org.jboss.tools.jst.web.ui.test", "projects/Jbide6061Test", //$NON-NLS-1$ //$NON-NLS-2$
				"Jbide6061Test")); //$NON-NLS-1$

		suite.addTestSuite(JstJspJbide1585Test.class);

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				JstJspJbide1641Test.class,
				JstJspNonAutomaticProposalInsertionTest.class,
				JstJspCANamespacesInLastChar.class),
				"org.jboss.tools.jst.web.ui.test", "projects/JsfJbide1641Test", //$NON-NLS-1$ //$NON-NLS-2$
				"JsfJbide1641Test")); //$NON-NLS-1$

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				Jbide1791Test.class,
				Jbide9092Test.class,
				SelectionBarTest.class),
				"org.jboss.tools.jst.web.ui.test", "projects/JsfJbide1791Test", //$NON-NLS-1$ //$NON-NLS-2$
				"JsfJbide1791Test")); //$NON-NLS-1$

		suite.addTest(new JUnit4TestAdapter(JspPreferencesPageTest.class));

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				JstJspQuickAssistTest.class,
				JstCAJQMFromPaletteTest.class),
				"org.jboss.tools.jst.web.ui.test", "projects/StaticWebProject", //$NON-NLS-1$ //$NON-NLS-2$
				"StaticWebProject")); //$NON-NLS-1$

		return suite;
	}
}