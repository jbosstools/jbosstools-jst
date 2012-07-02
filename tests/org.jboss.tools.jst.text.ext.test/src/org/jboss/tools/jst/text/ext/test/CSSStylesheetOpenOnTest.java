/******************************************************************************* 
 * Copyright (c) 2011-2012 Red Hat, Inc. 
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
import org.eclipse.jface.text.Region;
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
	private static final String SECOND_IN_A_ROW_PAGE_NAME =  PROJECT_NAME+"/WebContent/indexWithSecondClassInRule.html";
	private static final String RESOLVE_SELECTOR_PAGE_NAME =  PROJECT_NAME+"/WebContent/indexResolveSelector.html";
	private static final String PARENT_TO_CHILD_RESTRICTIONS_RESOLVE_SELECTOR_PAGE_NAME =  PROJECT_NAME+"/WebContent/indexParentToChildRestrictions.html";
	
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
	
	/*
	 * The test case for JBIDE-10104
	 */
	public void testCSSClassOpenOnWithASecondRuleNameInARow() throws PartInitException, BadLocationException {
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();
		final String textToFind = "class=\"style2\"";
		final String valueToFind = "style2";
		final String editorName = "secondClassName.css";
		final int[] editorSelectionOffsets = {
				0, 0, 0};
		final int[] editorSelectionLengths = {
				55, 55, 55};

		IEditorPart editor = WorkbenchUtils.openEditor(SECOND_IN_A_ROW_PAGE_NAME);
		if (editor != null) openedEditors.add(editor);
		try {
			assertTrue(editor instanceof JSPMultiPageEditor);
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
				
			IDocument document = viewer.getDocument();
			int startFrom = 0;
			for (int i = 0; i < editorSelectionOffsets.length; i++) {
				IRegion reg = new FindReplaceDocumentAdapter(document).find(startFrom,
						textToFind, true, true, false, false);
				assertNotNull("Tag: <div "+textToFind+"/> not found",reg);

				startFrom = reg.getOffset() + reg.getLength();

				reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
						valueToFind, true, true, false, false);
				assertNotNull("Tag: <div "+textToFind+"/> not found",reg);

				IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, reg, true); // new Region(reg.getOffset() + reg.getLength(), 0)
				
				assertTrue("Hyperlinks for value '"+valueToFind+"' are not found",(links != null && links.length > 0));
				
				boolean found = false;
				for(IHyperlink link : links){
					assertNotNull(link.toString());
					
					link.open();
					
					IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if (resultEditor != null) openedEditors.add(resultEditor);
					if(editorName.equals(resultEditor.getTitle())){
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
				assertTrue("OpenOn have not opened "+editorName+" editor",found);
			}
		} finally {
			closeEditors(openedEditors);
		}
	}
	
	public void testCSSClassResolveSelectorOpenOn() throws PartInitException, BadLocationException {
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();

		// CSS class names to be tested are placed one by one, 
		// so, for each next test we'll continue to search in document 
		// (We'll not search from beginning each time)
		//
		// Position to continue the search from
		int startFrom = 0;  

		// 'class="' - is the string to search
		final String TEXT_TO_SEARCH = "class=\"";
		
		// Valid CSS Stylesheet to be opened
		final String VALID_CSS_EDITOR_NAME = "styleResolveSelector.css";
		
		// Valid values for Text Selection after the open on is performed
		final String[] VALID_TEXT_SELECTIONS = new String[] {
				".styleA {color: #FF0000}",
				".styleA {color: #FF0000}",
				".styleA .styleB {color: #FF8000}",
				"div.styleA {color: #FF0080}",
				"div#div34.styleA {color: yellow}",
				"div[title=\"x\"].styleA {color: #0000FF}",
				"p input.styleA {color: #CCAA00}",
				"div p input.styleA {color: #CC00FF}",
				"[title=\"y\"] p input.styleA {color: #00CCFF}"
		};
		
		for (int i = 0; i < VALID_TEXT_SELECTIONS.length; i++) {
			IEditorPart editor = WorkbenchUtils.openEditor(RESOLVE_SELECTOR_PAGE_NAME);
			if (editor != null) openedEditors.add(editor);
			try {
				assertTrue(editor instanceof JSPMultiPageEditor);
				JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
				ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
					
				IDocument document = viewer.getDocument();
				IRegion reg = new FindReplaceDocumentAdapter(document).find(startFrom,
						TEXT_TO_SEARCH, true, true, false, false);
				assertNotNull("Attribute :" + TEXT_TO_SEARCH + " not found whyle search starting from " + startFrom, reg);

				startFrom = reg.getOffset() + reg.getLength();
				
				IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, new Region(startFrom, 0), true); // new Region(reg.getOffset() + reg.getLength(), 0)
				
				assertTrue("Hyperlinks not found for position " + startFrom,(links != null && links.length > 0));
				
				boolean found = false;
				for(IHyperlink link : links){
					assertNotNull(link.toString());
					
					link.open();
					
					IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if (resultEditor != null) openedEditors.add(resultEditor);
					if(VALID_CSS_EDITOR_NAME.equals(resultEditor.getTitle())){
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
							String selectionText = stEditor.getTextViewer().getDocument().get(textSelection.getOffset(), textSelection.getLength());
							assertTrue("Required CSS Rule is not selected", 
									(VALID_TEXT_SELECTIONS[i].equalsIgnoreCase(selectionText)));
							found = true;
							break;
						}
					}
				}
				assertTrue("OpenOn have not opened a valid selection in " + VALID_CSS_EDITOR_NAME + " editor",found);
			} finally {
				closeEditors(openedEditors);
			}
		}
	}
	
	public void testCSSClassParentToChildResolveRestrictions() throws PartInitException, BadLocationException {
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();

		// CSS class names to be tested are placed one by one, 
		// so, for each next test we'll continue to search in document 
		// (We'll not search from beginning each time)
		//
		// Position to continue the search from
		int startFrom = 0;  

		// 'class="' - is the string to search
		final String TEXT_TO_SEARCH = "class=\"";
		
		// Valid values for Text Selection after the open on is performed
		final String[][] VALID_TEXT_SELECTIONS = {
				{"styleParentToChildRestrictions.css", "[title=\"a\"].bc {background-color: aqua;}"},
				{"styleParentToChildRestrictions.css", "[title=\"a\"] .bc {background-color: green;}"},
				{"indexParentToChildRestrictions.html", ""},
				{"styleParentToChildRestrictions.css", "div * .s1 {background-color: yellow;}"},
				{"styleParentToChildRestrictions.css", "div p.s2 {background-color: brown;}"}
		};
		
		for (int i = 0; i < VALID_TEXT_SELECTIONS.length; i++) {
			String validEditorName = VALID_TEXT_SELECTIONS[i][0];
			String validSelection = VALID_TEXT_SELECTIONS[i][1];
			
			IEditorPart editor = WorkbenchUtils.openEditor(PARENT_TO_CHILD_RESTRICTIONS_RESOLVE_SELECTOR_PAGE_NAME);
			if (editor != null) openedEditors.add(editor);
			try {
				assertTrue(editor instanceof JSPMultiPageEditor);
				JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
				ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
					
				IDocument document = viewer.getDocument();
				IRegion reg = new FindReplaceDocumentAdapter(document).find(startFrom,
						TEXT_TO_SEARCH, true, true, false, false);
				assertNotNull("Attribute :" + TEXT_TO_SEARCH + " not found whyle search starting from " + startFrom, reg);

				startFrom = reg.getOffset() + reg.getLength();
				
				IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, new Region(startFrom, 0), true); // new Region(reg.getOffset() + reg.getLength(), 0)
				
				assertTrue("Hyperlinks not found for position " + startFrom,(links != null && links.length > 0));
				
				boolean found = false;
				for(IHyperlink link : links){
					assertNotNull(link.toString());
					
					link.open();
					
					IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if (resultEditor != null) openedEditors.add(resultEditor);
					if(validEditorName.equals(resultEditor.getTitle())){
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
							String selectionText = stEditor.getTextViewer().getDocument().get(textSelection.getOffset(), textSelection.getLength());
							assertTrue("Required CSS Rule is not selected", 
									(validSelection.equalsIgnoreCase(selectionText)));
							found = true;
							break;
						}
					}
				}
				assertTrue("OpenOn have not opened a valid selection [" + validSelection + "] in " + validEditorName + " editor",found);
			} finally {
				closeEditors(openedEditors);
			}
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
