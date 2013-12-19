/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class SwitchPaletteTest extends AbstractPaletteEntryTest implements JQueryConstants {
	IEditorPart editor = null;

	public SwitchPaletteTest() {}

	public void tearDown() {
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
	}
	
	public void testPaletteSwitch() throws Exception {
		editor = openEditor("d.html");
		PaletteViewer viewer = getPaletteViewer();
		ToolEntry toolEntry = findEntry(viewer, JQueryConstants.JQM_CATEGORY, "Page");
		assertNotNull(toolEntry);
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		document.replace(13, 1, "x");
		viewer = getPaletteViewer();
		toolEntry = findEntry(viewer, JQueryConstants.JQM_CATEGORY, "Page");
		assertNull(toolEntry);
		document.replace(13, 1, "l");
		viewer = getPaletteViewer();
		toolEntry = findEntry(viewer, JQueryConstants.JQM_CATEGORY, "Page");
		assertNotNull(toolEntry);
	}
	
}
