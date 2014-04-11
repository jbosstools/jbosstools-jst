/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.test;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.test.ca.HTML5Test;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary;
import org.jboss.tools.jst.web.ui.palette.internal.RunnablePaletteItem;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * @author Alexey Kazakov
 */
public abstract class PaletteCATest extends HTML5Test {

	@Override
	protected String getFilePath() {
		return "WebContent/pages/jquery/jQueryMobile.html";
	}

	protected void setUp() throws Exception {
		super.setUp();
		openEditor(getFilePath());
	}

	public void openEditor(String fileName) {
		IFile testfile = testProject.getFile(fileName);
		assertTrue("Test file doesn't exist " + testfile, testfile.isAccessible());
		WorkbenchUtils.openEditor(testProject.getName() + "/" + fileName); //$NON-NLS-1$
	}

	@Override
	public void testCustomTagLibs() {
		ICustomTagLibrary[] libs = CustomTagLibManager.getInstance().getLibraries();
		int count = 0;
		for (ICustomTagLibrary lib : libs) {
			if(getTaglibUri().equals(lib.getURI())) {
				count++;
			}
		}
		assertEquals("Some custom tag librarties " + getTaglibUri() + " are not loaded.", getLibNumbers(), count);
	}

	abstract protected int getLibNumbers();

	public void testItems() {
		Collection<RunnablePaletteItem> items = getItems();
		String[] labels = new String[items.size()];
		int i = 0;
		for (RunnablePaletteItem item : items) {
			labels[i++] = PaletteTagLibrary.getLabel(item);
		}
		KbQuery query = createKbQuery(KbQuery.Type.TAG_NAME, new KbQuery.Tag[]{createTag("tag")}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, labels);
	}

	protected abstract Collection<RunnablePaletteItem> getItems();
}