/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.common.ui.preferences.SeverityPreferencePage;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.preferences.WebXMLPreferencesMessages;
import org.jboss.tools.jst.web.ui.internal.preferences.WebXMLValidatorPreferencePage;
import org.jboss.tools.jst.web.validation.WebXMLPreferences;

import junit.framework.TestCase;

/**
 * @author Viacheslav Kabanovich
 */
public class ConfigurationBlockTest extends TestCase {
	
	public void testFilter() {
		String preferencePageId = WebXMLValidatorPreferencePage.PREF_ID;
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(WebUiPlugin.getShell(),
				preferencePageId,
				new String[]{preferencePageId},
				WebXMLPreferences.INVALID_ROLE_REF);
		Object selectedPage = dialog.getSelectedPage();
		assertTrue(selectedPage instanceof SeverityPreferencePage);
		SeverityPreferencePage page = (SeverityPreferencePage)selectedPage;
		assertEquals(WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidRoleRef_label, page.getFilterText());

		dialog.setBlockOnOpen(false);
		dialog.open();
		dialog.close();
	}

}
