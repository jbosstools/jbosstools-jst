/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.test.ca;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.web.ui.base.test.ca.ContentAssistantTestCase;
import org.jboss.tools.jst.web.ui.internal.editor.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * 
 * JUnit Test Cases for Test JBIDE-12824
 * 
 * @author Victor V. Rubezhny
 *
 */
public class JstJspNonAutomaticProposalInsertionTest extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "JsfJbide1641Test"; //$NON-NLS-1$
	private static final String PAGE_NAME = "/WebContent/pages/greeting.xhtml"; //$NON-NLS-1$
	private static final String MULTIPLE_PROPOSAL_PREFIX = "<ui:c"; //$NON-NLS-1$
	private static final String SINGLE_PROPOSAL_PREFIX = "<ui:composition"; //$NON-NLS-1$
	private static final String PROPOSAL_TO_APPLY_STRING = "ui:composition"; //$NON-NLS-1$
	private static final String TAG_TO_INSERT = "<ui:component />"; //$NON-NLS-1$
	private static final String SINGLE_PROPOSAL_IN_LIST_PREFIX = "<ui:componen"; //$NON-NLS-1$
	private static final String SINGLE_PROPOSAL_CHECK_STRING = "ui:component"; //$NON-NLS-1$
	
	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

	public void testNonAutomaticProposalsInsertion () {
		openEditor(PAGE_NAME);
		
		try {
			// Find start of a tag that starts with "<ui:c"
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf(MULTIPLE_PROPOSAL_PREFIX));
			int offsetToTest = start + MULTIPLE_PROPOSAL_PREFIX.length();
			
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1));
			
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));
	
			boolean bAtLeastOneProposalFound = false;
			for (ICompletionProposal p : res) {
				if (!(p instanceof AutoContentAssistantProposal)) 
					continue;
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
				String proposalString = proposal.getReplacementString();
				assertFalse("Proposal '" + proposalString + "' is Auto Insertable, which is wrong!", proposal.isAutoInsertable());
				bAtLeastOneProposalFound = true;
			}
			assertTrue("The proposal to apply not found.", bAtLeastOneProposalFound);
		} finally {
			closeEditor();
		}
	}

	public void testASingleProposalInContentAssist() {
		openEditor(PAGE_NAME);
		
		try {
			// Find start of a tag that starts with "<ui:composition"
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf(SINGLE_PROPOSAL_PREFIX));
			
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1));
			
			// Insert new <ui:component /> tag
			String documentContentModified = documentContent.substring(0, start) +
					TAG_TO_INSERT + documentContent.substring(start);
				jspTextEditor.setText(documentContentModified);
			JobUtils.waitForIdle();

			// That should be offset in the new document (will call content assist at the end of "<ui:componen" text 
			// (one char before the end of the tag name)
			int offsetToTest = start + SINGLE_PROPOSAL_IN_LIST_PREFIX.length();

			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertNotNull("Content Assistant returned no proposals", res);
			assertEquals(1, res.size());
	
			boolean bProposalToCheckFound = false;
			for (ICompletionProposal p : res) {
				if (!(p instanceof AutoContentAssistantProposal)) 
					continue;
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
				String proposalString = proposal.getReplacementString();
				
				if (SINGLE_PROPOSAL_CHECK_STRING.equals(proposalString)) {
					bProposalToCheckFound = true;
					break;
				}
			}
			assertTrue("The proposal to check not found.", bProposalToCheckFound);
			
			
			try {
				JobUtils.waitForIdle();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Waiting for the jobs to complete has failed.", false);
			} 

			// The document content should not be changed
			// This means that NO autocompletion was invoked
			String documentUpdatedContent = document.get();
			assertTrue("The proposal replacement is failed.", documentContentModified.equals(documentUpdatedContent));
		} finally {
			closeEditor();
		}
	}

	public void testInsertionOfATagWithTheSameName () {
		openEditor(PAGE_NAME);
		
		try {
			// Find start of a tag that starts with "<ui:composition"
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf(SINGLE_PROPOSAL_PREFIX));
			int offsetToTest = start + SINGLE_PROPOSAL_PREFIX.length();
			
			assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1));
			
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));
	
			boolean bProposalToApplyFound = false;
			for (ICompletionProposal p : res) {
				if (!(p instanceof AutoContentAssistantProposal)) 
					continue;
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
				String proposalString = proposal.getReplacementString();
				
				if (PROPOSAL_TO_APPLY_STRING.equals(proposalString)) {
					bProposalToApplyFound = true;
					proposal.apply(document);
					break;
				}
			}
			assertTrue("The proposal to apply not found.", bProposalToApplyFound);
			
			try {
				JobUtils.waitForIdle();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Waiting for the jobs to complete has failed.", false);
			} 

			// The document content should not be changed
			String documentUpdatedContent = document.get();
			assertTrue("The proposal replacement is failed.", documentContent.equals(documentUpdatedContent));
		} finally {
			closeEditor();
		}
	}
}
