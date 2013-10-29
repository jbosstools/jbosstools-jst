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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.FormPropertySheetPage;
import org.jboss.tools.jst.web.ui.internal.properties.IPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer.Entry;
import org.jboss.tools.jst.web.ui.internal.properties.html.HTMLPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.jquery.JQueryPropertySetViewer;
import org.jboss.tools.test.util.WorkbenchUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

public class FormPropertySheetPageTest extends TestCase implements JQueryHTMLConstants {
	protected IProject project = null;
	protected IEditorPart editor = null;
	protected StructuredTextEditor structuredEditor = null;
	FormPropertySheetPage page;

	public FormPropertySheetPageTest() {}

	public void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("SimpleProject");
	}

	protected void tearDown() throws Exception {
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	protected IEditorPart openEditor(String fileName) {
		IFile testfile = project.getFile(fileName);
		assertTrue("Test file doesn't exist: " + project.getName() + "/" + fileName, 
				(testfile.exists() && testfile.isAccessible()));

		editor = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName); //$NON-NLS-1$
		assertNotNull(editor);
		structuredEditor = ((JSPMultiPageEditor)editor).getSourceEditor();

		Object o = editor.getAdapter(IPropertySheetPage.class);
		assertTrue(o instanceof FormPropertySheetPage);

		page = (FormPropertySheetPage)o;
		
		Control c = structuredEditor.getTextViewer().getControl();
		assertTrue(c instanceof Composite);
		page.createControl((Composite)c);

		return editor;
	}

	public void testA() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("a-1");
		
		List<String> cs = page.getViewer().getCategories();
		assertEquals(3, cs.size());

		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "a-1"},
			{ATTR_HREF, "abc"}
		});
	}

	public void testArea() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("area-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "area-1"},
			{ATTR_HREF, "areaurl"},
			{ATTR_ALT, "myalt"},
			{ATTR_REL, "author"},
			{ATTR_SHAPE, "rect"},
			{ATTR_COORDS, "1,2,3,4"}
		});
	}

	public void testAudio() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("audio-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "audio-1"},
			{ATTR_SRC, "audiourl"},
			{ATTR_PRELOAD, "metadata"},
			{ATTR_CONTROLS, "true", ATTR_CONTROLS},
			{ATTR_LOOP, "true", ATTR_LOOP},
			{ATTR_AUTOPLAY, "false", null}
		});
	}

	public void testBase() throws Exception {
		openEditor("properties/a2.html");
		selectElementByName("base");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_HREF, "baseurl"}
		});
	}

	public void testBdo() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("bdo-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "bdo-1"},
			{ATTR_DIR, "rtl"}
		});
	}

	public void testBlockquote() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("blockquote-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "blockquote-1"},
			{ATTR_CITE, "mycite"}
		});
	}

	public void testButton() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("button-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "button-1"},
			{ATTR_TYPE, "reset"},
			{ATTR_NAME, "mybutton"},
			{ATTR_VALUE, ""},
			{ATTR_FORM, ""},
			{ATTR_DISABLED, FALSE, null},
			{ATTR_AUTOFOCUS, TRUE, ATTR_AUTOFOCUS}
			
		});
	}

	public void testCanvas() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("canvas-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "canvas-1"},
			{ATTR_HEIGHT, "10"},
			{ATTR_WIDTH, "20"},
		});
	}

	public void testCommand() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("command-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "command-1"},
			{ATTR_LABEL, "mycommand"},
			{ATTR_ICON, "myicon"},
			{CHECKED, TRUE, CHECKED},
			{ATTR_DISABLED, TRUE, ATTR_DISABLED},
			{ATTR_TYPE, "checkbox"},
			{ATTR_RADIOGROUP, "g"}
		});
	}

	public void testDel() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("del-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "del-1"},
			{ATTR_CITE, "mydelcite"},
			{ATTR_DATETIME, "1234"},
		});
	}

	public void testDetails() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("details-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "details-1"},
			{ATTR_OPEN, TRUE, ATTR_OPEN}
		});
	}

	public void testEmbed() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("embed-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "embed-1"},
			{ATTR_SRC, "embedurl"},
			{ATTR_HEIGHT, "1"},
			{ATTR_WIDTH, "2"},
			{ATTR_TYPE, "type1"}
		});
	}

	public void testFieldset() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("fieldset-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "fieldset-1"},
			{ATTR_NAME, "myfieldset"},
			{ATTR_FORM, "f"},
			{ATTR_DISABLED, TRUE, ATTR_DISABLED}
		});
	}

	public void testForm() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("form-1");
		
		HTMLPropertySetViewer viewer = findHTMLSetViewer();

		assertEditors(viewer, new String[][]{
			{ATTR_ID, "form-1"},
			{ATTR_NAME, "myform"},
			{ATTR_ACTION, "myaction"},
			{ATTR_METHOD, "get", "get"},
			{ATTR_NOVALIDATE, "false", null},
			{ATTR_AUTOCOMPLETE, "false", "off"}
		});
	}

	public void testPage() throws Exception {
		openEditor("properties/a1.html");
		selectElementByID("page-1");

		List<String> cs = page.getViewer().getCategories();
		assertEquals(3, cs.size());
		
		JQueryPropertySetViewer jq = findJQuerySetViewer();
		Entry fe = jq.getEditor(ATTR_DATA_ROLE);
		assertNotNull(fe);
		assertTrue(fe.isLayout());
		assertEquals(fe.getEditor().getValueAsString(), ROLE_PAGE);
	}

	private JQueryPropertySetViewer findJQuerySetViewer() {
		IPropertySetViewer psv = page.getViewer().getViewer("jQuery");
		assertTrue(psv instanceof JQueryPropertySetViewer);
		return (JQueryPropertySetViewer)psv;
	}

	private HTMLPropertySetViewer findHTMLSetViewer() {
		IPropertySetViewer psv = page.getViewer().getViewer("HTML");
		assertTrue(psv instanceof HTMLPropertySetViewer);
		return (HTMLPropertySetViewer)psv;
	}

	private void selectElementByID(String id) {
		Element e = getElementById(id);
		StructuredSelection sel = new StructuredSelection(e);
		page.selectionChanged(editor, sel);
	}

	private Element getElementById(String id) {
		StructuredModelWrapper w = new StructuredModelWrapper();
		IDocument document = structuredEditor.getDocumentProvider().getDocument(structuredEditor.getEditorInput());
		w.init(document);
		try {
			Document xml = w.getDocument();
			Element e = xml.getElementById(id);
			assertNotNull(e);
			return e;
		} finally {
			w.dispose();
		}
	}

	private void selectElementByName(String name) {
		Element e = getElementByName(name);
		StructuredSelection sel = new StructuredSelection(e);
		page.selectionChanged(editor, sel);
	}

	private Element getElementByName(String name) {
		StructuredModelWrapper w = new StructuredModelWrapper();
		IDocument document = structuredEditor.getDocumentProvider().getDocument(structuredEditor.getEditorInput());
		w.init(document);
		try {
			Document xml = w.getDocument();
			NodeList e = xml.getElementsByTagName(name);
			assertTrue(e.getLength() > 0);
			return (Element)e.item(0);
		} finally {
			w.dispose();
		}
	}

	private void assertEditors(AbstractAdvancedPropertySetViewer viewer, 
			String[][] pairs) {
		for (int i = 0; i < pairs.length; i++) {
			String name = pairs[i][0];
			String expected = pairs[i][1];
			Entry entry = viewer.getEditor(name);
			assertNotNull("Editor " + name + " is not found.", entry);
			assertEquals("Unexpected value of editor " + name, expected, entry.getEditor().getValueAsString());
			if(pairs[i].length > 2) {
				Object expectedModelValue = entry.getModelValue();
				assertEquals("Unexpected model value of editor " + name, pairs[i][2], expectedModelValue);
			}
		}
	}
}
