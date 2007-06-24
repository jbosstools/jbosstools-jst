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
package org.jboss.tools.jst.web.ui.wizards.palette;

import java.util.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class ParentGroupComponent {
	Composite composite;
	Button addToExisting;
	CCombo existingGroup;
	Button createNewGroup;
	Text newGroup;	
	ImportTLDPage listener;	
	String[] items = new String[0];	
	String initialItem = null;
	boolean isAdjusting = false;
	
	public void setListener(ImportTLDPage listener) {
		this.listener = listener;
	}
	
	public void setItems(String[] items) {
		this.items = items;
		if(existingGroup != null && !existingGroup.isDisposed()) {
			existingGroup.setItems(items);
		}
	}
	
	public void setInitialItem(String item) {
		initialItem = item;
		if(initialItem != null && existingGroup != null && !existingGroup.isDisposed()) {
			existingGroup.setText(item);
		}
	}
	
	public Control createControl(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;		
		group.setLayout(layout);
		Composite c = new Composite(group, SWT.NONE);
		layout = new GridLayout(2, false);
		c.setLayout(layout);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addToExisting = new Button(c, SWT.RADIO);
		GridData gd = new GridData();
		addToExisting.setText(WebUIMessages.ADD_TO_EXISTING_GROUP);
		addToExisting.setLayoutData(gd);
		addToExisting.setSelection(true);
		addToExisting.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				isAdjusting = true;
				createNewGroup.setSelection(!addToExisting.getSelection());
				isAdjusting = false;
				fire();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		existingGroup = new CCombo(c, SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		existingGroup.setLayoutData(gd);
		existingGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		existingGroup.setItems(items);
		if(initialItem != null) existingGroup.setText(initialItem);
		existingGroup.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fire();
			}
		});

		createNewGroup = new Button(c, SWT.RADIO);
		createNewGroup.setText(WebUIMessages.CREATE_NEW_GROUP);
		gd = new GridData();
		createNewGroup.setLayoutData(gd);
		createNewGroup.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				isAdjusting = true;
				addToExisting.setSelection(!createNewGroup.getSelection());
				isAdjusting = false;
				fire();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		newGroup = new Text(c, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		newGroup.setLayoutData(gd);
		newGroup.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fire();
			}
		});
		composite = group;
		return composite;
	}
	
	protected void fire() {
		if(!isAdjusting && listener != null) listener.propertyChange(null);
	}
	
	public void updateFieldEnablement() {
		if(addToExisting == null || addToExisting.isDisposed()) return;
		boolean existing = addToExisting.getSelection();
		newGroup.setEnabled(!existing);
		existingGroup.setEnabled(existing);
	}
	
	public void store(Properties p) {
		p.setProperty("parent group", getValue()); //$NON-NLS-1$
	}
	
	public String getValue() {
		if(addToExisting == null || addToExisting.isDisposed()) return ""; //$NON-NLS-1$
		boolean existing = addToExisting.getSelection();
		String s = (existing) ? existingGroup.getText() : newGroup.getText();
		return (s == null) ? "" : s; //$NON-NLS-1$
	}

}
