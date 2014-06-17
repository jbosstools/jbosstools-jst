package org.jboss.tools.jst.web.ui.openon.test;

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
				"org.jboss.tools.jst.web.ui.test",
				new String[]{"projects/OpenOnTest"},
				new String[]{"OpenOnTest"}));
		
		suite.addTest(new ProjectImportTestSetup(new TestSuite(JQueryMobileHyperlinkDetectorTest.class),
				"org.jboss.tools.jst.web.ui.test",
				new String[]{"projects/OpenOnTest"},
				new String[]{"OpenOnTest"}));

		suite.addTest(new ProjectImportTestSetup(new TestSuite(TaglibOpenOnTest.class),
				"org.jboss.tools.jst.web.ui.test",
				new String[]{"projects/stopka-ui-test"},
				new String[]{"stopka-ui-test"}));
		suite.addTest(new ProjectImportTestSetup(new TestSuite(CreateNewFileHyperlinkTest.class),
				"org.jboss.tools.jst.web.ui.test",
				new String[]{"projects/Test"},
				new String[]{"Test"}));
		//$JUnit-END$
		return suite;
	}
}
