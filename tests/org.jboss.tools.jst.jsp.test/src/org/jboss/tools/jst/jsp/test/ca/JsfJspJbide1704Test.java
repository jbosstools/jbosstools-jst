package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.JobUtils;

public class JsfJspJbide1704Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	
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
			JobUtils.waitForIdle();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		for (int i = 0; i < PAGE_EXTENSIONS.length; i++) {
			doTestJsfJspJbide1704(PAGE_NAME + PAGE_EXTENSIONS[i]);
		}
	}
	
	private void doTestJsfJspJbide1704(String pageName) {

		openEditor(pageName);
		
		IStructuredDocumentRegion[] regions = ((IStructuredDocument)document).getStructuredDocumentRegions();
		
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
						
						for (int k = 0; result != null && k < result.length; k++) {
							// There should not be a proposal of type Red.Proposal in the result
							assertFalse("Content Assistant peturned proposals of type (" + result[k].getClass().getName() + ").", (result[k] instanceof AutoContentAssistantProposal));
						}
					}
				}
				
			}
			
		}
		
		closeEditor();
	}

}
