/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.npm.internal.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jst.js.npm.internal.Messages;
import org.jboss.tools.jst.js.npm.internal.preference.editor.NpmHomeFieldEditor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PAGE_ID = "org.jboss.tools.jst.js.npm.preferences.NpmPreferencesPage"; //$NON-NLS-1$

	public NpmPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(NpmPreferenceHolder.getStore());
	}

	@Override
	protected void createFieldEditors() {
		NpmHomeFieldEditor npmHomeEditor = new NpmHomeFieldEditor(NpmPreferenceHolder.PREF_NPM_LOCATION,
				Messages.NpmPreferencePage_NpmLocationLabel, getFieldEditorParent());
		addField(npmHomeEditor);
	}
	
}
