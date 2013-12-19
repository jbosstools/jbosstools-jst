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
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizardPage;

public class NewJQueryMobile13PaletteWizardTest extends NewJQueryMobilePaletteWizardTest {

	protected String getFileName() {
		return "a.html";
	}

	protected JQueryMobileVersion getVersion() {
		return JQueryMobileVersion.JQM_1_3;
	}

	public void testNewHeaderBarWizard() {
		IWizardPage currentPage = runToolEntry("Header Bar", true);

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

		compareGeneratedAndInsertedText(wizard);

		assertTextIsInserted(wizardPage.getEditorValue(EDITOR_ID_TITLE));
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

}
