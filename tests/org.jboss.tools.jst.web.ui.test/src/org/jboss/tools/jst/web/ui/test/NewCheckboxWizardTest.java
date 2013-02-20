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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCheckboxWizardTest extends AbstractPaletteEntryTest {
	
	public NewCheckboxWizardTest() {}
	
	public void testNewCheckboxWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Checkbox", true);

		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();
		
		String label = "My Favorite Checkbox";
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_LABEL).setValue(label);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_THEME).setValue("b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI) < 0);

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
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_LABEL).setValue(label);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_THEME).setValue("b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI) < 0);
		
		assertEquals("Off", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_OFF));
		assertEquals("On", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_ON));
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_OFF).setValueAsString("Off-1");
		assertTrue(wizard.getTextForTextView().indexOf("Off-1") > 0);

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

		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_CLOSE_BTN) < 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_CLOSE_BUTTON).setValueAsString(JQueryConstants.CLOSE_RIGHT);
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_CLOSE_BTN + "=\"" + JQueryConstants.CLOSE_RIGHT + "\"") >= 0);

		String title = wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_TITLE);

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

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_MINI).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_MINI) < 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_DISABLED).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_CLASS + "=\"ui-disabled\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_DISABLED).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_CLASS) < 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_INLINE).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_INLINE + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_INLINE).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_INLINE) < 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_ICON_ONLY).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_ICONPOS + "=\"notext\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_ICON_POS).setValueAsString("arrow-r");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_ICONPOS + "=\"notext\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_ICON_ONLY).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_ICONPOS + "=\"arrow-r\"") > 0);

		String label = wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_LABEL);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);

		editor.getSite().getPage().closeEditor(editor, false);
	}

}
