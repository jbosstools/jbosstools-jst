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
package org.jboss.tools.jst.web.ui.openon.test;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkDetector;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.test.util.WorkbenchUtils;

public class CSSClassNamesOpenOnTest extends TestCase {
	private static final String PROJECT_NAME = "OpenOnTest";
	private static final String PAGE_NAME_ABSOLUTE = PROJECT_NAME + "/WebContent/cssAbsoluteClassNames.html";
	private static final String PAGE_NAME_RELATIVE = PROJECT_NAME + "/WebContent/cssRelativeClassNames.html";
	private static final String EDITOR_NAME = "ionic.css";
	private static final String TAG_NAME = "div";
	private static final String CSS_CLASS_NAME_TEMPLATE = "bar";
	
	public IProject project = null;

	protected void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	protected void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}

	public CSSClassNamesOpenOnTest() {
		super("HTML OpenOn on CSS Class Names (relative and absolute) test for non-WTP projects");
	}

	public void testCSSClassNamesAbsoluteOpenOn() throws PartInitException, BadLocationException {
		doCSSClassNamesOpenOnTest(PAGE_NAME_ABSOLUTE, EDITOR_NAME);
	}
	
	public void testCSSClassNamesRelativeOpenOn() throws PartInitException, BadLocationException {
		doCSSClassNamesOpenOnTest(PAGE_NAME_RELATIVE, EDITOR_NAME);
	}
		
	private void doCSSClassNamesOpenOnTest(String pageName, String editorName) throws PartInitException, BadLocationException {
		final String tagName = "div";  
		final String valueToFind = "red";  
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();
		
		IEditorPart editor = WorkbenchUtils.openEditor(pageName);
		if (editor != null) openedEditors.add(editor);
		try {
			assertTrue(editor instanceof JSPMultiPageEditor);
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
			
			IDocument document = viewer.getDocument();
			IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
					TAG_NAME, true, true, false, false);
			assertNotNull("Tag:"+tagName+" not found",reg);
			
			reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
					CSS_CLASS_NAME_TEMPLATE, true, true, false, false);
			assertNotNull("Value to find:"+valueToFind+" not found",reg);
			
			IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, reg, true); // new Region(reg.getOffset() + reg.getLength(), 0)
			
			assertTrue("Hyperlinks for value '"+valueToFind+"' are not found",(links != null && links.length > 0));
			
			boolean found = false;
			for(IHyperlink link : links){
				assertNotNull(link.toString());
				
				link.open();
				
				IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				if (resultEditor != null) openedEditors.add(resultEditor);
				if(editorName.equals(resultEditor.getTitle())){
					found = true;
					return;
				}
			}
			assertTrue("OpenOn have not opened "+editorName+" editor",found);
		} finally {
			closeEditors(openedEditors);
		}
	}

	protected void closeEditors (HashSet<IEditorPart> editors) {
		if (editors == null || editors.isEmpty()) 
			return;
		for (IEditorPart editor : editors) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(editor, false);
		}
	}
	
}
