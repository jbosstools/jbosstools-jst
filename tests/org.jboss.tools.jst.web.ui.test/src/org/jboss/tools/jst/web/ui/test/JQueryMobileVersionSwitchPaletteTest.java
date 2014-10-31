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
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizardPage;

public class JQueryMobileVersionSwitchPaletteTest extends AbstractPaletteEntryTest {
	IEditorPart editor = null;

	public JQueryMobileVersionSwitchPaletteTest() {}

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

	protected void checkVersion(JQueryMobileVersion version) {
		IWizardPage page = runToolEntry(JQueryConstants.JQM_CATEGORY, "Page", true);
		assertTrue(page instanceof NewJQueryWidgetWizardPage);
		NewJQueryWidgetWizardPage jpage = (NewJQueryWidgetWizardPage)page;
		assertEquals(version, jpage.getWizard().getVersion());
	}

	public void testdoNotSwitchVersionTo13() {
		editor = openEditor("a14.html");
		checkVersion(JQueryMobileVersion.JQM_1_4);
	}

	public void testSwitchVersionTo13() {
		editor = openEditor("a14.html");
		switchVersion(JQueryMobileVersion.JQM_1_3);
		checkVersion(JQueryMobileVersion.JQM_1_3);
	}

	public void testdoNotSwitchVersionTo14() {
		editor = openEditor("a.html");
		checkVersion(JQueryMobileVersion.JQM_1_3);
	}

	public void testSwitchVersionTo14() {
		editor = openEditor("a.html");
		switchVersion(JQueryMobileVersion.JQM_1_4);
		checkVersion(JQueryMobileVersion.JQM_1_4);
	}

}
