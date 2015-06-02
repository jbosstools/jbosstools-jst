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
package org.jboss.tools.jst.js.bower.internal.preferences;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.Messages;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PAGE_ID = "org.jboss.tools.jst.js.bower.preferences.BowerPreferencesPage"; //$NON-NLS-1$
	private NpmHomeFieldEditor bowerEditor;
	
	public BowerPreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(BowerPreferenceHolder.getStore());
	}
	
	@Override
	protected void createFieldEditors() {
		bowerEditor = new NpmHomeFieldEditor(BowerPreferenceHolder.PREF_NPM_LOCATION, Messages.BowerPreferencePage_LocationLabel, getFieldEditorParent());
		addField(bowerEditor);
	}
		
	private static class NpmHomeFieldEditor extends DirectoryFieldEditor {
		
		public NpmHomeFieldEditor(String name, String label, Composite composite) {
			super(name, label, composite);
			setEmptyStringAllowed(true);
		}
	
		
		@Override
		protected boolean doCheckState() {
			String filename = getTextControl().getText();
			filename = filename.trim();
			if (filename.isEmpty()) {
				this.getPage().setMessage(Messages.BowerPreferencePage_NotSpecifiedWarning, IStatus.WARNING); 
				return true;
			} else {
				// clear the warning message
				this.getPage().setMessage(null);
			}

			if (!filename.endsWith(File.separator)) {
				filename = filename + File.separator;
			}
			
			File selectedFile = new File(filename);
			File nodeModules = new File(selectedFile, BowerConstants.NODE_MODULES);
			if (nodeModules == null || !nodeModules.exists()) {
				setErrorMessage(Messages.BowerPreferencePage_NotValidError); 
				return false;
			}
			
			File bower = new File(nodeModules, BowerConstants.BOWER);
			if (bower == null || !bower.exists()) {
				setErrorMessage(Messages.BowerPreferencePage_NotInstalledError);
				return false;
			}
			return true;
		}
		
		@Override
		public void setValidateStrategy(int value) {
			super.setValidateStrategy(VALIDATE_ON_KEY_STROKE);
		}
		
	}

}
