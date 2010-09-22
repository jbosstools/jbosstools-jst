package org.jboss.tools.jst.jsp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jst.jsp.test.ca.Jbide1791Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1641Test;
import org.jboss.tools.jst.jsp.test.ca.SelectionBarTest;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.jsp.test"); //$NON-NLS-1$
		
		/*
		 * 	TODO: Uncomment the following test case after https://jira.jboss.org/browse/JBIDE-7104 issue 
		 * is resolved due to enable the test to run
 		
 		suite.addTestSuite(JstJspJbide1585Test.class);
 		
 		*/
 		
		suite.addTestSuite(JstJspJbide1641Test.class);
		
		/* 
		 * TODO: Uncomment the following test case after https://jira.jboss.org/browse/JBIDE-7100 issue 
		 * is resolved due to enable the test to run
		
		suite.addTestSuite(Jbide1791Test.class);
		
		*/
		suite.addTestSuite(JspPreferencesPageTest.class);
		
		suite.addTestSuite(SelectionBarTest.class);
	
		return suite;
	}

}
