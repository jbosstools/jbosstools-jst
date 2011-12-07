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
import java.util.Set;

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
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkDetector;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.tld.ITaglibMapping;
import org.jboss.tools.jst.web.tld.IWebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;
import org.jboss.tools.jst.web.tld.WebProjectFactory;
import org.jboss.tools.jst.web.ui.editors.TLDCompoundEditor;
import org.jboss.tools.test.util.WorkbenchUtils;

public class TaglibOpenOnTest extends TestCase {
	private static final String PROJECT_NAME = "stopka-ui-test";
	private static final String PAGE_NAME =  PROJECT_NAME+"/src/main/webapp/test.jspx";
	
	
	public IProject project = null;

	protected void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		
		XModelObject o = EclipseResourceUtil.createObjectForResource(project);
		XModel model = o.getModel();
		IWebProject wp = WebProjectFactory.instance.getWebProject(model);
		TaglibMapping m = (TaglibMapping)wp.getTaglibMapping();
		m.revalidate(WebAppHelper.getWebApp(model));		
	}
	
	protected void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}

	public TaglibOpenOnTest() {
		super("Taglib OpenOn test");
	}

	/**
	 * This method tests the case described in JBIDE-10302:
	 * 
	 * EXECUTE: Open file stopka-ui-test\src\main\webapp\test.jspx
	 * EXECUTE: Invoke open-on at <utils:common-head-data/>
	 *          where prefix is mapped to tag library uri "http://stopka.us/ui/utils"
	 * ASSERT:  Resource 'utils.tld' is opened in TLD editor.
	 * EXECUTE: In opened TLD editor, invoke open-on at '/META-INF/tags/utils/common-head-data.tagx' 
	 *          located at xpath=taglib/tag-file/path/#text
	 * ASSERT:  Resource 'common-head-data.tagx' is opened in VPE.
	 * 
	 * @throws PartInitException
	 * @throws BadLocationException
	 */
	public void testTaglibOpenOn() throws PartInitException, BadLocationException {
		String editorName = "utils.tld";
		HashSet<IEditorPart> openedEditors = new HashSet<IEditorPart>();

		IEditorPart editor = WorkbenchUtils.openEditor(PAGE_NAME);
		if (editor != null) openedEditors.add(editor);
		assertTrue(editor instanceof JSPMultiPageEditor);
		try {
			JSPMultiPageEditor jspMultyPageEditor = (JSPMultiPageEditor) editor;
			ISourceViewer viewer = jspMultyPageEditor.getSourceEditor().getTextViewer(); 
				
			IHyperlink[] links = findLinks(viewer, "utils:common-head-data", "utils:common-head-data");
			IEditorPart resultEditor = findEditor(links, editorName, openedEditors);
			assertNotNull("OpenOn have not opened "+editorName+" editor", resultEditor);
			
			//Second openon.
			TLDCompoundEditor tldEditor = (TLDCompoundEditor)((EditorPartWrapper)resultEditor).getEditor();
			tldEditor.selectPageByName("Source");
			viewer = tldEditor.getSourceEditor().getTextViewer();
			links = findLinks(viewer, "path", "/META-INF/tags/utils/common-head-data.tagx");
			editorName = "common-head-data.tagx";
			resultEditor = findEditor(links, editorName, openedEditors);
			assertNotNull("OpenOn have not opened "+editorName+" editor", resultEditor);
		} finally {
			closeEditors(openedEditors);
		}
	}

	private IEditorPart findEditor(IHyperlink[] links, String editorName, Set<IEditorPart> openedEditors) {
		for(IHyperlink link : links){
			assertNotNull(link.toString());
			
			link.open();
			
			IEditorPart resultEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (resultEditor != null) openedEditors.add(resultEditor);
			if(editorName.equals(resultEditor.getTitle())){
				return resultEditor;
			}
		}
		return null;
	}

	private IHyperlink[] findLinks(ISourceViewer viewer, String tagName, String valueToFind) throws BadLocationException {
		IDocument document = viewer.getDocument();
		IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
				tagName, true, true, false, false);
		assertNotNull("Tag:"+tagName+" not found",reg);
		
		reg = new FindReplaceDocumentAdapter(document).find(reg.getOffset(),
				valueToFind, true, true, false, false);
		assertNotNull("Value to find:"+valueToFind+" not found",reg);
		
		IHyperlink[] links = HyperlinkDetector.getInstance().detectHyperlinks(viewer, reg, true); // new Region(reg.getOffset() + reg.getLength(), 0)
		
		assertTrue("Hyperlinks for value '"+valueToFind+"' are not found",(links != null && links.length > 0));

		return links;
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
