/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.wizards.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.attribute.editor.IPropertyEditor;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.context.ImportWebWarContext;
import org.jboss.tools.jst.web.ui.wizards.appregister.AppRegisterComponent;

public class ImportWebWarWizardPage extends WizardPage {
	protected ImportWebWarContext context;
	private XAttributeSupport support;
	AppRegisterComponent appRegister = new AppRegisterComponent();
	private IModelPropertyEditorAdapter warLocationAdapter;
	private IModelPropertyEditorAdapter nameLocationAdapter;
	private IModelPropertyEditorAdapter useDefaultPathAdapter;
	private IModelPropertyEditorAdapter projectLocationAdapter;
	private IPropertyEditor projectLocationEditor;
	private Composite supportControl;
	Path defaultPath = (Path)ModelUIPlugin.getWorkspace().getRoot().getLocation();
	
	public ImportWebWarWizardPage(ImportWebWarContext context) {
		super("Wizard Page"); //$NON-NLS-1$
		this.context = context;
		XEntityData entityData = XEntityDataImpl.create(new String[][] {
			{"WebPrjAdoptWarStep0", ""}, //$NON-NLS-1$ //$NON-NLS-2$
			{"*.war location", "yes"}, //$NON-NLS-1$ //$NON-NLS-2$
			{"name", "yes"}, //$NON-NLS-1$ //$NON-NLS-2$
			{"use default path", "no"}, //$NON-NLS-1$ //$NON-NLS-2$
			{"location", "yes"}, //$NON-NLS-1$ //$NON-NLS-2$
//          WTP allows only classic structure
//			{"import type", "no"}
		});
		XAttributeData[] ad = entityData.getAttributeData();
		for (int i = 0; i < ad.length; i++) {
			ad[i].setValue(ad[i].getAttribute().getDefaultValue());
		}
		support = new XAttributeSupport(ModelUtilities.getPreferenceModel().getRoot(), entityData);
		support.setLayout(getLayoutForSupport());
		warLocationAdapter = support.getPropertyEditorAdapterByName("*.war location"); //$NON-NLS-1$
		nameLocationAdapter = support.getPropertyEditorAdapterByName("name"); //$NON-NLS-1$
		useDefaultPathAdapter = support.getPropertyEditorAdapterByName("use default path"); //$NON-NLS-1$
		projectLocationAdapter = support.getPropertyEditorAdapterByName("location"); //$NON-NLS-1$
		appRegister.setContext(context.getRegisterServerContext());
		appRegister.setEnabling(false);
		appRegister.init();
		initListeners();
	}

	private Layout getLayoutForSupport() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		return gridLayout;
	}

	private Layout getLayoutForAppRegister() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 4;
		gridLayout.horizontalSpacing = 16;
		gridLayout.verticalSpacing = 10;
		return gridLayout;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c.setLayout(new GridLayout());
		Control control = support.createControl(c);
		supportControl = (Composite)control;
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectLocationEditor = (IPropertyEditor)support.getPropertyEditorByName("location"); //$NON-NLS-1$
		updateLocationEditor();
		initListeners();
		createRegisterControl(c);
		setControl(c);
		setErrorMessage(null);
		setMessage(null);
		setPageComplete(context.getErrorMessage() == null);						
	}
	
	private boolean isDefaultPath() {
		return "true".equals(useDefaultPathAdapter.getStringValue(true)); //$NON-NLS-1$
	}
	
	private void updateLocationEditor() {
		if(projectLocationEditor == null) return;
		projectLocationEditor.getFieldEditor(supportControl).setEnabled(!isDefaultPath(), supportControl);
	}
	
	private void onWarLocationChanged() {
		if(lock) return;
		lock = true;
		try {
			context.setWarLocation(warLocationAdapter.getStringValue(true));
			appRegister.loadApplicationName();
			nameLocationAdapter.setValue(context.getProjectName());
			onUseDefaultChanged();
			setPageComplete(validatePage());
			getContainer().updateButtons();
		} finally {
			lock = false;
		}
	}
	
	private void onNameChanged() {
		if(lock) return;
		lock = true;
		try {
			context.setProjectName(nameLocationAdapter.getStringValue(true));
			appRegister.loadApplicationName();
			if(!isDefaultPath()) return;
			context.setCustomerLocation(getDefaultLocation());
			projectLocationAdapter.setValue(getDefaultLocation());
			setPageComplete(validatePage());
			getContainer().updateButtons();
		} finally {
			lock = false;
		}
	}
	
	private void onUseDefaultChanged() {
		updateLocationEditor();
		if(!isDefaultPath()) return;
		context.setCustomerLocation(getDefaultLocation());
		projectLocationAdapter.setValue(getDefaultLocation());
	}
	
	private String getDefaultLocation() {
		IProject p = context.getProjectHandle();
		if(p == null) {
			return defaultPath.toOSString();
		} else {
			return defaultPath.toOSString() + "/" + p.getName(); //$NON-NLS-1$
		}
	}

	public Control createRegisterControl(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		appRegister.setLayoutForSupport(getLayoutForAppRegister());
		Control ch = appRegister.createControl(parent);
		ch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return ch;
	}

	private boolean validatePage() {
		String message = context.getErrorMessage();
		setErrorMessage(message);
		setMessage(null);
		return message == null;
	}

	private void initListeners() {
		warLocationAdapter.addValueChangeListener(
			new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt)	{
					onWarLocationChanged();
				}
			}
		);
		nameLocationAdapter.addValueChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt)	{
						onNameChanged();
					}
				}
		);
		useDefaultPathAdapter.addValueChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt)	{
						onUseDefaultChanged();
					}
				}
		);
		appRegister.addPropertyChangeListener(inputListener);
	}

	boolean lock = false;
	InputChangeListener inputListener = new InputChangeListener();

	class InputChangeListener implements java.beans.PropertyChangeListener {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if(!lock) {
				appRegister.commit();
			}
			setPageComplete(validatePage());
			getContainer().updateButtons();
		}
	}

	public void commit() {
		boolean isC = true;
//      WTP allows only classic structure
//		boolean isC = support.getPropertyEditorAdapterByName("import type").getStringValue(true).indexOf("Eclipse") >= 0;
		context.setClassicEclipseProject(isC);
		context.setCustomerLocation(projectLocationAdapter.getStringValue(true));
		context.prepare();
	}

}
