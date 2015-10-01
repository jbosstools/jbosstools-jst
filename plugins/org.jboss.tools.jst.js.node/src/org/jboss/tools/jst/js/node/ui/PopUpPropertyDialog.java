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
package org.jboss.tools.jst.js.node.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class PopUpPropertyDialog extends Dialog {

	private final String title;
	private final String initialValue;
	private final VerifyListener verifyListener;
	private String valueLabel;
	private Text valueText;
	private String name;

	public PopUpPropertyDialog(Shell shell, String title, String valueLabel, String initialValue,
			VerifyListener verifyListener) {
		super(shell);
		this.title = title;
		this.valueLabel = valueLabel;
		this.initialValue = initialValue;
		this.verifyListener = verifyListener;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 7;
		gridLayout.marginWidth = 12;
		comp.setLayout(gridLayout);

		Label label = new Label(comp, SWT.NONE);
		label.setText(valueLabel);
		label.setFont(comp.getFont());

		valueText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		valueText.setLayoutData(gd);
		valueText.setFont(comp.getFont());
		valueText.setText(initialValue == null ? "" : initialValue); //$NON-NLS-1$
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		return comp;
	}

	public String getName() {
		return this.name;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			name = valueText.getText();
		} else {
			name = null;
		}
		super.buttonPressed(buttonId);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	protected void updateButtons() {
		String name = valueText.getText().trim();
		Event e = new Event();
		e.widget = valueText;
		VerifyEvent ev = new VerifyEvent(e);
		ev.doit = true;
		if (verifyListener != null) {
			ev.text = name;
			verifyListener.verifyText(ev);
		}
		getButton(IDialogConstants.OK_ID).setEnabled((name.length() > 0));
	}

	public void create() {
		super.create();
		updateButtons();
	}
	
}