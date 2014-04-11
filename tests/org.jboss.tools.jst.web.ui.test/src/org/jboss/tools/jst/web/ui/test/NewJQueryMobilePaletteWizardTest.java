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

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleSetWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleSetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGridWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGridWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedCheckboxesWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedCheckboxesWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeadingWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeadingWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewNavbarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewNavbarWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPanelWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPanelWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPopupWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPopupWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRadioWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRadioWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewSelectMenuWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewSelectMenuWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTableWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTableWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTabsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTabsWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteManager;
import org.jboss.tools.jst.web.ui.palette.internal.RunnablePaletteItem;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewJQueryMobilePaletteWizardTest extends AbstractPaletteEntryTest implements JQueryConstants {
	IEditorPart editor = null;
	
	public NewJQueryMobilePaletteWizardTest() {}

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
		IWizardPage result = runToolEntry(JQueryConstants.JQM_CATEGORY, entry, wizardExpected);
		if(wizardExpected) {
			assertTrue(result instanceof NewJQueryWidgetWizardPage);
			NewJQueryWidgetWizardPage page = (NewJQueryWidgetWizardPage)result;
			assertEquals(getVersion(), page.getWizard().getVersion());
		}
		return result;
	}

	protected JQueryMobileVersion getVersion() {
		return JQueryMobileVersion.JQM_1_4;
	}

	public void testScriptsAreCopied() {
		File f = WebModelPlugin.getJSStateRoot();
		assertTrue(new File(f, "js").isDirectory());
		assertTrue(new File(f, "js/jquery.mobile.structure-1.2.0.min.css").isFile());
	}

	public void testNewPageWizard() {
		IWizardPage currentPage = runToolEntry("Page", true);

		assertTrue(currentPage instanceof NewPageWizardPage);

		NewPageWizardPage wizardPage = (NewPageWizardPage)currentPage;
		NewPageWizard wizard = (NewPageWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Page");

		assertTextExists(wizard, "\t");
		IPreferenceStore s = EditorsPlugin.getDefault().getPreferenceStore();
		s.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS, true);
		assertTextDoesNotExist(wizard, "\t");
		s.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH, 12);
		assertTextExists(wizard, "            <");
		s.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH, 4);
		assertTextDoesNotExist(wizard, "            <");
		assertTextExists(wizard, "    <");
		s.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS, false);
		assertTextExists(wizard, "\t");

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_HEADER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_HEADER, FALSE);
		assertTextDoesNotExist(wizard, ROLE_HEADER);
		assertTextDoesNotExist(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_HEADER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_HEADER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FOOTER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_FOOTER, FALSE);
		assertTextDoesNotExist(wizard, ROLE_FOOTER);
		assertTextDoesNotExist(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_FOOTER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FOOTER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));

		assertTextDoesNotExist(wizard, ATTR_DATA_REL);
		wizardPage.setEditorValue(EDITOR_ID_BACK_BUTTON, TRUE);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_BACK);

		//Check " < > in attribute value.
		assertTextDoesNotExist(wizard, ATTR_DATA_THEME);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "\"</div>");
		assertAttrExists(wizard, ATTR_DATA_THEME, "&quot;&lt;/div&gt;");
		wizardPage.setEditorValue(EDITOR_ID_THEME, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_THEME);

		//Check < > in text.
		String headerText = "<page title>";
		String headerHtmlText = "&lt;page title&gt;";

		wizardPage.setEditorValue(EDITOR_ID_HEADER_TITLE, headerText);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(ROLE_CONTENT);
		assertTextIsInserted(ROLE_PAGE);
		assertTextIsInserted(headerHtmlText);
	}

	public void testApplyWithoutUI() {
		Collection<RunnablePaletteItem> items = PaletteManager.getInstance().getItems(JQueryConstants.JQM_CATEGORY, getVersion().toString());
		ITextSelection s = (ITextSelection)textEditor.getSelectionProvider().getSelection();
		for (RunnablePaletteItem item: items) {
			if("Button".equals(item.getName())) {
				item.apply(textEditor, s.getOffset(), s.getOffset());
				break;
			}
		}
		assertTextIsInserted("button-1");
	}

	public void testNewHeaderBarWizard() {
		IWizardPage currentPage = runToolEntry("Header Bar", true);

		assertTrue(currentPage instanceof NewHeaderBarWizardPage);

		NewHeaderBarWizardPage wizardPage = (NewHeaderBarWizardPage)currentPage;
		NewHeaderBarWizard wizard = (NewHeaderBarWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Header Bar");

		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);

		doVersionSpecificTest(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(wizardPage.getEditorValue(EDITOR_ID_TITLE));
	}

	public void testNewFooterBarWizard() {
		IWizardPage currentPage = runToolEntry("Footer Bar", true);

		assertTrue(currentPage instanceof NewFooterWizardPage);

		NewFooterWizardPage wizardPage = (NewFooterWizardPage)currentPage;
		NewFooterWizard wizard = (NewFooterWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Footer Bar");

		IFieldEditor barpos = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_BAR_POSITION)).getEditors().get(0);
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);
	
		assertFalse("Bar position editor should be disabled for default arrangement if button number is not 1.",
				barpos.isEnabled());
		assertTextDoesNotExist(wizard, ROLE_CONTROLGROUP);
		wizardPage.setEditorValue(EDITOR_ID_ARRAGEMENT, ARRAGEMENT_GROUPED);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_CONTROLGROUP);
		assertTrue("Bar position editor should be enabled for grouped arrangement.",
				barpos.isEnabled());
		wizardPage.setEditorValue(EDITOR_ID_BAR_POSITION, "right");
		assertTextExists(wizard, CLASS_UI_BTN_PREFIX + "right");
		wizardPage.setEditorValue(EDITOR_ID_ARRAGEMENT, ARRAGEMENT_NAVBAR);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_NAVBAR);
		assertFalse("Bar position editor should be disabled for navbar arrangement.",
				barpos.isEnabled());
		assertTextDoesNotExist(wizard, CLASS_UI_BTN_PREFIX + "right");
		wizardPage.setEditorValue(EDITOR_ID_ARRAGEMENT, ARRAGEMENT_DEFAULT);
		assertFalse(barpos.isEnabled());
		assertTextDoesNotExist(wizard, CLASS_UI_BTN_PREFIX + "right");

		String label = "Run Footer Bar Test";
		wizardPage.setEditorValue(EDITOR_ID_TITLE, label);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	public void doVersionSpecificTest(NewHeaderBarWizardPage wizardPage, NewHeaderBarWizard wizard) {
		assertTextExists(wizard, CLASS_BUTTON_LEFT);
		assertTextExists(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_LEFT);
		wizardPage.setEditorValue(EDITOR_ID_RIGHT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextExists(wizard, CLASS_BUTTON_LEFT);
		wizardPage.setEditorValue(EDITOR_ID_RIGHT_BUTTON, TRUE);
		assertTextExists(wizard, CLASS_BUTTON_RIGHT);
	}

	public void testNewCheckboxWizard() {
		IWizardPage currentPage = runToolEntry("Checkbox", true);

		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Checkbox");

		String label = "My Favorite Checkbox";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		doTestMini(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	protected void doTestMini(NewJQueryWidgetWizardPage wizardPage, NewJQueryWidgetWizard wizard) {
		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);
	}

	public void testNewToggleWizard() {
		IWizardPage currentPage = runToolEntry("Flip Toggle Switch", true);

		assertTrue(currentPage instanceof NewToggleWizardPage);

		NewToggleWizardPage wizardPage = (NewToggleWizardPage)currentPage;
		NewToggleWizard wizard = (NewToggleWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Flip Toggle Switch");

		doVersionSpecificTest(wizardPage, wizard);

		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");

		assertTextExists(wizard, label);
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);

		doTestMini(wizardPage, wizard);
		
		assertEquals("Off", wizardPage.getEditorValue(EDITOR_ID_OFF));
		assertEquals("On", wizardPage.getEditorValue(EDITOR_ID_ON));
		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off-1");
		assertTextExists(wizard, "Off-1");

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	protected void doVersionSpecificTest(NewToggleWizardPage wizardPage, NewToggleWizard wizard) {
		//checkbox is default
		assertTextExists(wizard, TAG_INPUT);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_CHECKBOX);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FLIPSWITCH);

		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off-1");
		assertAttrExists(wizard, ATTR_DATA_OFF_TEXT, "Off-1");
		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off");
		assertTextDoesNotExist(wizard, ATTR_DATA_OFF_TEXT);

		wizardPage.setEditorValue(EDITOR_ID_ON, "On-1");
		assertAttrExists(wizard, ATTR_DATA_ON_TEXT, "On-1");
		wizardPage.setEditorValue(EDITOR_ID_ON, "On");
		assertTextDoesNotExist(wizard, ATTR_DATA_ON_TEXT);

		wizardPage.setEditorValue(EDITOR_ID_SELECTED, TRUE);
		assertAttrExists(wizard, CHECKED, CHECKED);
		wizardPage.setEditorValue(EDITOR_ID_SELECTED, FALSE);
		assertTextDoesNotExist(wizard, CHECKED);
		
		//change to select
		wizardPage.setEditorValue(EDITOR_ID_TOGGLE_KIND, TOGGLE_KIND_SELECT);
		assertTextExists(wizard, TAG_SELECT);
		assertTextDoesNotExist(wizard, TYPE_CHECKBOX);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FLIPSWITCH);

		wizardPage.setEditorValue(EDITOR_ID_SELECTED, TRUE);
		assertAttrExists(wizard, SELECTED, SELECTED);
		wizardPage.setEditorValue(EDITOR_ID_SELECTED, FALSE);
		assertTextDoesNotExist(wizard, SELECTED);

		//change back to checkbox
		wizardPage.setEditorValue(EDITOR_ID_TOGGLE_KIND, TOGGLE_KIND_CHECKBOX);
		assertTextExists(wizard, TAG_INPUT);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_CHECKBOX);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FLIPSWITCH);

		
	}

	public void testNewListviewWizard() {
		IWizardPage currentPage = runToolEntry("Listview", true);

		assertTrue(currentPage instanceof NewListviewWizardPage);

		NewListviewWizardPage wizardPage = (NewListviewWizardPage)currentPage;
		NewListviewWizard wizard = (NewListviewWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Listview");

		assertEquals("3", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, "Item 4");
		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));
		assertTextExists(wizard, "Item 4");		
	
		wizardPage.setEditorValue(EDITOR_ID_AUTODIVIDERS, TRUE);
		assertAttrExists(wizard, ATTR_DATA_AUTODIVIDERS, TRUE);

		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_INSET, TRUE);
		assertAttrExists(wizard, ATTR_DATA_INSET, TRUE);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_INSET, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_INSET);

		doVersionSpecificTest(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);

		assertAttrIsInserted(ATTR_DATA_AUTODIVIDERS, TRUE);
	}

	protected void doVersionSpecificTest(NewListviewWizardPage wizardPage, NewListviewWizard wizard) {
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_UI_FILTERABLE);
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_FILTERABLE);
		doTestSearchFilter(wizardPage, wizard);
	}

	public void testNewDialogWizard() {
		IWizardPage currentPage = runToolEntry("Dialog", true);
		assertTrue(currentPage instanceof NewDialogWizardPage);

		NewDialogWizardPage wizardPage = (NewDialogWizardPage)currentPage;
		NewDialogWizard wizard = (NewDialogWizard)wizardPage.getWizard(); 

		compareUIAndNonUIWizards(wizard, "Dialog");

		doVersionSpecificTest(wizardPage, wizard);

		assertTextDoesNotExist(wizard, ATTR_DATA_CLOSE_BTN);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_RIGHT);
		assertAttrExists(wizard, ATTR_DATA_CLOSE_BTN, CLOSE_RIGHT);

		String title = wizardPage.getEditorValue(EDITOR_ID_TITLE);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(title);
	}

	protected void doVersionSpecificTest(NewDialogWizardPage wizardPage, NewDialogWizard wizard) {
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_PAGE);
		assertAttrExists(wizard,  ATTR_DATA_DIALOG, TRUE);
		assertTextExists(wizard, CLASS_UI_BTN);
	}
			
	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard(); 

		compareUIAndNonUIWizards(wizard, "Button");

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertTextExists(wizard, CLASS_UI_MINI);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_MINI);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertTextExists(wizard, CLASS_UI_STATE_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_STATE_DISABLED);

		wizardPage.setEditorValue(EDITOR_ID_INLINE, TRUE);
		assertTextExists(wizard, CLASS_UI_BTN_INLINE);
		wizardPage.setEditorValue(EDITOR_ID_INLINE, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_BTN_INLINE);

		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertTextExists(wizard, CLASS_UI_BTN_ICON_NOTEXT);
		wizardPage.setEditorValue(EDITOR_ID_ICON, "alert");
		assertTextExists(wizard, "ui-icon-alert");
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "right");
		assertTextExists(wizard, CLASS_UI_BTN_ICON_NOTEXT);
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, FALSE);
		System.out.println(wizard.getTextForBrowser());
		assertTextExists(wizard, CLASS_UI_BTN_ICON_PREFIX + "right");

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionDialogLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_DIALOG);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionCloseLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_CLOSE);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_REL);

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	private void compareUIAndNonUIWizards(IDropWizard wizard, String itemName) {
		String startText = wizard.getWizardModel().getElementGenerator().generateStartTag();
		String endText = wizard.getWizardModel().getElementGenerator().generateEndTag();
		PaletteItemResult result = NewJQueryWidgetWizard.runWithoutUi(textEditor, getVersion(), itemName);
		assertNotNull(result);
		assertEquals(startText, result.getStartText());
		assertEquals(endText, result.getEndText());
	}

	public void testNewFormButtonWizard() {
		IWizardPage currentPage = runToolEntry("Form Button", true);
		assertTrue(currentPage instanceof NewFormButtonWizardPage);

		NewFormButtonWizardPage wizardPage = (NewFormButtonWizardPage)currentPage;
		NewFormButtonWizard wizard = (NewFormButtonWizard)wizardPage.getWizard(); 

		compareUIAndNonUIWizards(wizard, "Form Button");

		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeSubmitLabel);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_SUBMIT);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeResetLabel);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_BUTTON);
		assertAttrExists(wizard, ATTR_VALUE, "Input");
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_BUTTON);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertTextExists(wizard, CLASS_UI_MINI);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_MINI);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertTextExists(wizard, CLASS_UI_STATE_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_STATE_DISABLED);

		wizardPage.setEditorValue(EDITOR_ID_INLINE, TRUE);
		assertTextExists(wizard, CLASS_UI_BTN_INLINE);
		wizardPage.setEditorValue(EDITOR_ID_INLINE, FALSE);
		assertTextDoesNotExist(wizard, CLASS_UI_BTN_INLINE);

		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertTextExists(wizard, CLASS_UI_BTN_ICON_NOTEXT);
		wizardPage.setEditorValue(EDITOR_ID_ICON, "alert");
		assertTextExists(wizard, "ui-icon-alert");
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "right");
		assertTextExists(wizard, CLASS_UI_BTN_ICON_NOTEXT);
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, FALSE);
		assertTextExists(wizard, CLASS_UI_BTN_ICON_PREFIX + "right");

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewLinkWizard() {
		IWizardPage currentPage = runToolEntry("Link", true);
		assertTrue(currentPage instanceof NewLinkWizardPage);

		NewLinkWizardPage wizardPage = (NewLinkWizardPage)currentPage;
		NewLinkWizard wizard = (NewLinkWizard)wizardPage.getWizard(); 

		compareUIAndNonUIWizards(wizard, "Link");

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionPopupLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_POPUP);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionCloseLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_CLOSE);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_REL);

		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, TRANSITION_FLIP);
		assertAttrExists(wizard, ATTR_DATA_TRANSITION, TRANSITION_FLIP);
		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_TRANSITION);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	public void testNewRangeSliderWizard() {
		IWizardPage currentPage = runToolEntry("Range Slider", true);

		assertTrue(currentPage instanceof NewRangeSliderWizardPage);

		NewRangeSliderWizardPage wizardPage = (NewRangeSliderWizardPage)currentPage;
		NewRangeSliderWizard wizard = (NewRangeSliderWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Range Slider");

		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		doTestMini(wizardPage, wizard);

		assertAttrExists(wizard, ATTR_DATA_MIN, "0");
		wizardPage.setEditorValue(EDITOR_ID_MIN, "10");
		assertAttrExists(wizard, ATTR_DATA_MIN, "10");

		assertAttrExists(wizard, ATTR_DATA_MAX, "100");
		wizardPage.setEditorValue(EDITOR_ID_MAX, "200");
		assertAttrExists(wizard, ATTR_DATA_MAX, "200");

		assertTextDoesNotExist(wizard, ATTR_DATA_STEP);
		wizardPage.setEditorValue(EDITOR_ID_STEP, "2");
		assertAttrExists(wizard, ATTR_DATA_STEP, "2");

		assertAttrExists(wizard, ATTR_DATA_VALUE, "40");
		wizardPage.setEditorValue(EDITOR_ID_VALUE, "50");
		assertAttrExists(wizard, ATTR_DATA_VALUE, "50");

		assertTextDoesNotExist(wizard, ATTR_DATA_VALUE + "=\"60\"");
		wizardPage.setEditorValue(EDITOR_ID_RANGE, TRUE);
		assertAttrExists(wizard, ATTR_DATA_VALUE, "60");

		assertTextDoesNotExist(wizard, ATTR_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_DISABLED, ATTR_DISABLED);

		assertTextDoesNotExist(wizard, CLASS_HIDDEN_ACCESSIBLE);
		wizardPage.setEditorValue(EDITOR_ID_HIDE_LABEL, TRUE);
		assertTextExists(wizard, CLASS_HIDDEN_ACCESSIBLE);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	public void testNewTextInputWizard() {
		IWizardPage currentPage = runToolEntry("Text Input", true);

		assertTrue(currentPage instanceof NewTextInputWizardPage);

		NewTextInputWizardPage wizardPage = (NewTextInputWizardPage)currentPage;
		NewTextInputWizard wizard = (NewTextInputWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Text Input");

		doTestMini(wizardPage, wizard);

		assertAttrExists(wizard, ATTR_TYPE, TYPE_TEXT);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_COLOR);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_COLOR);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_TEXTAREA);
		assertTextDoesNotExist(wizard, ATTR_TYPE);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_SEARCH);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_SEARCH);
		
		assertAttrExists(wizard, ATTR_DATA_CLEAR_BTN, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_CLEAR_INPUT, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_CLEAR_BTN);
		wizardPage.setEditorValue(EDITOR_ID_CLEAR_INPUT, TRUE);
		assertAttrExists(wizard, ATTR_DATA_CLEAR_BTN, TRUE);

		wizardPage.setEditorValue(EDITOR_ID_VALUE, "777");
		assertAttrExists(wizard, ATTR_DATA_VALUE, "777");

		wizardPage.setEditorValue(EDITOR_ID_PLACEHOLDER, "abcdef");
		assertAttrExists(wizard, ATTR_PLACEHOLDER, "abcdef");

		doVersionSpecificTest(wizardPage, wizard);

		IFieldEditor min = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_MIN)).getEditors().get(1);
		assertFalse(min.isEnabled());
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, "number");
		assertTrue(min.isEnabled());

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
		assertNull(wizardPage.getMessage());
		wizardPage.setEditorValue(EDITOR_ID_PATTERN, "");
		assertTextDoesNotExist(wizard, ATTR_PATTERN);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(wizardPage.getEditorValue(EDITOR_ID_LABEL));
	}

	protected void doVersionSpecificTest(NewTextInputWizardPage wizardPage, NewTextInputWizard wizard) {
		assertAttrExists(wizard, ATTR_CLASS, CLASS_UI_FIELD_CONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, CLASS_UI_FIELD_CONTAIN);
	}

	public void testNewNavbarWizard() {
		IWizardPage currentPage = runToolEntry("Navbar", true);

		assertTrue(currentPage instanceof NewNavbarWizardPage);

		NewNavbarWizardPage wizardPage = (NewNavbarWizardPage)currentPage;
		NewNavbarWizard wizard = (NewNavbarWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Navbar");

		assertEquals("3", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));
		IFieldEditor iconPos = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_ICON_POS)).getEditors().get(0);
		assertFalse(iconPos.isEnabled());
		wizardPage.setEditorValue(EDITOR_ID_ICON, "delete");
		assertTrue(iconPos.isEnabled());
		assertTextDoesNotExist(wizard, ATTR_DATA_ICONPOS);
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "left");
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "left");

		wizardPage.setEditorValue(EDITOR_ID_LABEL, "Run Test");

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted("Run Test");
	}

	public void testNewGridWizard() {
		IWizardPage currentPage = runToolEntry("Grid", true);

		assertTrue(currentPage instanceof NewGridWizardPage);

		NewGridWizardPage wizardPage = (NewGridWizardPage)currentPage;
		NewGridWizard wizard = (NewGridWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Grid");

		assertTextDoesNotExist(wizard, "ui-block-d");
		wizardPage.setEditorValue(EDITOR_ID_GRID_COLUMNS, "5");
		assertTextExists(wizard, "ui-block-d");

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted("ui-block-d");
	}

	public void testNewGroupedCheckboxesWizard() {
		IWizardPage currentPage = runToolEntry("Grouped Checkboxes", true);

		assertTrue(currentPage instanceof NewGroupedCheckboxesWizardPage);

		NewGroupedCheckboxesWizardPage wizardPage = (NewGroupedCheckboxesWizardPage)currentPage;
		NewGroupedCheckboxesWizard wizard = (NewGroupedCheckboxesWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Grouped Checkboxes");

		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewRadioWizard() {
		IWizardPage currentPage = runToolEntry("Radio Button", true);

		assertTrue(currentPage instanceof NewRadioWizardPage);

		NewRadioWizardPage wizardPage = (NewRadioWizardPage)currentPage;
		NewRadioWizard wizard = (NewRadioWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Radio Button");

		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewCollapsibleContentBlockWizard() {
		IWizardPage currentPage = runToolEntry("Collapsible Content Block", true);
		assertTrue(currentPage instanceof NewCollapsibleWizardPage);

		NewCollapsibleWizardPage wizardPage = (NewCollapsibleWizardPage)currentPage;
		NewCollapsibleWizard wizard = (NewCollapsibleWizard)wizardPage.getWizard(); 

		compareUIAndNonUIWizards(wizard, "Collapsible Content Block");

		doTestMini(wizardPage, wizard);

		assertTextDoesNotExist(wizard, ATTR_DATA_INSET);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_INSET, FALSE);
		assertAttrExists(wizard, ATTR_DATA_INSET, FALSE);

		assertTextDoesNotExist(wizard, ATTR_DATA_COLLAPSED_ICON);
		wizardPage.setEditorValue(EDITOR_ID_COLLAPSED_ICON, "arrow-r");
		assertAttrExists(wizard, ATTR_DATA_COLLAPSED_ICON, "arrow-r");

		assertTextDoesNotExist(wizard, ATTR_DATA_EXPANDED_ICON);
		wizardPage.setEditorValue(EDITOR_ID_EXPANDED_ICON, "arrow-u");
		assertAttrExists(wizard, ATTR_DATA_EXPANDED_ICON, "arrow-u");

		assertTextDoesNotExist(wizard, TAG_DIV);
		assertTextExists(wizard, TAG_FIELDSET);
		wizardPage.setEditorValue(EDITOR_ID_FIELD_SET, FALSE);
		assertTextDoesNotExist(wizard, TAG_FIELDSET);
		assertTextExists(wizard, TAG_DIV);

		String defaultHeader = "Header";
		String header = "My Collapsible";
		assertTextExists(wizard, defaultHeader);
		wizardPage.setEditorValue(EDITOR_ID_HEADER, header);
		assertTextExists(wizard, header);
		assertTextDoesNotExist(wizard, defaultHeader);

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(header);
	}

	public void testNewSelectMenuWizard() {
		IWizardPage currentPage = runToolEntry("Select Menu", true);

		assertTrue(currentPage instanceof NewSelectMenuWizardPage);

		NewSelectMenuWizardPage wizardPage = (NewSelectMenuWizardPage)currentPage;
		NewSelectMenuWizard wizard = (NewSelectMenuWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Select Menu");

		doVersionSpecificTest(wizardPage, wizard);

		doTestMini(wizardPage, wizard);

		assertTextDoesNotExist(wizard, CLASS_HIDDEN_ACCESSIBLE);
		wizardPage.setEditorValue(EDITOR_ID_HIDE_LABEL, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
		wizardPage.setEditorValue(EDITOR_ID_HIDE_LABEL, FALSE);
		assertTextDoesNotExist(wizard, CLASS_HIDDEN_ACCESSIBLE);

		assertTextDoesNotExist(wizard, ATTR_DATA_CORNERS);
		wizardPage.setEditorValue(EDITOR_ID_CORNERS, FALSE);
		assertAttrExists(wizard, ATTR_DATA_CORNERS, FALSE);
		wizardPage.setEditorValue(EDITOR_ID_CORNERS, TRUE);
		assertTextDoesNotExist(wizard, ATTR_DATA_CORNERS);

		doTestSearchFilter(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);
	}

	protected void doVersionSpecificTest(NewSelectMenuWizardPage wizardPage, NewSelectMenuWizard wizard) {
		assertAttrExists(wizard, ATTR_CLASS, CLASS_UI_FIELD_CONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, CLASS_UI_FIELD_CONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_UI_FIELD_CONTAIN);

		doTestSearchFilter(wizardPage, wizard);
	}

	protected void doTestSearchFilter(NewJQueryWidgetWizardPage wizardPage, NewJQueryWidgetWizard<?> wizard) {
		assertTextDoesNotExist(wizard, ATTR_DATA_TYPE);
		assertTextDoesNotExist(wizard, ATTR_DATA_FILTER);
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_TYPE, TYPE_SEARCH);		
		assertAttrExists(wizard, ATTR_DATA_FILTER, TRUE);		
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_TYPE);
		assertTextDoesNotExist(wizard, ATTR_DATA_FILTER);
		assertTextDoesNotExist(wizard, TYPE_SEARCH);
	}

	public void testNewPopupWizard() {
		IWizardPage currentPage = runToolEntry("Popup", true);

		assertTrue(currentPage instanceof NewPopupWizardPage);
		NewPopupWizardPage wizardPage = (NewPopupWizardPage)currentPage;
		NewPopupWizard wizard = (NewPopupWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Popup");

		doVersionSpecificTest(wizardPage, wizard);

		assertTextDoesNotExist(wizard, ATTR_DATA_TRANSITION);
		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, TRANSITION_POP);
		assertAttrExists(wizard, ATTR_DATA_TRANSITION, TRANSITION_POP);
		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_TRANSITION);

		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION_TO);
		wizardPage.setEditorValue(EDITOR_ID_POSITION_TO, POSITION_TO_WINDOW);
		assertAttrExists(wizard, ATTR_DATA_POSITION_TO, POSITION_TO_WINDOW);
		wizardPage.setEditorValue(EDITOR_ID_POSITION_TO, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION_TO);

		assertTextDoesNotExist(wizard, CLASS_BUTTON_LEFT);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_LEFT);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_BACK);
		assertTextExists(wizard, CLASS_BUTTON_LEFT);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_NONE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_LEFT);

		compareGeneratedAndInsertedText(wizard);
	}

	protected void doVersionSpecificTest(NewPopupWizardPage wizardPage, NewPopupWizard wizard) {
		assertTextDoesNotExist(wizard, "\"info\"");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, TRUE);
		assertTextExists(wizard, CLASS_UI_ICON_PREFIX + "info");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, FALSE);
		assertTextDoesNotExist(wizard, "\"info\"");
	
		assertTextDoesNotExist(wizard, ATTR_DATA_ARROW);
		wizardPage.setEditorValue(ATTR_DATA_ARROW, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ARROW, TRUE);
		wizardPage.setEditorValue(ATTR_DATA_ARROW, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_ARROW);
	}

	public void testNewGroupedButtonsBarWizard() {
		IWizardPage currentPage = runToolEntry("Grouped Buttons", true);

		assertTrue(currentPage instanceof NewGroupedButtonsWizardPage);

		NewGroupedButtonsWizardPage wizardPage = (NewGroupedButtonsWizardPage)currentPage;
		NewGroupedButtonsWizard wizard = (NewGroupedButtonsWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Grouped Buttons");

		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);

		assertEquals("3", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));
		IFieldEditor iconPos = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_ICON_POS)).getEditors().get(0);
		IFieldEditor icononly = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_ICON_ONLY)).getEditors().get(0);
		assertFalse(iconPos.isEnabled());
		assertFalse(icononly.isEnabled());
		wizardPage.setEditorValue(EDITOR_ID_ICON, "delete");
		assertTrue(iconPos.isEnabled());
		assertTrue(icononly.isEnabled());
		assertTextDoesNotExist(wizard, ATTR_DATA_ICONPOS);
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "right");
		assertTextExists(wizard, "ui-btn-icon-right");
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertTextExists(wizard, "ui-btn-icon-notext");

		doTestMini(wizardPage, wizard);

		doTestSearchFilter(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewPanelWizard() {
		IWizardPage currentPage = runToolEntry("Panel", true);

		assertTrue(currentPage instanceof NewPanelWizardPage);
		NewPanelWizardPage wizardPage = (NewPanelWizardPage)currentPage;
		NewPanelWizard wizard = (NewPanelWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Panel");

		assertTextDoesNotExist(wizard, ATTR_DATA_DISPLAY);
		wizardPage.setEditorValue(EDITOR_ID_DISPLAY, DISPLAY_OVERLAY);
		assertAttrExists(wizard, ATTR_DATA_DISPLAY, DISPLAY_OVERLAY);
		wizardPage.setEditorValue(EDITOR_ID_DISPLAY, DISPLAY_PUSH);
		assertAttrExists(wizard, ATTR_DATA_DISPLAY, DISPLAY_PUSH);
		wizardPage.setEditorValue(EDITOR_ID_DISPLAY, DISPLAY_REVEAL);
		assertTextDoesNotExist(wizard, ATTR_DATA_DISPLAY);

		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_PANEL_POSITION, POSITION_RIGHT);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_PANEL_POSITION, POSITION_LEFT);
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);

		assertTextDoesNotExist(wizard, ATTR_DATA_DISMISSABLE);
		wizardPage.setEditorValue(EDITOR_ID_DISMISSABLE, FALSE);
		assertAttrExists(wizard, ATTR_DATA_DISMISSABLE, FALSE);
		wizardPage.setEditorValue(EDITOR_ID_DISMISSABLE, TRUE);
		assertTextDoesNotExist(wizard, ATTR_DATA_DISMISSABLE);

		assertTextDoesNotExist(wizard, ATTR_DATA_SWIPE_CLOSE);
		wizardPage.setEditorValue(EDITOR_ID_SWIPE_CLOSE, FALSE);
		assertAttrExists(wizard, ATTR_DATA_SWIPE_CLOSE, FALSE);
		wizardPage.setEditorValue(EDITOR_ID_SWIPE_CLOSE, TRUE);
		assertTextDoesNotExist(wizard, ATTR_DATA_SWIPE_CLOSE);

		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION_FIXED);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION_FIXED, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION_FIXED);

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_LISTVIEW);
		wizardPage.setEditorValue(EDITOR_ID_ADD_LIST, FALSE);
		assertTextDoesNotExist(wizard, ROLE_LISTVIEW);
		wizardPage.setEditorValue(EDITOR_ID_ADD_LIST, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_LISTVIEW);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTableWizard() {
		IWizardPage currentPage = runToolEntry("Table", true);

		assertTrue(currentPage instanceof NewTableWizardPage);
		NewTableWizardPage wizardPage = (NewTableWizardPage)currentPage;
		NewTableWizard wizard = (NewTableWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Table");

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_TABLE);

		assertTextExists(wizard, CLASS_RESPONSIVE);
		wizardPage.setEditorValue(EDITOR_ID_RESPONSIVE, FALSE);
		assertTextDoesNotExist(wizard, CLASS_RESPONSIVE);
		wizardPage.setEditorValue(EDITOR_ID_RESPONSIVE, TRUE);
		assertTextExists(wizard, CLASS_RESPONSIVE);

		assertTextDoesNotExist(wizard, CLASS_TABLE_STRIPE);
		wizardPage.setEditorValue(EDITOR_ID_STRIPES, TRUE);
		assertTextExists(wizard, CLASS_TABLE_STRIPE);
		wizardPage.setEditorValue(EDITOR_ID_STRIPES, FALSE);
		assertTextDoesNotExist(wizard, CLASS_TABLE_STRIPE);

		doTestSearchFilter(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewCollapsibleSetBarWizard() {
		IWizardPage currentPage = runToolEntry("Collapsible Set", true);

		assertTrue(currentPage instanceof NewCollapsibleSetWizardPage);

		NewCollapsibleSetWizardPage wizardPage = (NewCollapsibleSetWizardPage)currentPage;
		NewCollapsibleSetWizard wizard = (NewCollapsibleSetWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Collapsible Set");

		assertEquals("3", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		wizardPage.setEditorValue(EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_DATA_EXPANDED_ICON);
		wizardPage.setEditorValue(EDITOR_ID_EXPANDED_ICON, "delete");
		assertAttrExists(wizard, ATTR_DATA_EXPANDED_ICON, "delete");
		wizardPage.setEditorValue(EDITOR_ID_EXPANDED_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_EXPANDED_ICON);
	
		assertTextDoesNotExist(wizard, ATTR_DATA_ICONPOS);
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "right");
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "right");

		doTestMini(wizardPage, wizard);

		doTestSearchFilter(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabsWizard() {
		IWizardPage currentPage = runToolEntry("Tabs", true);

		assertTrue(currentPage instanceof NewTabsWizardPage);

		NewTabsWizardPage wizardPage = (NewTabsWizardPage)currentPage;
		NewTabsWizard wizard = (NewTabsWizard)wizardPage.getWizard();
		
		compareUIAndNonUIWizards(wizard, "Tabs");

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_NAVBAR);
		wizardPage.setEditorValue(EDITOR_ID_TABS_LAYOUT, ROLE_LISTVIEW);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_LISTVIEW);
		wizardPage.setEditorValue(EDITOR_ID_TABS_LAYOUT, ROLE_NAVBAR);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_NAVBAR);

		assertTextDoesNotExist(wizard, "collapsible");
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLLAPSIBLE, TRUE);
		assertTextExists(wizard, "collapsible: true");
		assertTextDoesNotExist(wizard, "active");
		wizardPage.setEditorValue(EDITOR_ID_COLLAPSED, TRUE);
		assertTextExists(wizard, "active: false");
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLLAPSIBLE, FALSE);
		assertTextDoesNotExist(wizard, "collapsible");
		assertTextDoesNotExist(wizard, "active");

		assertTextDoesNotExist(wizard, "event");
		wizardPage.setEditorValue(EDITOR_ID_TABS_ACTIVATION, MOUSEOVER);
		assertTextExists(wizard, "event: \"mouseover\"");
		wizardPage.setEditorValue(EDITOR_ID_TABS_ACTIVATION, CLICK);
		assertTextDoesNotExist(wizard, "event");
		
		assertTextDoesNotExist(wizard, "show");
		wizardPage.setEditorValue(EDITOR_ID_TABS_ANIMATED, TRUE);
		assertTextExists(wizard, "show: true");
		wizardPage.setEditorValue(EDITOR_ID_TABS_ANIMATED, FALSE);
		assertTextDoesNotExist(wizard, "show");
		
		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewHeadingWizard() {
		IWizardPage currentPage = runToolEntry("Heading", true);

		assertTrue(currentPage instanceof NewHeadingWizardPage);

		NewHeadingWizardPage wizardPage = (NewHeadingWizardPage)currentPage;
		NewHeadingWizard wizard = (NewHeadingWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Heading");

		assertTextExists(wizard, "<h3 id=\"heading-1\" class=\"ui-bar ui-bar-a ui-corner-all\">");
		assertTextExists(wizard, "<div class=\"ui-body ui-body-a ui-corner-all\">");
		wizardPage.setEditorValue(EDITOR_ID_HEADING_SIZE, "h5");
		assertTextExists(wizard, "<h5");
		wizardPage.setEditorValue(EDITOR_ID_HEADING_SIZE, "h3");
		assertTextExists(wizard, "<h3");
		wizardPage.setEditorValue(EDITOR_ID_CORNERS, FALSE);
		assertAttrExists(wizard, ATTR_CLASS, "ui-bar ui-bar-a");
		wizardPage.setEditorValue(EDITOR_ID_CORNERS, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, "ui-bar ui-bar-a ui-corner-all");

		wizardPage.setEditorValue(EDITOR_ID_HEADING_LAYOUT, HEADING_LAYOUT_COMBINED);
		assertTextExists(wizard, "<div id=\"heading-1\" class=\"ui-body ui-body-a ui-corner-all\">");

		wizardPage.setEditorValue(EDITOR_ID_HEADING_LAYOUT, HEADING_LAYOUT_ATTACHED);
		assertTextExists(wizard, "<div id=\"heading-1\" class=\"ui-corner-all custom-corners\">");

		compareGeneratedAndInsertedText(wizard);
	}

	protected void compareGeneratedAndInsertedText(NewJQueryWidgetWizard<?> wizard) {
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

}
