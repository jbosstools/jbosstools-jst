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

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertManager;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.JSPTagProposalFactory;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.PaletteDropCommand;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPopupWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPopupWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewSelectMenuWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewSelectMenuWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

public class NewJQueryMobile13PaletteWizardTest extends NewJQueryMobilePaletteWizardTest {

	protected String getFileName() {
		return "a.html";
	}

	protected JQueryMobileVersion getVersion() {
		return JQueryMobileVersion.JQM_1_3;
	}

	protected void doVersionSpecificTest(NewListviewWizardPage wizardPage, NewListviewWizard wizard) {
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FILTER, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_SEARCH_FILTER, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_FILTER);
	}

	protected void doVersionSpecificTest(NewSelectMenuWizardPage wizardPage, NewSelectMenuWizard wizard) {
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_HORIZONTAL);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
	}

	protected void doVersionSpecificTest(NewTextInputWizardPage wizardPage, NewTextInputWizard wizard) {
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, ROLE_FIELDCONTAIN);
	}

	protected void doVersionSpecificTest(NewPopupWizardPage wizardPage, NewPopupWizard wizard) {
		assertTextDoesNotExist(wizard, "\"info\"");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ICON, "info");
		wizardPage.setEditorValue(EDITOR_ID_INFO_STYLED, FALSE);
		assertTextDoesNotExist(wizard, "\"info\"");
	}

	protected void doVersionSpecificTest(NewDialogWizardPage wizardPage, NewDialogWizard wizard) {
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_DIALOG);
	}

	protected void doVersionSpecificTest(NewToggleWizardPage wizardPage, NewToggleWizard wizard) {
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_SLIDER);
		wizardPage.setEditorValue(EDITOR_ID_SELECTED, TRUE);
		assertAttrExists(wizard, SELECTED, SELECTED);
		wizardPage.setEditorValue(EDITOR_ID_SELECTED, FALSE);
		assertTextDoesNotExist(wizard, SELECTED);
	}

	public void doVersionSpecificTest(NewHeaderBarWizardPage wizardPage, NewHeaderBarWizard wizard) {
		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextExists(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
	}

	public void testNewFooterBarWizard() {
		IWizardPage currentPage = runToolEntry("Footer Bar", true);

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

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard(); 

		doTestMini(wizardPage, wizard);

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

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(label);
	}

	public void testNewFormButtonWizard() {
		IWizardPage currentPage = runToolEntry("Form Button", true);
		assertTrue(currentPage instanceof NewFormButtonWizardPage);

		NewFormButtonWizardPage wizardPage = (NewFormButtonWizardPage)currentPage;
		NewFormButtonWizard wizard = (NewFormButtonWizard)wizardPage.getWizard(); 

		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeSubmitLabel);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_SUBMIT);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_RESET);
		assertAttrExists(wizard, ATTR_VALUE, WizardMessages.buttonTypeResetLabel);
		wizardPage.setEditorValue(EDITOR_ID_FORM_BUTTON_TYPE, BUTTON_TYPE_BUTTON);
		assertAttrExists(wizard, ATTR_VALUE, "Input");
		assertAttrExists(wizard, ATTR_TYPE, BUTTON_TYPE_BUTTON);

		doTestMini(wizardPage, wizard);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_DISABLED, ATTR_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DISABLED);

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

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewGroupedButtonsBarWizard() {
		IWizardPage currentPage = runToolEntry("Grouped Buttons", true);

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

		doTestMini(wizardPage, wizard);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabsWizard() {
		//do nothing, no wizard in JQM 1.3.
	}

	public void testNewHeadingWizard() {
		//do nothing, no wizard in JQM 1.3.
	}

	protected void doTestSearchFilter(NewJQueryWidgetWizardPage wizardPage, NewJQueryWidgetWizard<?> wizard) {
		//no common search filter in JQM 1.3. 
		//Search filter in Listview is tested separately.
	}

}
