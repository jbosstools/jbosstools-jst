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
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegionList;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.contentassist.ExtendedJSPContentAssistProcessor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JsfJspJbide1704Test extends TestCase {
	TestProjectProvider provider = null;
	IProject project = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide1704Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting";
	private static final String[] PAGE_EXTENSIONS = { ".xhtml", ".jsp" };
	
	private static final String PREFIX_TAG_NAME = "f:loadBundle";

	public static Test suite() {
		return new TestSuite(JsfJspJbide1704Test.class);
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

	public void testJsfJspJbide1704 () {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		for (int i = 0; i < PAGE_EXTENSIONS.length; i++) {
			doTestJsfJspJbide1704(PAGE_NAME + PAGE_EXTENSIONS[i]);
		}
	}
	
	private void doTestJsfJspJbide1704(String pageName) {
		IFile jspFile = project.getFile(pageName);

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
		
		assertTrue("The IDocument is not instance of IStructuredDocument", (document instanceof IStructuredDocument));

		IStructuredDocument sDocument = (IStructuredDocument)document;
		IStructuredDocumentRegion[] regions = sDocument.getStructuredDocumentRegions();
		String documentContent = document.get();
		
		boolean fLoadBundleTagIsFound = false;
		for (int i = 0; i < regions.length; i++) {
			IStructuredDocumentRegion sdRegion = regions[i];
			ITextRegionList list = sdRegion.getRegions();

			// find <f:loadBundle tag first (after this tag the CA is full of JSF- and other kind of proposals
			if (!fLoadBundleTagIsFound) {
				if (DOMRegionContext.XML_TAG_OPEN == list.get(0).getType() &&
					DOMRegionContext.XML_TAG_NAME == list.get(1).getType() &&
					PREFIX_TAG_NAME.equals(sdRegion.getFullText(list.get(1)).trim())) {
					fLoadBundleTagIsFound = true;
				}
				continue;
			}
			
			//
			if (DOMRegionContext.XML_TAG_OPEN == list.get(0).getType() &&
					DOMRegionContext.XML_TAG_NAME == list.get(1).getType()) {
				// find all the attribute values and their "after closing quotes" offsets
				
				for (int j = 2; j < list.size(); j++) {
					ITextRegion region = list.get(j);
					if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE == region.getType()) {
						int length = sdRegion.getFullText(region).trim().length();
						int offsetToTest = sdRegion.getStartOffset(region) + length;
						
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
						
//						if (errorMessage != null && errorMessage.trim().length() > 0) {
//							System.out.println("#" + offsetToTest + ": ERROR MESSAGE: " + errorMessage);
//						}

						for (int k = 0; result != null && k < result.length; k++) {
							// There should not be a proposal of type Red.Proposal in the result
							assertFalse("Content Assistant peturned proposals of type (" + result[k].getClass().getName() + ").", (result[k] instanceof AutoContentAssistantProposal));
						}
					}
				}
				
			}
			
		}
		
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Waiting for the jobs to complete has failed.", false);
		} 

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.closeEditor(editorPart, false);
	}

}
