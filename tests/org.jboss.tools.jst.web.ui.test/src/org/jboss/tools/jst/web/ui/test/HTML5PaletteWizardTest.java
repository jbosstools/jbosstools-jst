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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewAudioWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewAudioWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewImageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewImageWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLabelWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLabelWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewVideoWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewVideoWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTML5PaletteWizardTest extends AbstractPaletteEntryTest implements JQueryConstants, HTMLConstants {
	IEditorPart editor = null;
	
	public HTML5PaletteWizardTest() {}

	public void setUp() {
		super.setUp();
		editor = openEditor(getFileName());
	}

	protected String getFileName() {
		return "a14.html";
	}

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

	public IWizardPage runToolEntry(String entry, boolean wizardExpected) {
		IWizardPage result = runToolEntry("HTML", entry, wizardExpected);
		if(wizardExpected) {
			assertTrue(result instanceof NewHTMLWidgetWizardPage);
			NewHTMLWidgetWizardPage page = (NewHTMLWidgetWizardPage)result;
			assertEquals(getVersion(), page.getWizard().getVersion());
		}
		return result;
	}

	protected HTMLVersion getVersion() {
		return HTMLVersion.HTML_5_0;
	}

	public void testNewFormWizard() {
		IWizardPage currentPage = runToolEntry("Form", true);

		assertTrue(currentPage instanceof NewFormWizardPage);

		NewFormWizardPage wizardPage = (NewFormWizardPage)currentPage;
		NewFormWizard wizard = (NewFormWizard)wizardPage.getWizard();

		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(EDITOR_ID_NAME, "myForm");
		assertAttrExists(wizard, ATTR_NAME, "myForm");

		assertTextDoesNotExist(wizard, ATTR_METHOD);
		wizardPage.setEditorValue(EDITOR_ID_FORM_METHOD, METHOD_POST);
		assertAttrExists(wizard, ATTR_METHOD, METHOD_POST);
		wizardPage.setEditorValue(EDITOR_ID_FORM_METHOD, METHOD_GET);
		assertTextDoesNotExist(wizard, ATTR_METHOD);

		assertTextDoesNotExist(wizard, ATTR_NOVALIDATE);
		wizardPage.setEditorValue(EDITOR_ID_VALIDATE, FALSE);
		assertAttrExists(wizard, ATTR_NOVALIDATE, ATTR_NOVALIDATE);
		wizardPage.setEditorValue(EDITOR_ID_VALIDATE, TRUE);
		assertTextDoesNotExist(wizard, ATTR_NOVALIDATE);

		assertTextDoesNotExist(wizard, ATTR_AUTOCOMPLETE);
		wizardPage.setEditorValue(EDITOR_ID_AUTOCOMPLETE, FALSE);
		assertAttrExists(wizard, ATTR_AUTOCOMPLETE, AUTOCOMPLETE_OFF);
		wizardPage.setEditorValue(EDITOR_ID_AUTOCOMPLETE, TRUE);
		assertTextDoesNotExist(wizard, ATTR_AUTOCOMPLETE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewImageWizard() {
		IWizardPage currentPage = runToolEntry("Image", true);

		assertTrue(currentPage instanceof NewImageWizardPage);

		NewImageWizardPage wizardPage = (NewImageWizardPage)currentPage;
		NewImageWizard wizard = (NewImageWizard)wizardPage.getWizard();

		assertAttrExists(wizard, ATTR_ALT, "");
		wizardPage.setEditorValue(EDITOR_ID_ALT, "myalt");
		assertAttrExists(wizard, ATTR_ALT, "myalt");

		assertAttrExists(wizard, ATTR_SRC, "");
		wizardPage.setEditorValue(EDITOR_ID_SRC, "mysrc.gif");
		assertAttrExists(wizard, ATTR_SRC, "mysrc.gif");

		assertTextDoesNotExist(wizard, ATTR_WIDTH);
		wizardPage.setEditorValue(EDITOR_ID_WIDTH, "20");
		assertAttrExists(wizard, ATTR_WIDTH, "20");
		wizardPage.setEditorValue(EDITOR_ID_WIDTH, "");
		assertTextDoesNotExist(wizard, ATTR_WIDTH);

		assertTextDoesNotExist(wizard, ATTR_HEIGHT);
		wizardPage.setEditorValue(EDITOR_ID_HEIGHT, "30");
		assertAttrExists(wizard, ATTR_HEIGHT, "30");
		wizardPage.setEditorValue(EDITOR_ID_HEIGHT, "");
		assertTextDoesNotExist(wizard, ATTR_HEIGHT);

		assertTextDoesNotExist(wizard, ATTR_CROSSORIGIN);
		wizardPage.setEditorValue(EDITOR_ID_CROSSORIGIN, CROSSORIGIN_ANONIMOUS);
		assertAttrExists(wizard, ATTR_CROSSORIGIN, CROSSORIGIN_ANONIMOUS);
		wizardPage.setEditorValue(EDITOR_ID_CROSSORIGIN, "");
		assertTextDoesNotExist(wizard, ATTR_CROSSORIGIN);

		assertTextDoesNotExist(wizard, ATTR_ISMAP);
		wizardPage.setEditorValue(EDITOR_ID_ISMAP, TRUE);
		assertAttrExists(wizard, ATTR_ISMAP, ATTR_ISMAP);
		wizardPage.setEditorValue(EDITOR_ID_ISMAP, FALSE);
		assertTextDoesNotExist(wizard, ATTR_ISMAP);

		assertTextDoesNotExist(wizard, ATTR_USEMAP);
		wizardPage.setEditorValue(EDITOR_ID_USEMAP, "#map");
		assertAttrExists(wizard, ATTR_USEMAP, "#map");
		wizardPage.setEditorValue(EDITOR_ID_USEMAP, "");
		assertTextDoesNotExist(wizard, ATTR_USEMAP);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewVideoWizard() {
		IWizardPage currentPage = runToolEntry("Video", true);

		assertTrue(currentPage instanceof NewVideoWizardPage);

		NewVideoWizardPage wizardPage = (NewVideoWizardPage)currentPage;
		NewVideoWizard wizard = (NewVideoWizard)wizardPage.getWizard();

		assertEquals("1", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_AUTOPLAY);
		wizardPage.setEditorValue(EDITOR_ID_AUTOPLAY, TRUE);
		assertAttrExists(wizard, ATTR_AUTOPLAY, ATTR_AUTOPLAY);
		wizardPage.setEditorValue(EDITOR_ID_AUTOPLAY, FALSE);
		assertTextDoesNotExist(wizard, ATTR_AUTOPLAY);

		assertTextDoesNotExist(wizard, ATTR_LOOP);
		wizardPage.setEditorValue(EDITOR_ID_LOOP, TRUE);
		assertAttrExists(wizard, ATTR_LOOP, ATTR_LOOP);
		wizardPage.setEditorValue(EDITOR_ID_LOOP, FALSE);
		assertTextDoesNotExist(wizard, ATTR_LOOP);

		assertTextDoesNotExist(wizard, ATTR_MUTED);
		wizardPage.setEditorValue(EDITOR_ID_MUTED, TRUE);
		assertAttrExists(wizard, ATTR_MUTED, ATTR_MUTED);
		wizardPage.setEditorValue(EDITOR_ID_MUTED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_MUTED);

		assertAttrExists(wizard, ATTR_CONTROLS, ATTR_CONTROLS);
		wizardPage.setEditorValue(EDITOR_ID_CONTROLS, FALSE);
		assertTextDoesNotExist(wizard, ATTR_CONTROLS);
		wizardPage.setEditorValue(EDITOR_ID_CONTROLS, TRUE);
		assertAttrExists(wizard, ATTR_CONTROLS, ATTR_CONTROLS);

		assertTextExists(wizard, TAG_SOURCE);
		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "2");
		assertEquals("2", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewAudioWizard() {
		IWizardPage currentPage = runToolEntry("Audio", true);

		assertTrue(currentPage instanceof NewAudioWizardPage);

		NewAudioWizardPage wizardPage = (NewAudioWizardPage)currentPage;
		NewAudioWizard wizard = (NewAudioWizard)wizardPage.getWizard();

		assertEquals("1", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_AUTOPLAY);
		wizardPage.setEditorValue(EDITOR_ID_AUTOPLAY, TRUE);
		assertAttrExists(wizard, ATTR_AUTOPLAY, ATTR_AUTOPLAY);
		wizardPage.setEditorValue(EDITOR_ID_AUTOPLAY, FALSE);
		assertTextDoesNotExist(wizard, ATTR_AUTOPLAY);

		assertTextDoesNotExist(wizard, ATTR_LOOP);
		wizardPage.setEditorValue(EDITOR_ID_LOOP, TRUE);
		assertAttrExists(wizard, ATTR_LOOP, ATTR_LOOP);
		wizardPage.setEditorValue(EDITOR_ID_LOOP, FALSE);
		assertTextDoesNotExist(wizard, ATTR_LOOP);

		assertTextDoesNotExist(wizard, ATTR_MUTED);
		wizardPage.setEditorValue(EDITOR_ID_MUTED, TRUE);
		assertAttrExists(wizard, ATTR_MUTED, ATTR_MUTED);
		wizardPage.setEditorValue(EDITOR_ID_MUTED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_MUTED);

		assertAttrExists(wizard, ATTR_CONTROLS, ATTR_CONTROLS);
		wizardPage.setEditorValue(EDITOR_ID_CONTROLS, FALSE);
		assertTextDoesNotExist(wizard, ATTR_CONTROLS);
		wizardPage.setEditorValue(EDITOR_ID_CONTROLS, TRUE);
		assertAttrExists(wizard, ATTR_CONTROLS, ATTR_CONTROLS);

		assertTextExists(wizard, TAG_SOURCE);
		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "2");
		assertEquals("2", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewLabelWizard() {
		IWizardPage currentPage = runToolEntry("Label", true);

		assertTrue(currentPage instanceof NewLabelWizardPage);

		NewLabelWizardPage wizardPage = (NewLabelWizardPage)currentPage;
		NewLabelWizard wizard = (NewLabelWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(EDITOR_ID_LABEL, "Address:");
		assertTextExists(wizard, ">Address:<");

		wizardPage.setEditorValue(EDITOR_ID_FOR, "inputID");
		assertAttrExists(wizard, ATTR_FOR, "inputID");

		wizardPage.setEditorValue(EDITOR_ID_FORM, "formID");
		assertAttrExists(wizard, ATTR_FORM, "formID");
		
		compareGeneratedAndInsertedText(wizard);
	}

	protected void compareGeneratedAndInsertedText(NewHTMLWidgetWizard<?> wizard) {
		String generatedText = wizard.getTextForTextView();

		wizard.performFinish();

		String insertedText = getInsertedText();
		assertTrue(isSameHTML(generatedText, insertedText));
	}

	void assertAttrExists(AbstractNewHTMLWidgetWizard wizard, String attr, String value) {
		assertTextExists(wizard, attr + "=\"" + value + "\"");
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) >= 0);
	}

	void assertTextDoesNotExist(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) < 0);
	}

	void assertTextIsInserted(String text) {
		String content = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(content.indexOf(text) > 0);
	}

	void assertAttrIsInserted(String attr, String value) {
		assertTextIsInserted(attr + "=\"" + value + "\"");
	}

	public void testPaletteItemsWithoutUI() {
		XModelObject g = PreferenceModelUtilities.getPreferenceModel().getByPath(PaletteModel.MOBILE_PATH);
		List<String> failures = new ArrayList<String>(); 
		XModelObject[] cs = g.getChildren();
		for (XModelObject c: cs) {
			String name = c.getAttributeValue(XModelObjectConstants.ATTR_NAME);
			if(name.indexOf('.') >= 0) name = name.substring(name.indexOf('.') + 1);
			runPaletteItemsWithoutUI(failures, textEditor, c, name);
		}
		if(!failures.isEmpty()) {
			StringBuilder text = new StringBuilder();
			text.append("The following Palette wizards failed:\n");
			for (String s: failures) {
				text.append(s).append("\n");
			}
			fail(text.toString());
		}
	}

	private void runPaletteItemsWithoutUI(List<String> failures, JSPTextEditor textEditor, XModelObject group, String category) {
		XModelObject[] cs = group.getChildren();
		if(cs.length == 0) {
			try {
				long t = System.currentTimeMillis();
				String path = group.getPath();
				int i = path.indexOf(PaletteModel.VERSION_PREFIX);
				if(i < 0) return;
				String v = path.substring(i + 8);
				int j = v.indexOf("/");
				v = v.substring(0, j);
				String name = group.getAttributeValue(XModelObjectConstants.ATTR_NAME);
				if(name.indexOf('.') >= 0) name = name.substring(name.indexOf('.') + 1);
				PaletteItemResult r = AbstractNewHTMLWidgetWizard.runWithoutUi(textEditor, category, v, name);
				long dt = System.currentTimeMillis() - t;
				System.out.println("success " + group.getPath() + " in " + dt);
			} catch (Exception e) {
				failures.add(group.getPath());
			}
		} else {
			for (XModelObject c: cs) {
				runPaletteItemsWithoutUI(failures, textEditor, c, category);
			}
		}
	}

}
