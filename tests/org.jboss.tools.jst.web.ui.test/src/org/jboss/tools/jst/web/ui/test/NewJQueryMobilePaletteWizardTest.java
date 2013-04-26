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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGridWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGridWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedCheckboxesWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedCheckboxesWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizardPage;
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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

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
		editor = openEditor("a.html");
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

	public void testNewPageWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Page", true);

		assertTrue(currentPage instanceof NewPageWizardPage);

		NewPageWizardPage wizardPage = (NewPageWizardPage)currentPage;
		NewPageWizard wizard = (NewPageWizard)wizardPage.getWizard();

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

		wizard.performFinish();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(ROLE_CONTENT) > 0);
		assertTrue(text.indexOf(ROLE_PAGE) > 0);
	}

	public void testNewCheckboxWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Checkbox", true);

		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();
		
		String label = "My Favorite Checkbox";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizard.performFinish();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewToggleWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Flip Toggle Switch", true);

		assertTrue(currentPage instanceof NewToggleWizardPage);

		NewToggleWizardPage wizardPage = (NewToggleWizardPage)currentPage;
		NewToggleWizard wizard = (NewToggleWizard)wizardPage.getWizard();
		
		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");

		assertTextExists(wizard, label);
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);
		
		assertEquals("Off", wizardPage.getEditorValue(EDITOR_ID_OFF));
		assertEquals("On", wizardPage.getEditorValue(EDITOR_ID_ON));
		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off-1");
		assertTextExists(wizard, "Off-1");

		wizard.performFinish();

		assertTextIsInserted(label);
	}

	public void testNewListviewWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Listview", true);

		assertTrue(currentPage instanceof NewListviewWizardPage);

		NewListviewWizardPage wizardPage = (NewListviewWizardPage)currentPage;
		NewListviewWizard wizard = (NewListviewWizard)wizardPage.getWizard();
		
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

		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FILTER, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_FILTER);

		wizard.performFinish();

		assertAttrIsInserted(ATTR_DATA_AUTODIVIDERS, TRUE);
	}


	public void testNewDialogWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Dialog", true);
		assertTrue(currentPage instanceof NewDialogWizardPage);

		NewDialogWizardPage wizardPage = (NewDialogWizardPage)currentPage;
		NewDialogWizard wizard = (NewDialogWizard)wizardPage.getWizard(); 

		assertTextDoesNotExist(wizard, ATTR_DATA_CLOSE_BTN);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_RIGHT);
		assertAttrExists(wizard, ATTR_DATA_CLOSE_BTN, CLOSE_RIGHT);

		String title = wizardPage.getEditorValue(EDITOR_ID_TITLE);

		wizard.performFinish();

		assertTextIsInserted(title);
	}

	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard(); 

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_CLASS);

		wizardPage.setEditorValue(EDITOR_ID_INLINE, TRUE);
		assertAttrExists(wizard, ATTR_DATA_INLINE, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_INLINE, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_INLINE);

		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "notext");
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "arrow-r");
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "notext");
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, FALSE);
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "arrow-r");

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionDialogLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_DIALOG);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionCloseLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_CLOSE);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_REL);

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizard.performFinish();

		assertTextIsInserted(label);
	}

	public void testNewLinkWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Link", true);
		assertTrue(currentPage instanceof NewLinkWizardPage);

		NewLinkWizardPage wizardPage = (NewLinkWizardPage)currentPage;
		NewLinkWizard wizard = (NewLinkWizard)wizardPage.getWizard(); 

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

		wizard.performFinish();

		assertTextIsInserted(label);
	}

	public void testNewRangeSliderWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Range Slider", true);

		assertTrue(currentPage instanceof NewRangeSliderWizardPage);

		NewRangeSliderWizardPage wizardPage = (NewRangeSliderWizardPage)currentPage;
		NewRangeSliderWizard wizard = (NewRangeSliderWizard)wizardPage.getWizard();
		
		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

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

		wizard.performFinish();

		assertTextIsInserted(label);
	}

	public void testNewTextInputWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Text Input", true);

		assertTrue(currentPage instanceof NewTextInputWizardPage);

		NewTextInputWizardPage wizardPage = (NewTextInputWizardPage)currentPage;
		NewTextInputWizard wizard = (NewTextInputWizard)wizardPage.getWizard();
		
		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

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

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, ROLE_FIELDCONTAIN);

		wizard.performFinish();

		assertTextIsInserted(wizardPage.getEditorValue(EDITOR_ID_LABEL));
	}

	public void testNewHeaderBarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Header Bar", true);

		assertTrue(currentPage instanceof NewHeaderBarWizardPage);

		NewHeaderBarWizardPage wizardPage = (NewHeaderBarWizardPage)currentPage;
		NewHeaderBarWizard wizard = (NewHeaderBarWizard)wizardPage.getWizard();
		
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);

		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextExists(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
		wizard.performFinish();

		assertTextIsInserted(wizardPage.getEditorValue(EDITOR_ID_TITLE));
	}

	public void testNewNavbarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Navbar", true);

		assertTrue(currentPage instanceof NewNavbarWizardPage);

		NewNavbarWizardPage wizardPage = (NewNavbarWizardPage)currentPage;
		NewNavbarWizard wizard = (NewNavbarWizard)wizardPage.getWizard();
		
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

		wizard.performFinish();

		assertTextIsInserted("Run Test");
	}

	public void testNewFooterBarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Footer Bar", true);

		assertTrue(currentPage instanceof NewFooterWizardPage);

		NewFooterWizardPage wizardPage = (NewFooterWizardPage)currentPage;
		NewFooterWizard wizard = (NewFooterWizard)wizardPage.getWizard();
		
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);

		String label = "Run Footer Bar Test";
		wizardPage.setEditorValue(EDITOR_ID_TITLE, label);

		wizard.performFinish();

		assertTextIsInserted(label);
	}

	public void testNewGridWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Grid", true);

		assertTrue(currentPage instanceof NewGridWizardPage);

		NewGridWizardPage wizardPage = (NewGridWizardPage)currentPage;
		NewGridWizard wizard = (NewGridWizard)wizardPage.getWizard();

		assertTextDoesNotExist(wizard, "ui-block-d");
		wizardPage.setEditorValue(EDITOR_ID_GRID_COLUMNS, "5");
		assertTextExists(wizard, "ui-block-d");

		wizard.performFinish();

		assertTextIsInserted("ui-block-d");
	}

	public void testNewGroupedCheckboxesWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Grouped Checkboxes", true);

		assertTrue(currentPage instanceof NewGroupedCheckboxesWizardPage);

		NewGroupedCheckboxesWizardPage wizardPage = (NewGroupedCheckboxesWizardPage)currentPage;
		NewGroupedCheckboxesWizard wizard = (NewGroupedCheckboxesWizard)wizardPage.getWizard();

		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);

		wizard.performFinish();
	}

	public void testNewRadioWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Radio Button", true);

		assertTrue(currentPage instanceof NewRadioWizardPage);

		NewRadioWizardPage wizardPage = (NewRadioWizardPage)currentPage;
		NewRadioWizard wizard = (NewRadioWizard)wizardPage.getWizard();

		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, DATA_TYPE_HORIZONTAL);

		wizard.performFinish();
	}

	public void testNewCollapsibleContentBlockWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Collapsible Content Block", true);
		assertTrue(currentPage instanceof NewCollapsibleWizardPage);

		NewCollapsibleWizardPage wizardPage = (NewCollapsibleWizardPage)currentPage;
		NewCollapsibleWizard wizard = (NewCollapsibleWizard)wizardPage.getWizard(); 

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

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

		wizard.performFinish();

		assertTextIsInserted(header);
	}

	public void testNewSelectMenuWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Select Menu", true);

		assertTrue(currentPage instanceof NewSelectMenuWizardPage);

		NewSelectMenuWizardPage wizardPage = (NewSelectMenuWizardPage)currentPage;
		NewSelectMenuWizard wizard = (NewSelectMenuWizard)wizardPage.getWizard();

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

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

		wizard.performFinish();
	}

	public void testNewPopupWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Popup", true);

		assertTrue(currentPage instanceof NewPopupWizardPage);
		NewPopupWizardPage wizardPage = (NewPopupWizardPage)currentPage;
		NewPopupWizard wizard = (NewPopupWizard)wizardPage.getWizard();

		assertTextDoesNotExist(wizard, "\"info\"");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ICON, "info");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, FALSE);
		assertTextDoesNotExist(wizard, "\"info\"");

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
		assertAttrExists(wizard, ATTR_CLASS, CLASS_BUTTON_LEFT);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_NONE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_LEFT);

		wizard.performFinish();
	}

	public void testNewGroupedButtonsBarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Grouped Buttons", true);

		assertTrue(currentPage instanceof NewGroupedButtonsWizardPage);

		NewGroupedButtonsWizardPage wizardPage = (NewGroupedButtonsWizardPage)currentPage;
		NewGroupedButtonsWizard wizard = (NewGroupedButtonsWizard)wizardPage.getWizard();
		
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
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "right");
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizard.performFinish();
	}

	public void testNewPanelWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Panel", true);

		assertTrue(currentPage instanceof NewPanelWizardPage);
		NewPanelWizardPage wizardPage = (NewPanelWizardPage)currentPage;
		NewPanelWizard wizard = (NewPanelWizard)wizardPage.getWizard();

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

		wizard.performFinish();
	}

	void assertAttrExists(AbstractNewHTMLWidgetWizard wizard, String attr, String value) {
		assertTextExists(wizard, attr + "=\"" + value + "\"");
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) > 0);
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
