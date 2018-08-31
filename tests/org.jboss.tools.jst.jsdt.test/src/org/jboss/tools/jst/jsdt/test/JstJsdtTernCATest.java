/*******************************************************************************
 * Copyright (c) 2014-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsdt.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;

import tern.eclipse.ide.ui.contentassist.JSTernCompletionProposal;

/**
 * 
 * JUnit Test for Tern.java integration
 * 
 * @author Victor Rubezhny
 *
 */
public class JstJsdtTernCATest extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "JavaScriptProject"; //$NON-NLS-1$
	private static final String PAGE_NAME = "WebContent/index.html"; //$NON-NLS-1$
	private static final String ECMASCRIPT_PREFIX_STRING = 
			"// Test Script"; //$NON-NLS-1$
	private static final String[] ECMASCRIPT_PROPOSAL_STRINGS = {
		"Array : fn - ecmascript",  //$NON-NLS-1$
		"ArrayBuffer : fn - ecmascript",  //$NON-NLS-1$
		"ArrayBuffer(length) - ecmascript",  //$NON-NLS-1$
		"Boolean : fn - ecmascript",  //$NON-NLS-1$
		"Boolean(value) : bool - ecmascript"  //$NON-NLS-1$
	};
	private static final String BROWSER_PREFIX_STRING =
		"// Test Script"; //$NON-NLS-1$ 
	private static final String[] BROWSER_PROPOSAL_STRINGS = {
		"CanvasRenderingContext2D : CanvasRenderingContext2D - browser" //$NON-NLS-1$
	};
	
	public static Test suite() {
		return new TestSuite(JstJsdtAllTests.class);
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
	}

	protected void tearDown() throws Exception {
	}

	public void testJstJsdtTernCAOnEcmaScript() {
		doJstJsdtTernCATest(PAGE_NAME, ECMASCRIPT_PREFIX_STRING, ECMASCRIPT_PROPOSAL_STRINGS);
	}

	public void testJstJsdtTernCAOnBrowser() {
		doJstJsdtTernCATest(PAGE_NAME, BROWSER_PREFIX_STRING, BROWSER_PROPOSAL_STRINGS);
	}

	private void doJstJsdtTernCATest(String pageName, String prefix, String[] values) {
		openEditor(pageName);
		try {		
			String documentContent = document.get();
			int start = (documentContent == null ? -1 : documentContent.indexOf(prefix)); //$NON-NLS-1$
			int offsetToTest = start;// + prefix.length();
			
			assertTrue("Cannot find the starting point in the test file  \"" + pageName + "\"", (start != -1)); //$NON-NLS-1$ //$NON-NLS-2$

			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
/*
			if (res != null) {
				int i = 0;
				System.out.println("For prefix: " + prefix);
				for (ICompletionProposal p : res) {
					JSTernCompletionProposal proposal = (JSTernCompletionProposal)p;
					String proposalString = proposal.getDisplayString();
					System.out.println("#" + (i++) + ": " + proposalString);
				}
			}
*/
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			
			Set<String> vals = new HashSet<String>();
			vals.addAll(Arrays.asList(values));
					
			System.out.println("[doJstJsdtTernCATest] proposals: >>>");
			for (ICompletionProposal p : res) {
				assertTrue("Content Assistant returned proposals which type (" + p.getClass().getName() + ") differs from JSTernCompletionProposal", (p instanceof JSTernCompletionProposal));  //$NON-NLS-1$//$NON-NLS-2$
				
				JSTernCompletionProposal proposal = (JSTernCompletionProposal)p;
				String proposalString = proposal.getDisplayString();
				
				for (String value : values) {
					if (value.equals(proposalString)) {
						vals.remove(value);
					}
				}
				if (vals.size() == 0) {
					break;
				}
			}
			System.out.println("[doJstJsdtTernCATest] proposals: <<<");
			
			String rest = "";
			if (vals.size() > 0) {
				boolean first = true;
				for (String val : vals) {
					if (!first) {
						rest += ", ";
						first = false;
					}
					rest += "[" + val + "]";
				}
			}
			assertEquals("The proposal Display Strings is/are not found: " + rest, vals.size(), 0); 
		} finally {
			closeEditor();
		}
	}
}
