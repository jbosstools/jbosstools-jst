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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewListviewWizardTest extends AbstractPaletteEntryTest {
	
	public NewListviewWizardTest() {}
	
	public void testNewListviewWizard() {
		IEditorPart editor = openEditor("a.html");

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Listview", true);

		assertTrue(currentPage instanceof NewListviewWizardPage);

		NewListviewWizardPage wizardPage = (NewListviewWizardPage)currentPage;
		NewListviewWizard wizard = (NewListviewWizard)wizardPage.getWizard();
		
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_AUTODIVIDERS).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_AUTODIVIDERS + "=\"true\"") > 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_INSET).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_INSET + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_INSET).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_INSET) < 0);

		wizardPage.getEditor(JQueryConstants.EDITOR_ID_SEARCH_FILTER).setValueAsString("true");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_FILTER + "=\"true\"") > 0);
		wizardPage.getEditor(JQueryConstants.EDITOR_ID_SEARCH_FILTER).setValueAsString("false");
		assertTrue(wizard.getTextForTextView().indexOf(JQueryConstants.ATTR_DATA_FILTER) < 0);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(JQueryConstants.ATTR_DATA_AUTODIVIDERS + "=\"true\"") > 0);
		
		editor.getSite().getPage().closeEditor(editor, false);
	}

}
