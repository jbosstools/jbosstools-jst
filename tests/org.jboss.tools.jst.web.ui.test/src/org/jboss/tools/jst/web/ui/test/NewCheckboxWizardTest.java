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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCheckboxWizardTest extends AbstractPaletteEntryTest implements JQueryConstants {
	
	public NewCheckboxWizardTest() {}
	
	public void testNewCheckboxWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Checkbox", true);

		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();
		
		String label = "My Favorite Checkbox";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, "true");
		assertTextExists(wizard, ATTR_DATA_MINI + "=\"true\"");
		wizardPage.setEditorValue(EDITOR_ID_MINI, "false");
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
		
		editor.getSite().getPage().closeEditor(editor, false);
	}

	public void testNewToggleWizard() {
		IEditorPart editor = openEditor("a.html");

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
		assertTextExists(wizard, ATTR_DATA_MINI + "=\"true\"");
		wizardPage.setEditorValue(EDITOR_ID_MINI, "false");
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);
		
		assertEquals("Off", wizardPage.getEditorValue(EDITOR_ID_OFF));
		assertEquals("On", wizardPage.getEditorValue(EDITOR_ID_ON));
		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off-1");
		assertTextExists(wizard, "Off-1");

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
		
		editor.getSite().getPage().closeEditor(editor, false);
	}

	public void testNewDialogWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Dialog", true);
		assertTrue(currentPage instanceof NewDialogWizardPage);

		NewDialogWizardPage wizardPage = (NewDialogWizardPage)currentPage;
		NewDialogWizard wizard = (NewDialogWizard)wizardPage.getWizard(); 

		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_CLOSE_BTN) < 0);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_RIGHT);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_CLOSE_BTN + "=\"" + CLOSE_RIGHT + "\"") >= 0);

		String title = wizardPage.getEditorValue(EDITOR_ID_TITLE);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(title) > 0);
		editor.getSite().getPage().closeEditor(editor, false);
	}

	public void testNewButtonWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard(); 

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_MINI + "=\"true\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_MINI, "false");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_MINI) < 0);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_CLASS + "=\"ui-disabled\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, "false");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_CLASS) < 0);

		wizardPage.setEditorValue(EDITOR_ID_INLINE, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_INLINE + "=\"true\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_INLINE, "false");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_INLINE) < 0);

		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_ICONPOS + "=\"notext\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "arrow-r");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_ICONPOS + "=\"notext\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, "false");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_ICONPOS + "=\"arrow-r\"") > 0);

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionDialogLabel);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_REL + "=\"" + DATA_REL_DIALOG + "\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_REL) < 0);

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);

		editor.getSite().getPage().closeEditor(editor, false);
	}

	public void testNewLinkWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Link", true);
		assertTrue(currentPage instanceof NewLinkWizardPage);

		NewLinkWizardPage wizardPage = (NewLinkWizardPage)currentPage;
		NewLinkWizard wizard = (NewLinkWizard)wizardPage.getWizard(); 

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionPopupLabel);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_REL + "=\"" + DATA_REL_POPUP + "\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_REL) < 0);

		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, TRANSITION_FLIP);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_TRANSITION + "=\"" + TRANSITION_FLIP + "\"") > 0);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DATA_REL) < 0);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);

		editor.getSite().getPage().closeEditor(editor, false);
	}

	public void testNewRangeSliderWizard() {
		IEditorPart editor = openEditor("a.html");

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
		assertTextExists(wizard, ATTR_DATA_MINI + "=\"true\"");
		wizardPage.setEditorValue(EDITOR_ID_MINI, "false");
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		assertTextExists(wizard, ATTR_DATA_MIN + "=\"0\"");
		wizardPage.setEditorValue(EDITOR_ID_MIN, "10");
		assertTextExists(wizard, ATTR_DATA_MIN + "=\"10\"");

		assertTextExists(wizard, ATTR_DATA_MAX + "=\"100\"");
		wizardPage.setEditorValue(EDITOR_ID_MAX, "200");
		assertTextExists(wizard, ATTR_DATA_MAX + "=\"200\"");

		assertTextDoesNotExist(wizard, ATTR_DATA_STEP);
		wizardPage.setEditorValue(EDITOR_ID_STEP, "2");
		assertTextExists(wizard, ATTR_DATA_STEP + "=\"2\"");

		assertTextExists(wizard, ATTR_DATA_VALUE + "=\"40\"");
		wizardPage.setEditorValue(EDITOR_ID_VALUE, "50");
		assertTextExists(wizard, ATTR_DATA_VALUE + "=\"50\"");

		assertTextDoesNotExist(wizard, ATTR_DATA_VALUE + "=\"60\"");
		wizardPage.setEditorValue(EDITOR_ID_RANGE, TRUE);
		assertTextExists(wizard, ATTR_DATA_VALUE + "=\"60\"");

		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DISABLED) < 0);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(ATTR_DISABLED + "=\"disabled\"") > 0);

		assertTrue(wizard.getTextForTextView().indexOf(CLASS_HIDDEN_ACCESSIBLE) < 0);
		wizardPage.setEditorValue(EDITOR_ID_HIDE_LABEL, TRUE);
		assertTrue(wizard.getTextForTextView().indexOf(CLASS_HIDDEN_ACCESSIBLE) > 0);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
		
		editor.getSite().getPage().closeEditor(editor, false);
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) > 0);
	}

	void assertTextDoesNotExist(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) < 0);
	}

}
