package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.JobUtils;

public class StrutsJspJbide1648Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "StrutsJbide1648Test";
	private static final String[] PAGE_NAMES = { 	
			"/WebContent/pages/a.jsp",
			"/WebContent/pages/a.xhtml"
		};

	public static Test suite() {
		return new TestSuite(StrutsJspJbide1648Test.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}

	public void testStrutsJspJbide1648() {
		try {
			JobUtils.waitForIdle();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		for (int i = 0; i < PAGE_NAMES.length; i++) {
			doTestOnPage(PAGE_NAMES[i]);
		}
		
	}
	
	private void doTestOnPage(String pageName) {
		openEditor(pageName);
		
		ICompletionProposal[] result= null;
		String errorMessage = null;

		try {
			IContentAssistProcessor p= TestUtil.getProcessor(viewer, 0, contentAssistant);
			if (p != null) {
				result= p.computeCompletionProposals(viewer, 0);
				errorMessage= p.getErrorMessage();
			}
		
		} catch (Throwable x) {
			x.printStackTrace();
			if (x instanceof NullPointerException) {
				assertTrue("Content Assistant failed with NullPointerException", false);
			} else {
				assertTrue("Content Assistant failed with exception: \n" + x.getLocalizedMessage(), false);
			}
		}
		
		closeEditor();

	}
}
