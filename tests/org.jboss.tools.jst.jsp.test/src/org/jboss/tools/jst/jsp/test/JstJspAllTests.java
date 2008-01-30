package org.jboss.tools.jst.jsp.test;

import org.jboss.tools.jst.jsp.test.ca.JsfJspJbide1704Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1585Test;
import org.jboss.tools.jst.jsp.test.ca.JstJspJbide1641Test;
import org.jboss.tools.jst.jsp.test.ca.StrutsJspJbide1648Test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JstJspAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jboss.tools.jst.jsp.test");
		suite.addTestSuite(JstJspJbide1585Test.class);
		suite.addTestSuite(StrutsJspJbide1648Test.class);
		suite.addTestSuite(JstJspJbide1641Test.class);
		suite.addTestSuite(JsfJspJbide1704Test.class);
		return suite;
	}

}
