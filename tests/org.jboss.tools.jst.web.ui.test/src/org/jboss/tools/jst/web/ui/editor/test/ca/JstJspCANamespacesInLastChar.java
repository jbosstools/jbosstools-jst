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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * 
 * @author Victor Rubezhny
 *
 */
public class JstJspCANamespacesInLastChar extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "JsfJbide1641Test";
	private static final String PAGE_NAME = "/WebContent/pages/inputname16432.xhtml";
	private static final String XMLNS_PREFIX_URI = "xmlns:prefix=";
	
	private static final String PREFIX_STRING = "<prefix:";
	
	private static final String UI_URI = "http://java.sun.com/jsf/facelets";
	private static final String[] CORRECT_UI_PROPOSALS = {
			"prefix:component",
			"prefix:define",
			"prefix:include"
		};
	
	private static final String CORE_URI = "http://java.sun.com/jsf/core";
	private static final String[] CORRECT_CORE_PROPOSALS = {
		"prefix:actionListener",
		"prefix:converter",
		"prefix:validator"
	};

	private static final String HTML_URI = "http://java.sun.com/jsf/html";
	private static final String[] CORRECT_HTML_PROPOSALS = {
		"prefix:column",
		"prefix:form",
		"prefix:inputText"
	};
	
	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

	public void testCANamespacesInLastChar() {
		openEditor(PAGE_NAME);
		try {
			List<String> wrongProposals = new ArrayList<String>();
			wrongProposals.addAll(Arrays.asList(CORRECT_HTML_PROPOSALS));
			wrongProposals.addAll(Arrays.asList(CORRECT_UI_PROPOSALS));
			doTestNamespaces(CORE_URI, CORRECT_CORE_PROPOSALS, wrongProposals.toArray(new String[0]));
			
			wrongProposals.clear();
			wrongProposals.addAll(Arrays.asList(CORRECT_CORE_PROPOSALS));
			wrongProposals.addAll(Arrays.asList(CORRECT_UI_PROPOSALS));
			doTestNamespaces(HTML_URI, CORRECT_HTML_PROPOSALS, wrongProposals.toArray(new String[0]));

			wrongProposals.clear();
			wrongProposals.addAll(Arrays.asList(CORRECT_CORE_PROPOSALS));
			wrongProposals.addAll(Arrays.asList(CORRECT_HTML_PROPOSALS));
			doTestNamespaces(UI_URI, CORRECT_UI_PROPOSALS, wrongProposals.toArray(new String[0]));
		} finally {
			closeEditor();
		}
	}

	private void doTestNamespaces(String uri, String[] correctProposals, String[] wrongProposals) {
		// Find start of <ui:composition> tag
		String documentContent = document.get();
		assertNotNull("Cannot get the document content for the test file  \"" + PAGE_NAME + "\"", documentContent);
		
		int xmlnsPrefixStart = documentContent.indexOf(XMLNS_PREFIX_URI);
		assertTrue("Cannot find the XMLNS starting point in the test file  \"" + PAGE_NAME + "\"", (xmlnsPrefixStart != -1));
		int xmlnsPrefixEnd = documentContent.indexOf('\"', xmlnsPrefixStart);
		assertTrue("Cannot find the XMLNS end point in the test file  \"" + PAGE_NAME + "\"", (xmlnsPrefixEnd != -1));
		xmlnsPrefixEnd = documentContent.indexOf('\"', xmlnsPrefixEnd + 1); // We need the closing quote index
		assertTrue("Cannot find the XMLNS end point in the test file  \"" + PAGE_NAME + "\"", (xmlnsPrefixEnd != -1));
		
		String documentModifiedContent = documentContent.substring(0, xmlnsPrefixStart + XMLNS_PREFIX_URI.length()) +
				'"' + uri + '"' + documentContent.substring(xmlnsPrefixEnd + 1);

		jspTextEditor.setText(documentModifiedContent);
		JobUtils.waitForIdle();
		documentContent = document.get();
		assertNotNull("Cannot get the document content for the test file  \"" + PAGE_NAME + "\"", documentContent);
		
		int start = documentContent.indexOf(PREFIX_STRING);
		assertTrue("Cannot find the starting point in the test file  \"" + PAGE_NAME + "\"", (start != -1));

		int offsetToTest = start + PREFIX_STRING.length();
		
		List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
		assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));

		for (String p : correctProposals) {
			assertTrue("Correct proposal <" + p + "> not found in CA result", containsProposal(res, p));
		}
		for (String p : wrongProposals) {
			assertFalse("Wrong proposal <" + p + "> found in CA result", containsProposal(res, p));
		}
	}
	
	private boolean containsProposal(List<ICompletionProposal> proposals, String proposal) {
		for (ICompletionProposal p : proposals) {
			if (p.getDisplayString().equals(proposal))
				return true;
		}
		return false;
	}
}