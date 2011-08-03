/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.preferences;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.ui.preferences.SettingsPage;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditorFactory;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Alexey Kazakov
 */
public class KBSettingsPreferencePage extends SettingsPage {

	public static final String ID = "org.jboss.tools.jst.web.kb.propertyPages.KBSettingsPreferencePage"; //$NON-NLS-1$

	private IProject project;
	private boolean kbEnabled;
	private boolean initialState;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.PropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		project = (IProject) getElement().getAdapter(IProject.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);

		GridData gd = new GridData();

		gd.horizontalSpan = 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;

		GridLayout gridLayout = new GridLayout(1, false);
		root.setLayout(gridLayout);

		Composite generalGroup = new Composite(root, SWT.NONE);
		generalGroup.setLayoutData(gd);
		gridLayout = new GridLayout(4, false);

		generalGroup.setLayout(gridLayout);

		initialState = isKBEnabled(project);
		IFieldEditor kbSupportCheckBox = IFieldEditorFactory.INSTANCE.createCheckboxEditor(
				KbMessages.KB_SETTINGS_PREFERENCE_PAGE_KB_SUPPORT, KbMessages.KB_SETTINGS_PREFERENCE_PAGE_KB_SUPPORT, initialState);
		kbSupportCheckBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Object value = evt.getNewValue();
				if (value instanceof Boolean) {
					boolean v = ((Boolean) value).booleanValue();
					setEnabledKBSuport(v);
				}
			}
		});
		kbEnabled = isKBEnabled(project);
		registerEditor(kbSupportCheckBox, generalGroup);

		validate();
		return root;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		getEditor(KbMessages.KB_SETTINGS_PREFERENCE_PAGE_KB_SUPPORT).setValue(isKBEnabled(project));
		validate();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if(isKBEnabled()!=initialState) {
			if(isKBEnabled()) {
				addKBSupport(project);
			} else {
				removeKBSupport(project);
			}
		}
		return true;
	}

	private void addKBSupport(IProject project) {
		if(project==null) {
			return;
		}
		WebKbPlugin.enableKB(project, new NullProgressMonitor());
	}

	private void removeKBSupport(IProject project) {
		WebKbPlugin.disableKB(project);
	}

	private boolean isKBEnabled(IProject project) {
		try {
			return(project.isAccessible() && project.hasNature(IKbProject.NATURE_ID));
		} catch (CoreException e) {
			//ignore - all checks are done above
			return false;
		}
	}

	private boolean isKBEnabled() {
		return kbEnabled;
	}

	public void setEnabledKBSuport(boolean enabled) {
		kbEnabled = enabled;
		editorRegistry.get(KbMessages.KB_SETTINGS_PREFERENCE_PAGE_KB_SUPPORT).setValue(enabled);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.ui.preferences.SettingsPage#validate()
	 */
	@Override
	protected void validate() {
	}

    public void dispose() {
    	super.dispose();
    }
}