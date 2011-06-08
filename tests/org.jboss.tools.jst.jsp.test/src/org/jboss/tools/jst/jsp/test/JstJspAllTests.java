/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
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

import org.jboss.tools.jst.jsp.test.ca.Jbide1791Test;
import org.jboss.tools.jst.jsp.test.ca.Jbide6061Test;
import org.jboss.tools.jst.jsp.test.ca.Jbide9092Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1641Test;
import org.jboss.tools.jst.jsp.test.selbar.SelectionBarTest;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.jsp.test"); //$NON-NLS-1$

		
 		suite.addTestSuite(JstJspJbide1585Test.class);
		suite.addTestSuite(JstJspJbide1641Test.class);
		
		suite.addTestSuite(Jbide1791Test.class);
		suite.addTestSuite(Jbide9092Test.class);
		suite.addTestSuite(Jbide6061Test.class);
		
		suite.addTestSuite(JspPreferencesPageTest.class);
		suite.addTestSuite(SelectionBarTest.class);
	
		return suite;
	}

}
