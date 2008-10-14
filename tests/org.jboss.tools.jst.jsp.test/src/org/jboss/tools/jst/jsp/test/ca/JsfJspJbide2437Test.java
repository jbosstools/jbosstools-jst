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

public class JsfJspJbide2437Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide1704Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting.xhtml";
	
	private static final String TAG_NAME = "ui:composition";
	private static final String ATTR_NAME = "template";

	public static Test suite() {
		return new TestSuite(JsfJspJbide2437Test.class);
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

	public void testJsfJspJbide2437 () {
		try {
			JobUtils.waitForIdle();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		openEditor(PAGE_NAME);
		
		IStructuredDocumentRegion[] regions = ((IStructuredDocument)document).getStructuredDocumentRegions();
		
		boolean fUiCompositionTagFound = false;
		for (int i = 0; i < regions.length && !fUiCompositionTagFound; i++) {
			IStructuredDocumentRegion sdRegion = regions[i];
			ITextRegionList list = sdRegion.getRegions();

			// find <ui:composition tag
			if (!fUiCompositionTagFound) {
				if (DOMRegionContext.XML_TAG_OPEN == list.get(0).getType() &&
					DOMRegionContext.XML_TAG_NAME == list.get(1).getType() &&
					TAG_NAME.equals(sdRegion.getFullText(list.get(1)).trim())) {
					fUiCompositionTagFound = true;
					
					// find TEMPLATE attribute of <ui:composition tag
					boolean fUiCompositionTemplateAttributeFound = false;
					for (int j = 2; j < list.size(); j++) {
						ITextRegion region = list.get(j);
						if (!fUiCompositionTemplateAttributeFound) {
							if (DOMRegionContext.XML_TAG_ATTRIBUTE_NAME == region.getType() &&
									ATTR_NAME.equalsIgnoreCase(sdRegion.getFullText(list.get(j)).trim())) {
								fUiCompositionTemplateAttributeFound = true;
							}
							continue;
						} else {
							if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE == region.getType()) {
								String valueText = sdRegion.getFullText(region);
								int openQuoteIndex = valueText == null ? 0 : valueText.indexOf('\"');
								if (openQuoteIndex == -1) 
									openQuoteIndex = valueText == null ? 0 : valueText.indexOf('\'');

								assertFalse("There is no value defined for template attribute of tag ui:composition.", (openQuoteIndex == -1));

								int offsetToTest = sdRegion.getStartOffset(region) + openQuoteIndex + 1;
								
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
								
								boolean bELProposalsFound = false;
								boolean bTemplatePathProposalsFound = false;
								for (int k = 0; 
										result != null && k < result.length &&
										(!bELProposalsFound || !bTemplatePathProposalsFound); k++) {
									if (result[k] instanceof AutoContentAssistantProposal) {
										AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)result[k];
										
										// Test the display string for the proposals - it has to shown the thmplate path beginning or EL-expression beginning
										// because the CA is started the calculation from the very beginning of the attribute value.
										String dispString = proposal.getDisplayString();
										
										assertFalse("The CA proposal returned NULL display string.", (dispString == null));
										
										if (dispString.startsWith("#{") || dispString.startsWith("${")) {
											bELProposalsFound = true;
										} else if (dispString.indexOf("/") != -1) {
											bTemplatePathProposalsFound = true;
										}
									}
								}
									
								// There should be proposals for template paths 
								assertTrue("Content Assistant peturned no proposals for template paths.", bTemplatePathProposalsFound);
								// There should be proposals for EL-expresions 
								assertTrue("Content Assistant peturned no proposals for template paths.", bELProposalsFound);
								
								break;
							}
						}
					}

				}
				continue;
			}
			
			break;
		}
		
		closeEditor();
	}

}
