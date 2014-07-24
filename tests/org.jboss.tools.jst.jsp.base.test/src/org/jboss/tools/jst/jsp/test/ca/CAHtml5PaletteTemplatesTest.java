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
package org.jboss.tools.jst.jsp.test.ca;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public abstract class CAHtml5PaletteTemplatesTest extends ContentAssistantTestCase {
	   private static final String PROJECT_NAME = "SimpleProject"; //$NON-NLS-1$

	public CAHtml5PaletteTemplatesTest() {}

	@Override
	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

	@Override
	public void tearDown() throws Exception {
		closeEditor();
		super.tearDown();
	}

	protected abstract String getPageName();

	protected abstract PaletteTagLibrary getTagLib();

	protected String decorateDisplay(String display) {
		return display;
	}

	public void testIcon() {
		PaletteTagLibrary lib = getTagLib();
		assertNotNull(lib.getImage());
	}

	public void doTestTemplate(String prefix, String template, String expectedDisplayString, int expectedIndexInList) throws Exception {
		openEditor(getPageName());
		assertNotNull("Document must not be null", document);
		String search = prefix + template;

		IRegion reg = null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, search, true, false, false, false); //$NON-NLS-1$
		} catch (BadLocationException e) {
			fail("Cannot find start of value text: " + e.getLocalizedMessage());
			}
		assertNotNull("Cannot find a text region to test", reg);
		int start = reg.getOffset() + search.length();
		   
		List<ICompletionProposal> res = new ArrayList<ICompletionProposal>(CATestUtil.collectProposals(contentAssistant, viewer, start));
		removeCopiesByDisplay(res);

		assertFalse(res.isEmpty());
		assertEquals(expectedDisplayString, res.get(expectedIndexInList).getDisplayString());		
	}

	private void removeCopiesByDisplay(List<ICompletionProposal> res) {
		Set<String> set = new HashSet<String>();
		Iterator<ICompletionProposal> i = res.iterator();
		while(i.hasNext()) {
			String s = i.next().getDisplayString();
			if(set.contains(s)) {
				i.remove();
			} else {
				set.add(s);
			}
		}
	}
}
