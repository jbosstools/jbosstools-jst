/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.preferences;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.preferences.ELSearchPreferences;

/**
 * Preference page for EL Search preferences
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ELSearchPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private IWorkbench workbench;
	IFieldEditor timeLimit;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		timeLimit = SwtFieldEditorFactory.INSTANCE.createTextEditor("timeLimit", //$NON-NLS-1$
				Messages.ELSearchPreferencePage_searchTimeLimit, 
				getPreferenceStore().getString(ELSearchPreferences.EL_SEARCH_TIME_LIMIT));
		timeLimit.doFillIntoGrid(composite);

		timeLimit.addPropertyChangeListener(new PropertyChangeListener() {			
			public void propertyChange(PropertyChangeEvent evt) {
				validate();
			}
		});
		validate();

		return composite;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	protected void validate() {
		if(timeLimit == null) {
			return;
		}
		String error = null;
		String value = timeLimit.getValueAsString();
		try {
			int v = Integer.parseInt(value);
			if(v < 0) {
				error = Messages.ELSearchPreferencePage_searchTimeLimit_invalid;
			}
		} catch (NumberFormatException e) {
			error = Messages.ELSearchPreferencePage_searchTimeLimit_invalid;
		}
		setErrorMessage(error);
		setValid(error == null);
	}

	@Override
	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();

		String defaultValue = store.getDefaultString(ELSearchPreferences.EL_SEARCH_TIME_LIMIT);
		timeLimit.setValue(defaultValue);
		store.setValue(ELSearchPreferences.EL_SEARCH_TIME_LIMIT, defaultValue);
		
		if (store instanceof IPersistentPreferenceStore) {
			try {
				((IPersistentPreferenceStore)store).save();
			} catch (IOException e) {
				WebKbPlugin.getDefault().logError(e);
			}
		}

		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		if(!isValid()) {
			return false;
		}
		IPreferenceStore store = getPreferenceStore();
		store.setValue(ELSearchPreferences.EL_SEARCH_TIME_LIMIT, timeLimit.getValueAsString());

		if (store instanceof IPersistentPreferenceStore) {
			try {
				((IPersistentPreferenceStore)store).save();
			} catch (IOException e) {
				WebKbPlugin.getDefault().logError(e);
			}
		}

		return super.performOk();
	}

	protected IPreferenceStore doGetPreferenceStore() {
		return WebKbPlugin.getDefault().getPreferenceStore();
	}

}
