package org.jboss.tools.jst.jsp.test.ca;

import org.jboss.tools.common.test.util.TestProjectProvider;

public class JsfJspJbide1813Test extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJspJbide1813Test";
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
	
	public void testJsfJspJbide1813Test(){
		
		// JBIDE-4341: the EL proposals are not to be returned (and tested) here anymore.
		//   The EL-proposals are removed from the test-list.
		//
		String[] proposals={
					"ltr",
					"rtl",
					"#{}"
		};

		checkProposals(PAGE_NAME, "dir=\"\"", 5, proposals, true);
	}
}