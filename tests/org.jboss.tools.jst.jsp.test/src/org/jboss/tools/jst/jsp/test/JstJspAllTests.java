package org.jboss.tools.jst.jsp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jst.jsp.test.ca.Jbide1791Test;
import org.jboss.tools.jst.jsp.test.ca.JsfJspJbide1704Test;
import org.jboss.tools.jst.jsp.test.ca.JsfJspJbide1717Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1641Test;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.jsp.test");
 		suite.addTestSuite(JstJspJbide1585Test.class);
		suite.addTestSuite(JstJspJbide1641Test.class);
		suite.addTestSuite(JsfJspJbide1704Test.class);
		suite.addTestSuite(JsfJspJbide1717Test.class);
		suite.addTestSuite(Jbide1791Test.class);
		suite.addTestSuite(JspPreferencesPageTest.class);
	
		return suite;
	}

}
