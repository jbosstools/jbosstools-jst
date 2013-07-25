/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jst.jsp.test.ca.CAMultipleCSSClassesInsertionTest;
import org.jboss.tools.jst.jsp.test.ca.Jbide1791Test;
import org.jboss.tools.jst.jsp.test.ca.Jbide6061Test;
import org.jboss.tools.jst.jsp.test.ca.Jbide9092Test;
import org.jboss.tools.jst.jsp.test.ca.JstCAOnCustomPrefixesTest;
import org.jboss.tools.jst.jsp.test.ca.JstCAURIChangeTest;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1641Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspNonAutomaticProposalInsertionTest;
import org.jboss.tools.jst.jsp.test.commands.KeyBindingsTest;
import org.jboss.tools.jst.jsp.test.selbar.SelectionBarTest;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.jsp.test"); //$NON-NLS-1$
		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				KeyBindingsTest.class,
				Jbide6061Test.class,
				CAMultipleCSSClassesInsertionTest.class,
				JstCAOnCustomPrefixesTest.class,
				JstCAURIChangeTest.class),
				"org.jboss.tools.jst.jsp.test", "projects/Jbide6061Test", //$NON-NLS-1$ //$NON-NLS-2$
				"Jbide6061Test")); //$NON-NLS-1$

		suite.addTestSuite(JstJspJbide1585Test.class);

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				JstJspJbide1641Test.class,
				JstJspNonAutomaticProposalInsertionTest.class),
				"org.jboss.tools.jst.jsp.test", "projects/JsfJbide1641Test", //$NON-NLS-1$ //$NON-NLS-2$
				"JsfJbide1641Test")); //$NON-NLS-1$

		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				Jbide1791Test.class,
				Jbide9092Test.class,
				SelectionBarTest.class),
				"org.jboss.tools.jst.jsp.test", "projects/JsfJbide1791Test", //$NON-NLS-1$ //$NON-NLS-2$
				"JsfJbide1791Test")); //$NON-NLS-1$

		suite.addTestSuite(JspPreferencesPageTest.class);

		return suite;
	}
}