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
public class PopUpKeyValueDialog extends Dialog {
	private final String title;
	private final String initialKey;
	private final String initialValue;
	private final VerifyListener verifyListener;
	protected Text keyText;
	protected Text valueText;
	private String key;
	private String value;
	private String keyLabelText;
	private String valueLabelText;

	public PopUpKeyValueDialog(Shell shell, String title, String initialKey, String initialValue, String keyLabelText,
			String valueLabelText, VerifyListener verifyListener) {
		super(shell);
		this.title = title;
		this.initialKey = initialKey;
		this.initialValue = initialValue;
		this.keyLabelText = keyLabelText;
		this.valueLabelText = valueLabelText;
		this.verifyListener = verifyListener;
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 7;
		gridLayout.marginWidth = 12;
		comp.setLayout(gridLayout);

		Label nameLabel = new Label(comp, SWT.NONE);
		nameLabel.setText(keyLabelText);
		nameLabel.setFont(comp.getFont());

		keyText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		keyText.setLayoutData(gd);
		keyText.setFont(comp.getFont());
		keyText.setText(initialKey == null ? "" : initialKey); //$NON-NLS-1$
		keyText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		Label valueLabel = new Label(comp, SWT.NONE);
		valueLabel.setText(valueLabelText);
		valueLabel.setFont(comp.getFont());

		valueText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
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

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			key = keyText.getText();
			value = valueText.getText();
		} else {
			key = null;
			value = null;
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
		String name = keyText.getText().trim();
		String value = valueText.getText().trim();
		// verify name
		Event e = new Event();
		e.widget = keyText;
		VerifyEvent ev = new VerifyEvent(e);
		ev.doit = true;
		if (verifyListener != null) {
			ev.text = name;
			verifyListener.verifyText(ev);
		}
		getButton(IDialogConstants.OK_ID).setEnabled((name.length() > 0) && (value.length() > 0) && ev.doit);
	}

	public void create() {
		super.create();
		updateButtons();
	}
}