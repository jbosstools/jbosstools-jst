/******************************************************************************* 
 * Copyright (c) 2007-2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.jboss.tools.jst.web.ui.internal.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.el.core.ELCorePlugin;
import org.jboss.tools.common.el.core.ca.preferences.ELContentAssistPreferences;

/**
 * Preference page for EL Content Assistant preferences
 * 
 * @author jeremy
 *
 */
public class ContentAssistPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private IWorkbench workbench;
	private Button buttonGettersAndSetters;
	private Button buttonMethodsView;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		IPreferenceStore store = ELCorePlugin.getDefault().getPreferenceStore();

		buttonGettersAndSetters = new Button(composite,SWT.CHECK);
		buttonGettersAndSetters.setText(Messages.ContentAssistPreferencePage_showGettersAndSetters);
		buttonGettersAndSetters.setSelection(store.getBoolean(ELContentAssistPreferences.SHOW_GETTERS_AND_SETTERS));

		buttonMethodsView = new Button(composite,SWT.CHECK);
		buttonMethodsView.setText(Messages.ContentAssistPreferencePage_showMethodsWithParenthesesOnly);
		buttonMethodsView.setSelection(store.getBoolean(ELContentAssistPreferences.SHOW_METHODS_WITH_PARENTHESES_ONLY));

		return composite;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	@Override
	protected void performDefaults() {
		IPreferenceStore store = ELCorePlugin.getDefault().getPreferenceStore();

		boolean defaultValue = store.getDefaultBoolean(ELContentAssistPreferences.SHOW_GETTERS_AND_SETTERS);
		buttonGettersAndSetters.setSelection(defaultValue);
		store.setValue(ELContentAssistPreferences.SHOW_GETTERS_AND_SETTERS, defaultValue);
		
		defaultValue = store.getDefaultBoolean(ELContentAssistPreferences.SHOW_METHODS_WITH_PARENTHESES_ONLY);
		buttonMethodsView.setSelection(defaultValue);
		store.setValue(ELContentAssistPreferences.SHOW_METHODS_WITH_PARENTHESES_ONLY, defaultValue);

		if (store instanceof IPersistentPreferenceStore) {
			try {
				((IPersistentPreferenceStore)store).save();
			} catch (IOException e) {
				ELCorePlugin.getPluginLog().logError(e);
			}
		}

		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		IPreferenceStore store = ELCorePlugin.getDefault().getPreferenceStore();
		store.setValue(ELContentAssistPreferences.SHOW_GETTERS_AND_SETTERS, 
						buttonGettersAndSetters.getSelection());
		store.setValue(ELContentAssistPreferences.SHOW_METHODS_WITH_PARENTHESES_ONLY, buttonMethodsView.getSelection());

		if (store instanceof IPersistentPreferenceStore) {
			try {
				((IPersistentPreferenceStore)store).save();
			} catch (IOException e) {
				ELCorePlugin.getPluginLog().logError(e);
			}
		}

		return super.performOk();
	}

}
