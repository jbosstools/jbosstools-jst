/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsdt.configuration.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.tools.jst.jsdt.JstJsdtMessages;
import org.jboss.tools.jst.jsdt.JstJsdtPlugin;
import org.jboss.tools.jst.jsdt.configuration.AngularJSProjectCapability;

public class AngularModuleConfigurationPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	public static final String PROP_ID = "org.jboss.tools.jst.jsdt.configuration.properties.AngularModuleConfigurationPropertyPage";

	IAdaptable element;
	IProject project;
	
	private Button button;
	private Button useButton;
	protected boolean doNotShow = false;
	private boolean defaultDoNotShow = false;
	protected boolean useAngular = false;
	private boolean defaultUseAngular = false;
	

	public AngularModuleConfigurationPropertyPage() {
		setTitle(JstJsdtMessages.AngularModuleConfigurationProperties_title);
	}


	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group = new Group(composite, SWT.NONE);
		group.setText(JstJsdtMessages.AngularModuleConfigurationProperties_desc);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		button = new Button(group, SWT.CHECK);
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				doNotShow = !doNotShow;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				doNotShow = !doNotShow;
			}
		});
		button.setText(JstJsdtMessages.DONT_SHOW_CHECKER_DIALOG);

		useButton = new Button(group, SWT.CHECK);
		useButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				useAngular = !useAngular;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				useAngular = !useAngular;
			}
		});
		useButton.setText(JstJsdtMessages.TURN_ANGULAR_MODULE_ON);

		initialize();
		Dialog.applyDialogFont(composite);

		return composite;
	}
	
	private void initialize() {
		IAdaptable element = getElement();
		project = (IProject)element.getAdapter(IProject.class);
		String value = null;
		try {
			value = project == null ? 
						null : 
						project.getPersistentProperty(AngularJSProjectCapability.DO_NOT_SHOW_ANGULAR_MODULE_WARNING);
		} catch (CoreException e) {
			JstJsdtPlugin.getDefault().logError(e);
		}
		doNotShow = "yes".equals(value);
		defaultDoNotShow = doNotShow;
		
		try {
			value = project == null ? 
						null : 
						project.getPersistentProperty(AngularJSProjectCapability.USE_ANGULAR_MODULE);
		} catch (CoreException e) {
			JstJsdtPlugin.getDefault().logError(e);
		}
		useAngular = "yes".equals(value);
		defaultUseAngular = useAngular;
		
		button.setSelection(defaultDoNotShow);
		useButton.setSelection(defaultUseAngular);
	}
	
	@Override
	public boolean performOk() {
		if (project != null) {
			try {
				project.setPersistentProperty(
						AngularJSProjectCapability.DO_NOT_SHOW_ANGULAR_MODULE_WARNING, 
						doNotShow ? "yes" : "no");
				
				project.setPersistentProperty(
						AngularJSProjectCapability.USE_ANGULAR_MODULE, 
						useAngular ? "yes" : "no");
			} catch (CoreException e) {
				JstJsdtPlugin.getDefault().logError(e);
			}
		}
		return super.performOk();
	}


	@Override
	protected void performApply() {
		performOk();
		super.performApply();
	}


	@Override
	protected void performDefaults() {
		button.setSelection(defaultDoNotShow);
		useButton.setSelection(defaultUseAngular);
		
		super.performDefaults();
	}
	
	
}