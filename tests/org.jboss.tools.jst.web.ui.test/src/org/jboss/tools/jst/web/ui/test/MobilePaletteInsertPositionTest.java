/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.model.PaletteItem;

public class MobilePaletteInsertPositionTest extends AbstractPaletteEntryTest implements JQueryConstants {
	IEditorPart editor = null;

	public MobilePaletteInsertPositionTest() {}

	protected void tearDown() throws Exception {
		if(currentDialog != null) {
			currentDialog.close();
		}
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	public void testPage() throws Exception {
		IFile f = project.getFile("p14_1.html");
		editor = openEditor("p14_1.html");
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		ToolEntry entry = findEntry(getPaletteViewer(), JQM_CATEGORY, "Page");
		assertNotNull(entry);
		PaletteItem paletteItem = (PaletteItem)entry;
		String content = FileUtil.getContentFromEditorOrFile(f);

		String ancor = "<!--page-1-begin-->";
		int beforePage = content.indexOf(ancor) + ancor.length();
		int afterPage = content.indexOf("<!--page-1-end-->");

		int offset = content.indexOf("footer");
		assertPositionCorrection(paletteItem, document, offset, afterPage);

		offset = content.indexOf("header");
		assertPositionCorrection(paletteItem, document, offset, beforePage);
		
		offset = content.indexOf("button-1") + 5;
		assertPositionCorrection(paletteItem, document, offset, afterPage);

		offset = content.indexOf("\"content");
		assertPositionCorrection(paletteItem, document, offset, beforePage);
	
		offset = 0;
		int bodyBeginning = content.indexOf("<body>") + 6;
		assertPositionCorrection(paletteItem, document, offset, bodyBeginning);
		
		int bodyEnding = content.indexOf("</body>");
		offset = bodyEnding + 5;
		assertPositionCorrection(paletteItem, document, offset, bodyEnding);
	}

	public void testFooter() throws Exception {
		IFile f = project.getFile("p14_2.html");
		editor = openEditor("p14_2.html");
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		ToolEntry entry = findEntry(getPaletteViewer(), JQM_CATEGORY, "Footer Bar");
		assertNotNull(entry);
		PaletteItem paletteItem = (PaletteItem)entry;
		String content = FileUtil.getContentFromEditorOrFile(f);

		int afterContent = content.indexOf("<!--content-end-->");
		
		int offset = 0;
		assertPositionCorrection(paletteItem, document, offset, afterContent);

		offset = content.indexOf("header");
		assertPositionCorrection(paletteItem, document, offset, afterContent);

		offset = content.indexOf("button-1");
		assertPositionCorrection(paletteItem, document, offset, afterContent);
	}

	public void testInsertDropInsideComment() throws Exception {
		IFile f = project.getFile("p14_3.html");
		editor = openEditor("p14_3.html");
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		ToolEntry entry = findEntry(getPaletteViewer(), JQM_CATEGORY, "Text Input");
		
		assertNotNull(entry);
		PaletteItem paletteItem = (PaletteItem)entry;
		String content = FileUtil.getContentFromEditorOrFile(f);

		int afterComment = content.indexOf("<!--after comment-->");
		String ancor = "<!--before comment-->";
		int beforeComment = content.indexOf(ancor) + ancor.length();
		
		int offset = content.indexOf("word1");
		assertPositionCorrection(paletteItem, document, offset, beforeComment);

		offset = content.indexOf("word2");
		assertPositionCorrection(paletteItem, document, offset, afterComment);
	}

	void assertPositionCorrection(PaletteItem paletteItem, IDocument document, int offset, int expectedOffset) {
		int newOffset = PaletteInsertHelper.getInstance().correctOffset(document, offset, paletteItem.getXModelObject().getPath());
		assertEquals(expectedOffset, newOffset);
	}
}
