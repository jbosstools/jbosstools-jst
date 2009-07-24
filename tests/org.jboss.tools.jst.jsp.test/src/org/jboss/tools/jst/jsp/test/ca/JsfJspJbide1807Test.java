package org.jboss.tools.jst.jsp.test.ca;

import org.jboss.tools.common.test.util.TestProjectProvider;

public class JsfJspJbide1807Test extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = true;
	private static final String PROJECT_NAME = "JsfJspJbide1807Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting.xhtml";
	
	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
	
	public void testJsfJspJbide1807Test(){
		
		// JBIDE-4341: the EL proposals are not to be returned (and tested) here anymore.
		//  - The EL-proposals are removed from the test-list.
		//  - The "/pages" proposal is added as the main case to test 
		String[] proposals={
					"/pages",
					"/templates",
					"new jsf el expression - create a new attribute value with #{}"
		};

		checkProposals(PAGE_NAME, "<input type=\"image\" src=\"", 25, proposals, true);
	}
}