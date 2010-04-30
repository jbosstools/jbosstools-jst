/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.pref.template;

import org.eclipse.wst.html.ui.internal.preferences.ui.HTMLTemplatePreferencePage;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * @author mareshkau
 *
 */
public class VPETemplatePreferencePage extends HTMLTemplatePreferencePage {

	public VPETemplatePreferencePage() {
		WebUiPlugin webUiPlugin = WebUiPlugin.getDefault();
		setPreferenceStore(webUiPlugin.getPreferenceStore());
		setTemplateStore(webUiPlugin.getTemplateStore());
		setContextTypeRegistry(webUiPlugin.getTemplateContextRegistry());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
  	  boolean ok = super.performOk();
  	  WebUiPlugin.getDefault().savePluginPreferences();
	  return ok;
	}

}
