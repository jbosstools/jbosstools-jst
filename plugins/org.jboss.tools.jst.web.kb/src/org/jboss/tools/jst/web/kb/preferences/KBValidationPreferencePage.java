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
package org.jboss.tools.jst.web.kb.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.jboss.tools.common.ui.preferences.SeverityPreferencePage;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Viacheslav Kabanovich
 */
public class KBValidationPreferencePage extends SeverityPreferencePage {
	public static final String PREF_ID = "org.jboss.tools.jst.web.kb.preferences.KBValidationPreferencePage"; //$NON-NLS-1$
	public static final String PROP_ID = "org.jboss.tools.jst.web.kb.propertyPages.KBValidationPreferencePage"; //$NON-NLS-1$\

	public KBValidationPreferencePage() {
		setPreferenceStore(WebKbPlugin.getDefault().getPreferenceStore());
		setTitle(KBPreferencesMessages.KB_VALIDATOR_PREFERENCE_PAGE_KB_VALIDATOR);
	}

	@Override
	public void createControl(Composite parent) {
		IWorkbenchPreferenceContainer container = (IWorkbenchPreferenceContainer) getContainer();
		fConfigurationBlock = new KBValidationConfigurationBlock(getNewStatusChangedListener(), getProject(), container);

		super.createControl(parent);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected String getPreferencePageID() {
		// TODO Auto-generated method stub
		return PREF_ID;
	}

	@Override
	protected String getPropertyPageID() {
		return PROP_ID;
	}
}