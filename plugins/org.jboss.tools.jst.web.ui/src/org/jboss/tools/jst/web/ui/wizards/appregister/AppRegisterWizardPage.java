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
package org.jboss.tools.jst.web.ui.wizards.appregister;

import org.eclipse.jface.wizard.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.jboss.tools.jst.web.context.RegisterServerContext;
import org.jboss.tools.jst.web.ui.Messages;

public class AppRegisterWizardPage extends WizardPage {
	protected RegisterServerContext context;
	AppRegisterComponent appRegister = new AppRegisterComponent();
	
	public AppRegisterWizardPage(RegisterServerContext context) {
		super(Messages.AppRegisterWizardPage_PageName);
		this.context = context;
		appRegister.setContext(context);
		appRegister.setEnabling(false);
		appRegister.init();
		initListeners();
	}

	public void createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout());
		Control ch = appRegister.createControl(c);
		ch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		setControl(c);
		validate();
	}

	public void setVisible(boolean visible) {
		if (visible) {
			appRegister.loadApplicationName();
		}
		validate();
		super.setVisible(visible);
	}
	
	private void initListeners() {
		appRegister.addPropertyChangeListener(inputListener);
	}

	public void validate() {
		String message = appRegister.getErrorMessage();
		setPageComplete(message == null);
		setErrorMessage(message);
	}
	
	InputChangeListener inputListener = new InputChangeListener();
	
	class InputChangeListener implements java.beans.PropertyChangeListener {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			validate();
		}
	}
	
}
