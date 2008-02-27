package org.jboss.tools.jst.jsp.test.ca;

import org.eclipse.core.resources.IResource;
import org.jboss.tools.common.test.util.TestProjectProvider;

public class JsfJspJbide1807Test extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJspJbide1807Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting";
	private static final String[] PAGE_EXTENSIONS = { ".xhtml", ".jsp" };
	
	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
		Throwable exception = null;
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception x) {
			exception = x;
			x.printStackTrace();
		}
		assertNull("An exception caught: " + (exception != null? exception.getMessage() : ""), exception);
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
	
	public void testJsfJspJbide1807Test(){
		String[][] proposals={
				{
					"#{applicationScope}",
					"#{cookie}",
					"#{facesContext}",
					"#{header}",
					"#{headerValues}",
					"#{initParam}",
					"#{param}",
					"#{paramValues}",
					"#{requestScope}",
					"#{sessionScope}",
					"#{view}"
				},
				{
					"jsp:attribute",
					"jsp:body",
					"jsp:element",
					"jsp:getProperty",
					"jsp:include",
					"jsp:output",
					"jsp:param",
					"JSP expression - JSP expression <%=..%>"
				}
		};
		
		for(int i = 0; i < PAGE_EXTENSIONS.length; i++){
			System.out.println("Testing file "+PAGE_NAME+PAGE_EXTENSIONS[i]+"...");
			contentAssistantCommonTest(PAGE_NAME+PAGE_EXTENSIONS[i], "<input type=\"image\" src=\"", 25, proposals[i], true);
			
		}
	}
}
