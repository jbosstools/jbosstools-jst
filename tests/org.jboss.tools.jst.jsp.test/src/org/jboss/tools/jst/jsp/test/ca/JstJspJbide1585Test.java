package org.jboss.tools.jst.jsp.test.ca;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistantExtension;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.RedHatCustomCompletionProposal;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JstJspJbide1585Test extends TestCase {
	TestProjectProvider provider = null;
	IProject project = null;
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

	public void testJstJspJbide1585() {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		IFile jspFile = project.getFile(PAGE_NAME);

		assertTrue("The file \"" + PAGE_NAME + "\" is not found", (jspFile != null));
		assertTrue("The file \"" + PAGE_NAME + "\" is not found", (jspFile.exists()));

		FileEditorInput editorInput = new FileEditorInput(jspFile);
		Throwable exception = null;
		IEditorPart editorPart = null;
		try {
			editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, "org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor");
		} catch (PartInitException ex) {
			exception = ex;
			ex.printStackTrace();
			assertTrue("The JSP Visual Editor couldn't be initialized.", false);
		}

		JSPMultiPageEditor jspEditor = null;
		
		if (editorPart instanceof JSPMultiPageEditor)
			jspEditor = (JSPMultiPageEditor)editorPart;
		
		// Delay for 3 seconds so that
		// the Favorites view can be seen.
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Waiting for the jobs to complete has failed.", false);
		} 
		TestUtil.delay(3000);

		JSPTextEditor jspTextEditor = jspEditor.getJspEditor();
		StructuredTextViewer viewer = jspTextEditor.getTextViewer();
		IDocument document = viewer.getDocument();
		SourceViewerConfiguration config = TestUtil.getSourceViewerConfiguration(jspTextEditor);
		IContentAssistant contentAssistant = (config == null ? null : config.getContentAssistant(viewer));

		assertTrue("Cannot get the Content Assistant instance for the editor for page \"" + PAGE_NAME + "\"", (contentAssistant != null));

		
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
		
//		if (errorMessage != null && errorMessage.trim().length() > 0) {
//			System.out.println("#" + offsetToTest + ": ERROR MESSAGE: " + errorMessage);
//		}

		assertTrue("Content Assistant peturned no proposals", (result != null && result.length > 0));
		
		for (int i = 0; i < result.length; i++) {
			assertTrue("Content Assistant peturned proposals which type (" + result[i].getClass().getName() + ") differs from RedHatCustomCompletionProposal", (result[i] instanceof RedHatCustomCompletionProposal));
			RedHatCustomCompletionProposal proposal = (RedHatCustomCompletionProposal)result[i];
			String proposalString = proposal.getReplacementString();
			int proposalReplacementOffset = proposal.getReplacementOffset();
			int proposalReplacementLength = proposal.getReplacementLength();
//			try {
//				System.out.println("Result#" + i + " ==> Offs: " + offsetToTest + " RedHatCustomCompletionProposal[" + proposalString + "], Offs: " + proposalReplacementOffset + ", Len: " + proposalReplacementLength + ", Doc: [" + document.get(proposalReplacementOffset, proposalReplacementLength));
//			} catch (BadLocationException e) {
//			}
			assertTrue("The proposal replacement Offset is not correct.", proposalReplacementOffset == start + TAG_OPEN_STRING.length());
			assertTrue("The proposal replacement Length is not correct.", proposalReplacementLength == PREFIX_STRING.length());
			assertTrue("The proposal isn\'t filtered properly in the Content Assistant.", proposalString.startsWith(PREFIX_STRING));
		}
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.closeEditor(editorPart, false);
	}

}
