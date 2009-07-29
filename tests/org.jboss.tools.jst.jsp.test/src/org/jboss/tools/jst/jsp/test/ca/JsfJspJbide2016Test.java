package org.jboss.tools.jst.jsp.test.ca;

import org.jboss.tools.common.test.util.TestProjectProvider;

public class JsfJspJbide2016Test extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide2016Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting.jsp";
	
	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
	
	public void testJsfJspJbide2016Test(){
		String[] proposals = {
			"user.name",
		};

		checkProposals(PAGE_NAME, "value=\"#{user.}\"", 14, proposals, false);
	}
}