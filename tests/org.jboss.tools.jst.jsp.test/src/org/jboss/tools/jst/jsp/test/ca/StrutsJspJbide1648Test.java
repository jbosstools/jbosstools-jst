package org.jboss.tools.jst.jsp.test.ca;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StrutsJspJbide1648Test extends TestCase {
	TestProjectProvider provider = null;
	IProject project = null;
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

	public void testStrutsJspJbide1648() {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		for (int i = 0; i < PAGE_NAMES.length; i++) {
			doTestOnPage(PAGE_NAMES[i]);
		}
		
	}
	
	private void doTestOnPage(String pageName) {
		IFile jspFile = project.getFile(pageName);

		assertTrue("The file \"" + pageName + "\" is not found", (jspFile != null));
		assertTrue("The file \"" + pageName + "\" is not found", (jspFile.exists()));

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

		assertTrue("Cannot get the Content Assistant instance for the editor for page \"" + pageName + "\"", (contentAssistant != null));

		ICompletionProposal[] result= null;
		String errorMessage = null;

		try {
			IContentAssistProcessor p= TestUtil.getProcessor(viewer, 0, contentAssistant);
			if (p != null) {
				result= p.computeCompletionProposals(viewer, 0);
				errorMessage= p.getErrorMessage();
			}
		
			//		if (errorMessage != null && errorMessage.trim().length() > 0) {
			//			System.out.println("#" + offsetToTest + ": ERROR MESSAGE: " + errorMessage);
			//		}
		} catch (Throwable x) {
			x.printStackTrace();
			if (x instanceof NullPointerException) {
				assertTrue("Content Assistant failed with NullPointerException", false);
			} else {
				assertTrue("Content Assistant failed with exception: \n" + x.getLocalizedMessage(), false);
			}
		}
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.closeEditor(editorPart, false);

	}
}
