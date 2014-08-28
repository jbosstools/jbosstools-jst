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
package org.jboss.tools.jst.jsdt.configuration;

import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.jsdt.JstJsdtMessages;

public class AngularModuleInfoDialog extends MessageDialog {

	private Button button;
	protected boolean doNotShowAgain = false;
	protected IProject project;

	protected AngularModuleInfoDialog(IProject project) {
		super(getActiveShell(),
				JstJsdtMessages.MISSING_ANGULAR_MODULE_MESSAGE_TITLE, null, "", INFORMATION, //$NON-NLS-1$
				new String[] { JstJsdtMessages.TURN_ANGULAR_MODULE_ON, JstJsdtMessages.SKIP_BUTTON_LABEL }, 0);
		this.project = project;
		message = getMessageInfo();
	}
	
	static Shell getActiveShell() {
		Display display = PlatformUI.getWorkbench().getDisplay();
		return display.getActiveShell();
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		parent.setLayout(gridLayout);
		button = new Button(parent, SWT.CHECK);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doNotShowAgain = !doNotShowAgain;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				doNotShowAgain = !doNotShowAgain;
			}
		});
		button.setText(JstJsdtMessages.bind(JstJsdtMessages.DONT_SHOW_CHECKER_DIALOG, project.getName()));
		return parent;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if (buttonId == 0) {
			fixButtonPressed();
		} else if(buttonId == 1) {
			// Skip button pressed
			saveDoNotShowAgain();
		}
	}

	private void fixButtonPressed() {
		AngularJSProjectCapability.setUseAngularModuleProjectPreference(project, true);
		saveDoNotShowAgain();
	}

	private void saveDoNotShowAgain() {
		AngularJSProjectCapability.setShowAngularModuleWarningProjectPreference(project, !doNotShowAgain);
	}

	protected String getMessageInfo() {
		String dialogMessage = MessageFormat.format(
				JstJsdtMessages.TURN_ANGULAR_MODULE_ON_TEXT, project.getName());
		return dialogMessage;
	}
}