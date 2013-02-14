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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizardPage;
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

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "check box", true);

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

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "flip toggle switch", true);

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

}
