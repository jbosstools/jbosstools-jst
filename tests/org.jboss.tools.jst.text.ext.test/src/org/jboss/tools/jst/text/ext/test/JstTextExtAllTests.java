package org.jboss.tools.jst.text.ext.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstTextExtAllTests {
	
/*
	// all tests were moved to jst.ui.test plugin
	public void testJsfTextExt () {
		
	}
*/	
	public static Test suite() {
	TestSuite suite = new TestSuite(JstTextExtAllTests.class.getName());

		//$JUnit-BEGIN$

//		suite.addTestSuite(JstTextExtAllTests.class);
		
		suite.addTest(new ProjectImportTestSetup(new TestSuite(CSSStylesheetOpenOnTest.class),
				"org.jboss.tools.jst.text.ext.test",
				new String[]{"projects/OpenOnTest"},
				new String[]{"OpenOnTest"}));

		//$JUnit-END$
		return suite;
	}
}
