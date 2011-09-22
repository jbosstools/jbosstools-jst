/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.test;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkDetector;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.test.util.WorkbenchUtils;

public class CSSStylesheetOpenOnTest extends TestCase {
	private static final String PROJECT_NAME = "OpenOnTest";
	private static final String PAGE_NAME =  PROJECT_NAME+"/WebContent/index.html";
	private static final String MEDIA_PAGE_NAME =  PROJECT_NAME+"/WebContent/indexWithMediaRules.html";
	
	public IProject project = null;

	protected void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	protected void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}

	public CSSStylesheetOpenOnTest() {
		super("HTML OpenOn on CSS Stylesheets test");
	}

	public void testCSSStylesheetOpenOn() throws PartInitException, BadLocationException {
		final String editorName = "style.css";
		final String tagName = "link";  
		final String valueToFind = "style.css";
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();

		IEditorPart editor = WorkbenchUtils.openEditor(PAGE_NAME);
		if (editor != null) openedEditors.add(editor);
		assertTrue(editor instanceof JSPMultiPageEditor);
		try {
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
				
			IDocument document = viewer.getDocument();
			IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
					tagName, true, true, false, false);
			assertNotNull("Tag:"+tagName+" not found",reg);
			
			reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
					valueToFind, true, true, false, false);
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

	public void testCSSClassOpenOn() throws PartInitException, BadLocationException {
		final String editorName = "style.css";
		final String tagName = "div";  
		final String valueToFind = "red";  
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();
		
		IEditorPart editor = WorkbenchUtils.openEditor(PAGE_NAME);
		if (editor != null) openedEditors.add(editor);
		try {
			assertTrue(editor instanceof JSPMultiPageEditor);
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
				
			IDocument document = viewer.getDocument();
			IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
					tagName, true, true, false, false);
			assertNotNull("Tag:"+tagName+" not found",reg);
			
			reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
					valueToFind, true, true, false, false);
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
	
	public void testCSSClassOpenOnWithMediaRules() throws PartInitException, BadLocationException {
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();
		final String tagName = "<p class=";  
		final String[] valuesToFind = {
					"common", "display", "printer", "displayAndPrinter",
					"inpageCommon", "inpageDisplay", "inpagePrinter", "inpageDisplayAndPrinter"};
		final String[] editorNames = {
				"styleWithMediaRules.css", "styleWithMediaRules.css", "styleWithMediaRules.css", "styleWithMediaRules.css",
				"indexWithMediaRules.html", "indexWithMediaRules.html", "indexWithMediaRules.html", "indexWithMediaRules.html"};
		final int[] editorSelectionOffsets = {
				0, 81, 167, 253, 22, 106, 194, 282};
		final int[] editorSelectionLengths = {
				57, 58, 51, 39, 63, 64, 57, 45};
		
		IEditorPart editor = WorkbenchUtils.openEditor(MEDIA_PAGE_NAME);
		if (editor != null) openedEditors.add(editor);
		try {
			assertTrue(editor instanceof JSPMultiPageEditor);
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
				
			IDocument document = viewer.getDocument();
			for (int i = 0; i < valuesToFind.length; i++) {
				IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
						tagName, true, true, false, false);
				assertNotNull("Tag:"+tagName+" not found",reg);
				
				reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
						valuesToFind[i], true, true, false, false);
				assertNotNull("Value to find '"+valuesToFind[i]+"' not found",reg);
				
				IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, reg, true); // new Region(reg.getOffset() + reg.getLength(), 0)
				
				assertTrue("Hyperlinks for value '"+valuesToFind[i]+"' are not found",(links != null && links.length > 0));
				
				boolean found = false;
				for(IHyperlink link : links){
					assertNotNull(link.toString());
					
					link.open();
					
					IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if (resultEditor != null) openedEditors.add(resultEditor);
					if(editorNames[i].equals(resultEditor.getTitle())){
						StructuredTextEditor stEditor = null;
						if (resultEditor instanceof StructuredTextEditor) {
							stEditor = (StructuredTextEditor)resultEditor;
						} else if (resultEditor instanceof JSPMultiPageEditor) {
							stEditor = ((JSPMultiPageEditor)resultEditor).getSourceEditor();
						}
						assertNotNull("Unexpected Editor is openned: " + resultEditor.getTitle() + " [" + resultEditor.getClass().getName() + "]", stEditor);
						ISelection selection = stEditor.getSelectionProvider().getSelection();
						assertFalse("Required CSS Rule is not selected", selection.isEmpty());
						if (selection instanceof TextSelection) {
							TextSelection textSelection = (TextSelection)selection;
							assertTrue("Required CSS Rule is not selected", 
									(textSelection.getOffset() == editorSelectionOffsets[i] && textSelection.getLength() == editorSelectionLengths[i]));
							found = true;
							break;
						}
					}
				}
				assertTrue("OpenOn have not opened "+editorNames[i]+" editor",found);
			}
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
