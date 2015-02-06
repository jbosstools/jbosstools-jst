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
package org.jboss.tools.jst.jsp.test.palette;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPage;
import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PalettePageImpl;
import org.jboss.tools.jst.web.ui.palette.PaletteAdapter;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteRootImpl;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractPaletteEntryTest extends TestCase {
	protected IProject project = null;
	protected JSPTextEditor textEditor;

	public AbstractPaletteEntryTest() {
	}

	public void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("SimpleProject");
	}

	public IEditorPart openEditor(String fileName) {
		return openEditor(project, fileName);
	}
	
	public IEditorPart openEditor(IProject project, String fileName) {
		IFile testfile = project.getFile(fileName);
		assertTrue("Test file doesn't exist: " + project.getName() + "/" + fileName, 
				(testfile.exists() && testfile.isAccessible()));

		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName); //$NON-NLS-1$
		assertTrue(editorPart instanceof JSPMultiPageEditor);
		JSPMultiPageEditor editor = (JSPMultiPageEditor)editorPart;
		textEditor = editor.getJspEditor();
		String text = textEditor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		int offset = text.indexOf(getInsertAfterText(), 0) + getInsertAfterText().length() + 1;
		textEditor.getSelectionProvider().setSelection(new TextSelection(offset, 0));
		return editorPart;
	}

	protected String getInsertAfterText() {
		return "<body>";
	}

	protected String getInsertBeforeText() {
		return "</body>";
	}

	protected WizardDialog currentDialog = null;

	public IWizardPage runToolEntry(String category, String entry, boolean wizardExpected) {
		PaletteViewer viewer = getPaletteViewer();
		ToolEntry toolEntry = findEntry(viewer, category, entry);
		assertNotNull(toolEntry);
		System.setProperty(IDropCommand.TEST_FLAG, "true");
		viewer.setActiveTool(toolEntry);
		System.getProperties().remove(IDropCommand.TEST_FLAG);
		Shell shell = findShell();
		assertEquals(wizardExpected, shell != null);
		if(wizardExpected) {
			Object data = shell.getData();
			assertTrue(data instanceof WizardDialog);
			currentDialog = (WizardDialog)data;
			return ((WizardDialog)data).getCurrentPage();
		} else {
			return null;
		}
		
	}

	public void switchVersion(IHTMLLibraryVersion version) {
		PaletteViewer viewer = getPaletteViewer();
		PaletteRootImpl root = (PaletteRootImpl)viewer.getPaletteRoot();
		root.getPaletteModel().getPaletteContents().setPreferredVersion(JQueryConstants.JQM_CATEGORY, version);
		((PaletteModelImpl)root.getPaletteModel()).reloadCategory();
	}

	public Shell findShell() {
		for (Shell sh : WebUiPlugin.getDefault().getWorkbench().getDisplay().getShells()) {
			if (getWizardWindowTitle()
					.equalsIgnoreCase(sh.getText())) {
				return sh;
			}
		}
		return null;
	}

	/**
	 * Override or modify if wizard window title will be different. 
	 * @return
	 */
	protected String getWizardWindowTitle() {
		return DropWizardMessages.Wizard_Window_Title;
	}

	public PaletteViewer getPaletteViewer() {
		PaletteAdapter adapter = getPaletteAdapter();
		return adapter.getViewer();
	}

	public PaletteAdapter getPaletteAdapter() {
		IWorkbenchPage page = WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		IViewPart view = page.findView("org.eclipse.gef.ui.palette_view");

		if(view == null) {
			try {
				view = page.showView("org.eclipse.gef.ui.palette_view");
			} catch(PartInitException e) {
				 fail("cannot open palette: " + e.getMessage());
			}
		}
		assertTrue(view instanceof PaletteView);
		PaletteView palette = (PaletteView)view;
		IPage viewPage = palette.getCurrentPage();
		assertTrue(viewPage instanceof PalettePageImpl);
		PalettePageImpl palettePage = (PalettePageImpl)viewPage;
		return (PaletteAdapter)palettePage.getAdapter();
	}

	public ToolEntry findEntry(PaletteViewer viewer, String category, String entry) {
		PaletteRoot root = viewer.getPaletteRoot();
		List<?> l = root.getChildren();
		for (Object o: l) {
			PaletteDrawer d = (PaletteDrawer)o;
			if(category.equals(d.getLabel())) {
				List<?> l2 = d.getChildren();
				for (Object o2: l2) {
					if(o2 instanceof ToolEntry) {
						ToolEntry t = (ToolEntry)o2;
						if(entry.equals(t.getLabel())) {
							return t;
						}
					}
				}
			}
		}
		return null;
	}

	protected String getInsertedText() {
		String content = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		int b = content.indexOf(getInsertAfterText()) + getInsertAfterText().length();
		int e = content.indexOf(getInsertBeforeText());
		return content.substring(b, e);
	}

	protected boolean isSameHTML(String s1, String s2) {
		return removeWhiteSpaces(s1).equals(removeWhiteSpaces(s2));
	}

	protected String removeWhiteSpaces(String s) {
		boolean insideTag = false;
		boolean quota = false;
		int whitespaces = 0;
		boolean beginning = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(!quota && Character.isWhitespace(c)) {
				if(!beginning) {
					whitespaces++;
				}
				continue;
			}
			if(!quota && c == '=' && insideTag) {
				whitespaces = 0;
				beginning = true;
			}
			if(!quota && c == '<') {
				insideTag = true;
				whitespaces = 0;
			}
			if(!quota && c == '>') {
				insideTag = false;
				beginning = true;
			} else if(!Character.isWhitespace(c)) {
				beginning = false;
			}
			if(insideTag) {
				if(c == '"') quota = !quota;
				if(quota) whitespaces = 0;
			}
			if(!quota && !Character.isWhitespace(c) && whitespaces > 0) {
				sb.append(' ');
				whitespaces = 0;
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
