/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.ca;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.test.util.TestProjectProvider;


/**
* Test case testing http://jira.jboss.com/jira/browse/JBIDE-12175 issue.
* 
* @author Victor Rubezhny
*
*/
public class JstCAOnCustomPrefixesTest  extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$
	private static final String PAGE_NAME = "/WebContent/pages/xhtml_page.xhtml"; //$NON-NLS-1$
	private static final String PREFIXES_PAGE_NAME = "/WebContent/pages/prefixes.xhtml"; //$NON-NLS-1$
	private static final String TAG_OPEN_STRING = "<"; //$NON-NLS-1$
	private static final String PREFIX_STRING = "custom1:commandBu"; //$NON-NLS-1$
	private static final String TAG_PROPOSAL_TO_FIND = "custom1:commandButton"; //$NON-NLS-1$
	private static final String TAG_INSERTION_STRING = TAG_OPEN_STRING + PREFIX_STRING;
	private static final String PREFIX2_STRING = "custom2:commandBu"; //$NON-NLS-1$
	private static final String TAG2_PROPOSAL_TO_FIND = "custom2:commandButton"; //$NON-NLS-1$
	private static final String TAG2_XNLNS_TO_FIND = "xmlns:custom2=\"http://java.sun.com/jsf/html\"";
	private static final String TAG2_INSERTION_STRING = TAG_OPEN_STRING + PREFIX2_STRING;
	private static final String XMLNS_ATTRIBUTE_INSERTION_STRING = "xmlns:"; //$NON-NLS-1$
	private static final String XMLNS_ATTRIBUTE_INSERTION_STRING_2 = "xmln"; //$NON-NLS-1$
	private static final String[] XMLNS_ATTRIBUTE_PROPOSALS_TO_FIND = {
				"xmlns:custom1=\"http://java.sun.com/jsf/html\"", //$NON-NLS-1$
				"xmlns:custom2=\"http://java.sun.com/jsf/html\"", //$NON-NLS-1$
				"xmlns:custom3=\"http://java.sun.com/jsf/html\"", //$NON-NLS-1$
				"xmlns:custom4=\"http://java.sun.com/jsf/html\"" //$NON-NLS-1$
		};
	private static final String CUSTOM_ATTRIBUTE_VALUE_INSERTION_STRING = "xmlns:custom3=\""; //$NON-NLS-1$
	private static final String CUSTOM_ATTRIBUTE_VALUE_PROPOSAL_TO_FIND = "\"http://java.sun.com/jsf/html\""; //$NON-NLS-1$

	public static Test suite() {
		return new TestSuite(JstCAOnCustomPrefixesTest.class);
	}

	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
		IFile file = project.getFile(PREFIXES_PAGE_NAME);
		PageContextFactory.createPageContext(file);
	}
	
	public void testCAOnCustomPrefixForTag() {
		openEditor(PAGE_NAME);
		
		// Find start of <f:view> tag
		String documentContent = document.get();
		int start = (documentContent == null ? -1 : documentContent.indexOf("<f:view")); //$NON-NLS-1$
		int offsetToTest = start + TAG_INSERTION_STRING.length();
		
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
		
		String documentContentModified = documentContent.substring(0, start) +
			TAG_INSERTION_STRING + documentContent.substring(start);
		
		jspTextEditor.setText(documentContentModified);
		
		try {
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {TAG_PROPOSAL_TO_FIND});
		} finally {
			closeEditor();
		}
	}

	public void testCAOnCustomPrefixForTagInsertion() {
		openEditor(PAGE_NAME);
		
		// Find start of <f:view> tag
		String documentContent = document.get();
		int start = (documentContent == null ? -1 : documentContent.indexOf("<f:view")); //$NON-NLS-1$
		int offsetToTest = start + TAG2_INSERTION_STRING.length();
		
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
		
		String documentContentModified = documentContent.substring(0, start) +
			TAG2_INSERTION_STRING + documentContent.substring(start);
		
		jspTextEditor.setText(documentContentModified);
		
		try {
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {TAG2_PROPOSAL_TO_FIND});
			
			boolean found = false;
			for (ICompletionProposal p : res) {
				if (!(p instanceof AutoContentAssistantProposal))
					continue;
				
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
	            if(proposal.getDisplayString().equals(TAG2_PROPOSAL_TO_FIND)){
	                found = true;
	                proposal.apply(viewer, '\0', 0, proposal.getReplacementOffset());
	                break;
	            }
			}
			assertTrue("Cannot find the requested proposal among the proposals returned by the Content Assisant", found); //$NON-NLS-1$
			
			documentContent = document.get(); // Get the updated document content
			int htmlStart = (documentContent == null ? -1 : documentContent.indexOf("<html")); //$NON-NLS-1$
			// Find end of <html> tag attributes
			int htmlEnd = (documentContent == null ? -1 : documentContent.indexOf(">", start)); //$NON-NLS-1$
			String htmlTagContent = documentContent.substring(htmlStart, htmlEnd + 1);
			
			assertTrue("Cannot find the '" + TAG2_XNLNS_TO_FIND + "' attribute/value in <html> tag!", (htmlTagContent.indexOf(TAG2_XNLNS_TO_FIND) != -1));
			
		} finally {
			closeEditor();
		}
	}

	public void testCAOnCustomPrefixForXMLNSAttributeOnRootTag() {
		openEditor(PAGE_NAME);
		
		try {		
			// Find start of <html> tag
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf("<html")); //$NON-NLS-1$
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
			// Find end of <html> tag attributes
			start = (documentContent == null ? -1 : documentContent.indexOf(">", start)); //$NON-NLS-1$
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$

			//
			// On a Root Tags the "xmlns:" proposals should be shown for any prefix part (xmlns:...)
			//
			int offsetToTest = start + 1 + XMLNS_ATTRIBUTE_INSERTION_STRING.length();
			
			String documentContentModified = documentContent.substring(0, start) + ' ' +
				XMLNS_ATTRIBUTE_INSERTION_STRING + ' ' + documentContent.substring(start);
			
			jspTextEditor.setText(documentContentModified);

			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), XMLNS_ATTRIBUTE_PROPOSALS_TO_FIND);

			//
			// On a Root Tags the "xmlns:" proposals should be shown for any prefix part
			//
			offsetToTest = start + 1 + XMLNS_ATTRIBUTE_INSERTION_STRING_2.length();
			documentContentModified = documentContent.substring(0, start) + ' ' +
				XMLNS_ATTRIBUTE_INSERTION_STRING_2 + ' ' + documentContent.substring(start);
			
			jspTextEditor.setText(documentContentModified);
			
			res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
			
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), XMLNS_ATTRIBUTE_PROPOSALS_TO_FIND);
		} finally {
			closeEditor();
		}
	}

	public void testCAOnCustomPrefixForXMLNSAttributeOnNotARootTag() {
		openEditor(PAGE_NAME);
		
		try {		
			// Find start of <h:inputText> tag
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf("<h:inputText")); //$NON-NLS-1$
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
			// Find end of <h:inputText> tag attributes
			start = (documentContent == null ? -1 : documentContent.indexOf(">", start)); //$NON-NLS-1$
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
			
			//
			// On non-Root Tags the "xmlns:" proposals should be shown only is prefix has "xmlns:" beginning
			//
			int offsetToTest = start + 1 + XMLNS_ATTRIBUTE_INSERTION_STRING.length();
			
			String documentContentModified = documentContent.substring(0, start) + ' ' +
				XMLNS_ATTRIBUTE_INSERTION_STRING + ' ' + documentContent.substring(start);
			
			jspTextEditor.setText(documentContentModified);

			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), XMLNS_ATTRIBUTE_PROPOSALS_TO_FIND);

			//
			// On non-Root Tags the "xmlns:" proposals should be shown only is prefix has "xmlns:" beginning
			// If there is no "xmlns:" prefix is typed in then no "xmlns:"-proposals should be shown
			//
			offsetToTest = start + 1 + XMLNS_ATTRIBUTE_INSERTION_STRING_2.length();
			documentContentModified = documentContent.substring(0, start) + ' ' +
				XMLNS_ATTRIBUTE_INSERTION_STRING_2 + ' ' + documentContent.substring(start);
			
			jspTextEditor.setText(documentContentModified);
			
			res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
			
			assertTrue("Content Assistant returned no proposals", res != null); //$NON-NLS-1$
			
			if (res.size() > 0)
				checkFalseResult(res.toArray(new ICompletionProposal[0]), XMLNS_ATTRIBUTE_PROPOSALS_TO_FIND);
		} finally {
			closeEditor();
		}
	}

	public void testCAOnCustomPrefixForXMLNSAttributeValue() {
		openEditor(PAGE_NAME);
		
		// Find start of <html> tag
		String documentContent = document.get();
		int start = (documentContent == null ? -1 : documentContent.indexOf("<html")); //$NON-NLS-1$
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
		// Find end of <html> tag attributes
		start = (documentContent == null ? -1 : documentContent.indexOf(">", start)); //$NON-NLS-1$
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$
		
		int offsetToTest = start + 1 + CUSTOM_ATTRIBUTE_VALUE_INSERTION_STRING.length();
		
		String documentContentModified = documentContent.substring(0, start) + ' ' +
			CUSTOM_ATTRIBUTE_VALUE_INSERTION_STRING + ' ' + documentContent.substring(start);
		
		jspTextEditor.setText(documentContentModified);
		
		try {
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {CUSTOM_ATTRIBUTE_VALUE_PROPOSAL_TO_FIND});
		} finally {
			closeEditor();
		}
	}

    private void checkResult(ICompletionProposal[] rst, String[] proposals) {
        for ( int i = 0 ; i < proposals.length ; i ++ ){
           assertTrue("Should be in proposals list",isInResultList(rst,proposals[i])); //$NON-NLS-1$
        }
        
    }

    private void checkFalseResult(ICompletionProposal[] rst, String[] proposals) {
        for ( int i = 0 ; i < proposals.length ; i ++ ){
           assertFalse("Should not be in proposals list",isInResultList(rst,proposals[i])); //$NON-NLS-1$
        }
        
    }

    private boolean isInResultList(ICompletionProposal[] rst, String string) {
        boolean r = false;
        
        for(ICompletionProposal cp:rst){
            if(cp.getDisplayString().equals(string)){
                r = true;
                break;
            }
        }
        return r;
    }

}
