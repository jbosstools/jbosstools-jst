package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.test.TestUtil;

public class JstJspJbide1585Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide1585Test";
	private static final String PAGE_NAME = "/WebContent/pages/inputname.xhtml";
	private static final String TAG_OPEN_STRING = "<";
	private static final String PREFIX_STRING = "ui:in";
	private static final String INSERTION_STRING = TAG_OPEN_STRING + PREFIX_STRING;

	public static Test suite() {
		return new TestSuite(JstJspJbide1585Test.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
		Throwable exception = null;

		assertNull("An exception caught: " + (exception != null? exception.getMessage() : ""), exception);
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}

	public void testJstJspJbide1585() {
		openEditor(PAGE_NAME);
		
		// Find start of <ui:define> tag
		String documentContent = document.get();
		int start = (documentContent == null ? -1 : documentContent.indexOf("<ui:define"));
		int offsetToTest = start + INSERTION_STRING.length();
		
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1));
		
		String documentContentModified = documentContent.substring(0, start) +
			INSERTION_STRING + documentContent.substring(start);
		
		jspTextEditor.setText(documentContentModified);
		
		ICompletionProposal[] result= null;
		String errorMessage = null;

		IContentAssistProcessor p= TestUtil.getProcessor(viewer, offsetToTest, contentAssistant);
		if (p != null) {
			try {
				result= p.computeCompletionProposals(viewer, offsetToTest);
			} catch (Throwable x) {
				x.printStackTrace();
			}
			errorMessage= p.getErrorMessage();
		}

		assertTrue("Content Assistant returned no proposals", (result != null && result.length > 0));
		
		for (int i = 0; i < result.length; i++) {
			assertTrue("Content Assistant returned proposals which type (" + result[i].getClass().getName() + ") differs from AutoContentAssistantProposal", (result[i] instanceof AutoContentAssistantProposal));
			AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)result[i];
			String proposalString = proposal.getReplacementString();
			int proposalReplacementOffset = proposal.getReplacementOffset();
			int proposalReplacementLength = proposal.getReplacementLength();

			assertTrue("The proposal replacement Offset is not correct.", proposalReplacementOffset == start + TAG_OPEN_STRING.length());
			assertTrue("The proposal replacement Length is not correct.", proposalReplacementLength == PREFIX_STRING.length());
			assertTrue("The proposal isn\'t filtered properly in the Content Assistant.", proposalString.startsWith(PREFIX_STRING));
		}
		
		closeEditor();
	}

}
