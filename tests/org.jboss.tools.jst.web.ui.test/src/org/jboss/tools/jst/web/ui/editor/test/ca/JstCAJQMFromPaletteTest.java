/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
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
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.jst.web.ui.internal.editor.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstCAJQMFromPaletteTest  extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "StaticWebProject";
	private static final String PAGE_NAME = "WebContent/contentassist/jqmCAFromPalette.html";
	
	private static final String START_TEXT = "<span>"; 		// No other <span/> tags should be inserted
	private static final String FINISH_TEXT = "</span>";	// No other <span/> tags should be inserted
	
	private static final String PAGE_TEMPLATE_NAME = "Page - jQuery Mobile 1.4";
	private static final String[] PAGE_TEMPLATE_ROWS = {
		"<div data-role=\"page\" id=\"page-1\">",
		"<div data-role=\"header\">",
		"<h1>Page Title</h1>",
		"</div>",
		"<div data-role=\"content\">",
		"<p>Page content goes here.</p>",
		"</div>",
		"<div data-role=\"footer\">",
		"<h4>Page Footer</h4>",
		"</div>",
		"</div>"
	};
	
	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}
	
	public void testJQMCAFromPaletteInsertBefore() {
		// Make a test rows when the page template rows are placed inside the text
		String[] testRows = new String[PAGE_TEMPLATE_ROWS.length + 2];
		System.arraycopy(PAGE_TEMPLATE_ROWS, 0, testRows, 0, PAGE_TEMPLATE_ROWS.length);
		testRows[testRows.length - 2] = START_TEXT;
		testRows[testRows.length - 1] = FINISH_TEXT;
		
		// Call CA in TEXT region
		doJQMCAFromPaletteTest(testRows, null, START_TEXT, 0);
		// Call CA in TAG (not closed) region
		doJQMCAFromPaletteTest(testRows, "<di", START_TEXT, 0);
	}
	
	public void testJQMCAFromPaletteInsertInside() {
		// Make a test rows when the page template rows are placed inside the text
		String[] testRows = new String[PAGE_TEMPLATE_ROWS.length + 2];
		testRows[0] = START_TEXT;
		System.arraycopy(PAGE_TEMPLATE_ROWS, 0, testRows, 1, PAGE_TEMPLATE_ROWS.length);
		testRows[testRows.length - 1] = FINISH_TEXT;
		
		// Call CA in TEXT region
		doJQMCAFromPaletteTest(testRows, null, START_TEXT, START_TEXT.length());
		// Call CA in TAG (not closed) region
		// this should be un-commented upon fixing CA issues when doing CA in the following places:
		// 1) <p><di|</p>
		// 2) <di|</p>
		// (No proposals returned in both cases)
		//
//		doJQMCAFromPaletteTest(testRows, "<di", START_TEXT, START_TEXT.length());
	}

	public void testJQMCAFromPaletteInsertAfter() {
		// Make a test rows when the page template rows are placed inside the text
		String[] testRows = new String[PAGE_TEMPLATE_ROWS.length + 2];
		testRows[0] = START_TEXT;
		testRows[1] = FINISH_TEXT;
		System.arraycopy(PAGE_TEMPLATE_ROWS, 0, testRows, 2, PAGE_TEMPLATE_ROWS.length);
		
		// Call CA in TEXT region
		doJQMCAFromPaletteTest(testRows, null, FINISH_TEXT, FINISH_TEXT.length());
		// Call CA in TAG (not closed) region
		doJQMCAFromPaletteTest(testRows, "<di", FINISH_TEXT, FINISH_TEXT.length());
	}

	@SuppressWarnings("restriction")
	private void doJQMCAFromPaletteTest(String[] testRows, String insertString, String tagToFind, int offset) {
		openEditor(PAGE_NAME);
		try {
			// Find start of <ui:composition> tag
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf(tagToFind));
			int offsetToTest = start + offset;
			assertTrue("Cannot find the starting point [" + tagToFind + "] in the test file  \"" + PAGE_NAME + "\"", (start != -1));

			if (insertString != null) {
				String documentContentModified = documentContent.substring(0, offsetToTest) +
						insertString + documentContent.substring(offsetToTest);

				jspTextEditor.setText(documentContentModified);
				JobUtils.waitForIdle();
				
				// Update offsetToTest according the inserted string
				offsetToTest += insertString.length();
			}
			
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
			
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));
	
			boolean bPropoosalToApplyFound = false;
			for (ICompletionProposal p : res) {
				if (!(p instanceof AutoContentAssistantProposal)) 
					continue;
				
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
				String proposalString = proposal.getDisplayString();
	
				if (PAGE_TEMPLATE_NAME.equals(proposalString)) {
					bPropoosalToApplyFound = true;
					proposal.apply(document);
					break;
				}
			}
			assertTrue("The proposal to apply not found.", bPropoosalToApplyFound);
	
			try {
				JobUtils.waitForIdle();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Waiting for the jobs to complete has failed.", false);
			} 
			String documentUpdatedContent = document.get();
			checkResult(documentUpdatedContent, testRows);
		} finally {
			closeEditor();
		}
	}
	
	private void checkResult (String content, String[] testRows) {
		int rowsMatched = 0;
		for (int index = 0, i = 0, lastRowLength = 0; index >= 0 && i < testRows.length; i++) {
			index = content.indexOf(testRows[i], index + lastRowLength);
			lastRowLength = testRows[i].length();
			rowsMatched++;
		}
		assertEquals("The only " + rowsMatched + " of " + testRows.length + " match", testRows.length, rowsMatched);
	}
}
