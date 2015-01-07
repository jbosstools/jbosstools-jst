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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.PaletteItemDropCommand;
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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewCanvasWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewCanvasWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewDatalistWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewDatalistWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewMenuitemWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewMenuitemWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTableWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTableWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;

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

	public void testNewCanvasWizard() {
		IWizardPage currentPage = runToolEntry("Canvas", true);

		assertTrue(currentPage instanceof NewCanvasWizardPage);

		NewCanvasWizardPage wizardPage = (NewCanvasWizardPage)currentPage;
		NewCanvasWizard wizard = (NewCanvasWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(EDITOR_ID_ID, "myCanvas");
		assertAttrExists(wizard, EDITOR_ID_ID, "myCanvas");

		assertTextDoesNotExist(wizard, ATTR_WIDTH);
		wizardPage.setEditorValue(ATTR_WIDTH, "149");
		assertAttrExists(wizard, ATTR_WIDTH, "149");
		
		assertTextDoesNotExist(wizard, ATTR_HEIGHT);
		wizardPage.setEditorValue(ATTR_HEIGHT, "151");
		assertAttrExists(wizard, ATTR_HEIGHT, "151");

		assertTextDoesNotExist(wizard, TAG_SCRIPT);
		wizardPage.setEditorValue(EDITOR_ID_ADD_SCRIPT_TEMPLATE, TRUE);
		assertTextExists(wizard, TAG_SCRIPT);
		wizardPage.setEditorValue(EDITOR_ID_ADD_SCRIPT_TEMPLATE, FALSE);
		assertTextDoesNotExist(wizard, TAG_SCRIPT);
		
		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewDatalistWizard() {
		IWizardPage currentPage = runToolEntry("Datalist", true);

		assertTrue(currentPage instanceof NewDatalistWizardPage);

		NewDatalistWizardPage wizardPage = (NewDatalistWizardPage)currentPage;
		NewDatalistWizard wizard = (NewDatalistWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(EDITOR_ID_ID, "myDatalist");
		assertAttrExists(wizard, EDITOR_ID_ID, "myDatalist");

		assertTextDoesNotExist(wizard, TAG_INPUT);
		wizardPage.setEditorValue(TAG_INPUT, TRUE);
		assertTextExists(wizard, TAG_INPUT);
		assertAttrExists(wizard, ATTR_LIST, "myDatalist");
		wizardPage.setEditorValue(EDITOR_ID_ID, "");
		assertAttrExists(wizard, ATTR_LIST, "datalist-1");
		
		wizardPage.setTextInputProperty(ATTR_NAME, "name1");
		wizardPage.editTextInput(false);
		assertAttrExists(wizard, ATTR_NAME, "name1");
		wizardPage.setTextInputProperty(JQueryConstants.EDITOR_ID_TEXT_TYPE, "search");
		wizardPage.editTextInput(false);
		assertAttrExists(wizard, ATTR_TYPE, "search");
		
		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewInputWizard() {
		IWizardPage currentPage = runToolEntry("Text Input", true);

		assertTrue(currentPage instanceof NewTextInputWizardPage);

		NewTextInputWizardPage wizardPage = (NewTextInputWizardPage)currentPage;
		NewTextInputWizard wizard = (NewTextInputWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(EDITOR_ID_ID, "myInput");
		assertAttrExists(wizard, EDITOR_ID_ID, "myInput");


		assertTextDoesNotExist(wizard, TAG_DATALIST);
		wizardPage.createDatalist(false);
		assertTextExists(wizard, TAG_DATALIST);
		wizardPage.setEditorValue(ATTR_LIST, "11");
		assertTextDoesNotExist(wizard, TAG_DATALIST);
		wizardPage.createDatalist(false);
		assertTextExists(wizard, TAG_DATALIST);
		wizardPage.setEditorValue(ATTR_LIST, "");
		assertTextDoesNotExist(wizard, TAG_DATALIST);
		
		wizardPage.setEditorValue(EDITOR_ID_PLACEHOLDER, "abcdef");
		assertAttrExists(wizard, ATTR_PLACEHOLDER, "abcdef");

		assertTextDoesNotExist(wizard, ATTR_AUTOFOCUS);
		wizardPage.setEditorValue(EDITOR_ID_AUTOFOCUS, TRUE);
		assertAttrExists(wizard, ATTR_AUTOFOCUS, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_AUTOFOCUS, FALSE);
		assertTextDoesNotExist(wizard, ATTR_AUTOFOCUS);

		assertTextDoesNotExist(wizard, ATTR_REQUIRED);
		wizardPage.setEditorValue(EDITOR_ID_REQUIRED, TRUE);
		assertAttrExists(wizard, ATTR_REQUIRED, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_REQUIRED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_REQUIRED);

		assertTextDoesNotExist(wizard, ATTR_PATTERN);
		wizardPage.setEditorValue(EDITOR_ID_PATTERN, "(");
		assertAttrExists(wizard, ATTR_PATTERN, "(");
		assertNotNull(wizardPage.getMessage());
		assertTrue(wizardPage.getMessage().toLowerCase().indexOf("unclosed") >= 0);
		wizardPage.setEditorValue(EDITOR_ID_PATTERN, ".*");
		assertAttrExists(wizard, ATTR_PATTERN, ".*");
//		assertEquals(WizardMessages.noPlaceForWidgetWarning, wizardPage.getMessage());
		assertNull(wizardPage.getMessage());
		wizardPage.setEditorValue(EDITOR_ID_PATTERN, "");
		assertTextDoesNotExist(wizard, ATTR_PATTERN);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("Form Button", true);

		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard();

		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeSubmitLabel);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_SUBMIT);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeResetLabel);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_BUTTON);
		assertAttrExists(wizard, ATTR_VALUE, "Input");
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_BUTTON);

		wizardPage.setEditorValue(EDITOR_ID_ID, "myButton");
		assertAttrExists(wizard, EDITOR_ID_ID, "myButton");

		assertTextDoesNotExist(wizard, ATTR_FORM);
		wizardPage.setEditorValue(ATTR_FORM, "myForm");
		assertAttrExists(wizard, ATTR_FORM, "myForm");
		wizardPage.setEditorValue(ATTR_FORM, "");
		assertTextDoesNotExist(wizard, ATTR_FORM);

		assertTextDoesNotExist(wizard, ATTR_AUTOFOCUS);
		wizardPage.setEditorValue(EDITOR_ID_AUTOFOCUS, TRUE);
		assertAttrExists(wizard, ATTR_AUTOFOCUS, ATTR_AUTOFOCUS);
		wizardPage.setEditorValue(EDITOR_ID_AUTOFOCUS, FALSE);
		assertTextDoesNotExist(wizard, ATTR_AUTOFOCUS);

		assertTextDoesNotExist(wizard, ATTR_DISABLED);
		wizardPage.setEditorValue(ATTR_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_DISABLED, ATTR_DISABLED);
		wizardPage.setEditorValue(ATTR_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DISABLED);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTableWizard() {
		IWizardPage currentPage = runToolEntry("Table", true);

		assertTrue(currentPage instanceof NewTableWizardPage);

		NewTableWizardPage wizardPage = (NewTableWizardPage)currentPage;
		NewTableWizard wizard = (NewTableWizard)wizardPage.getWizard();
	
		assertTextDoesNotExist(wizard, TAG_CAPTION);
		wizardPage.setEditorValue(TAG_CAPTION, "Some Table");
		assertTextExists(wizard, TAG_CAPTION);
		assertTextExists(wizard, "Some Table");
		wizardPage.setEditorValue(TAG_CAPTION, "");
		assertTextDoesNotExist(wizard, TAG_CAPTION);

		wizardPage.setEditorValue(EDITOR_ID_ID, "myTable");
		assertAttrExists(wizard, EDITOR_ID_ID, "myTable");

		assertTextExists(wizard, TAG_THEAD);
		assertTextExists(wizard, TAG_TFOOT);
		wizardPage.setEditorValue(EDITOR_ID_TABLE_KIND, TABLE_KIND_SIMPLE);
		assertTextDoesNotExist(wizard, TAG_THEAD);
		assertTextDoesNotExist(wizard, TAG_TFOOT);
		wizardPage.setEditorValue(EDITOR_ID_TABLE_KIND, TABLE_KIND_ADVANCED);
		assertTextExists(wizard, TAG_THEAD);
		assertTextExists(wizard, TAG_TFOOT);

		wizardPage.setEditorValue(TAG_TFOOT, FALSE);
		assertTextDoesNotExist(wizard, TAG_TFOOT);
		wizardPage.setEditorValue(TAG_TFOOT, TRUE);
		assertTextExists(wizard, TAG_TFOOT);
		
		wizardPage.setEditorValue(TAG_THEAD, FALSE);
		assertTextDoesNotExist(wizard, TAG_THEAD);
		wizardPage.setEditorValue(TAG_THEAD, TRUE);
		assertTextExists(wizard, TAG_THEAD);
		
		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewMenuitemWizard() {
		IWizardPage currentPage = runToolEntry("Menuitem", true);

		assertTrue(currentPage instanceof NewMenuitemWizardPage);

		NewMenuitemWizardPage wizardPage = (NewMenuitemWizardPage)currentPage;
		NewMenuitemWizard wizard = (NewMenuitemWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(EDITOR_ID_ID, "myItem");
		assertAttrExists(wizard, EDITOR_ID_ID, "myItem");

		wizardPage.setEditorValue(ATTR_LABEL, "Item 01");
		assertAttrExists(wizard, ATTR_LABEL, "Item 01");

		assertTextDoesNotExist(wizard, ATTR_ICON);
		wizardPage.setEditorValue(ATTR_ICON, "icon.gif");
		assertAttrExists(wizard, ATTR_ICON, "icon.gif");
		wizardPage.setEditorValue(ATTR_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_ICON);

		assertTextDoesNotExist(wizard, ATTR_DEFAULT);
		wizardPage.setEditorValue(ATTR_DEFAULT, TRUE);
		assertAttrExists(wizard, ATTR_DEFAULT, ATTR_DEFAULT);
		wizardPage.setEditorValue(ATTR_DEFAULT, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DEFAULT);

		assertTextDoesNotExist(wizard, ATTR_DISABLED);
		wizardPage.setEditorValue(ATTR_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_DISABLED, ATTR_DISABLED);
		wizardPage.setEditorValue(ATTR_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DISABLED);

		wizardPage.setEditorValue(ATTR_TYPE, MENUITEM_TYPE_CHECKBOX);
		assertAttrExists(wizard, ATTR_TYPE, MENUITEM_TYPE_CHECKBOX);
		assertTextDoesNotExist(wizard, CHECKED);
		wizardPage.setEditorValue(CHECKED, TRUE);
		assertAttrExists(wizard, CHECKED, CHECKED);
		wizardPage.setEditorValue(ATTR_TYPE, MENUITEM_TYPE_COMMAND);
		assertTextDoesNotExist(wizard, CHECKED);

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
		PaletteModelImpl model = new PaletteModelImpl();
		model.load();
		IPaletteGroup jqmGroup = model.getPaletteGroup("jQuery Mobile");
		assertNotNull(jqmGroup);
		
		List<String> failures = new ArrayList<String>();
		for(IPaletteVersionGroup vGroup : jqmGroup.getPaletteVersionGroups()){
			for(IPaletteCategory category : vGroup.getCategories()){
				for(IPaletteItem item : category.getItems()){
					runPaletteItemsWithoutUI(failures, textEditor, item);
				}
			}
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

	public static void runPaletteItemsWithoutUI(List<String> failures, JSPTextEditor textEditor, IPaletteItem item) {
		try {
			String name = item.getName();
			if(name.equals("Field Container")) {
				//no wizard.
				return;
			}
			long t = System.currentTimeMillis();
			PaletteItemResult r = ((AbstractNewHTMLWidgetWizard)item.createWizard()).runWithoutUi(textEditor);
			long dt = System.currentTimeMillis() - t;
			System.out.println("success " + item.getName() + " in " + dt);
		} catch (Exception e) {
			failures.add(item.getName());
		}
	}

	public void testPositionCorrectors() throws Exception {
		PaletteModelImpl model = new PaletteModelImpl();
		model.load();
		IPaletteGroup jqmGroup = model.getPaletteGroup("jQuery Mobile");
		assertNotNull(jqmGroup);
		
		List<String> failures = new ArrayList<String>();
		for(IPaletteVersionGroup vGroup : jqmGroup.getPaletteVersionGroups()){
			for(IPaletteCategory category : vGroup.getCategories()){
				for(IPaletteItem item : category.getItems()){
					doTestPositionCorrectors(failures, item);
				}
			}
		}
		if(!failures.isEmpty()) {
			StringBuilder text = new StringBuilder();
			text.append("The following position correctors failed:\n");
			for (String s: failures) {
				text.append(s).append("\n");
			}
			fail(text.toString());
		}
	}

	private void doTestPositionCorrectors(List<String> failures, IPaletteItem item) throws Exception {
		try {
			String name = item.getName();
			if(name.equals("JS/CSS") || name.equals("Field Container")) {
				//no corrector for libraries.
				return;
			}
			IPositionCorrector corrector = item.createPositionCorrector();
			assertNotNull(corrector);
			IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
			PaletteItemDropCommand command = new PaletteItemDropCommand(item, true);
			ITextSelection s = MobilePaletteInsertHelper.getInstance().correctSelection(doc, new TextSelection(doc, 0, 0), command);
			assertNotNull(s);
		} catch (Exception e) {
			failures.add(item.getName());
		}
	}

}
