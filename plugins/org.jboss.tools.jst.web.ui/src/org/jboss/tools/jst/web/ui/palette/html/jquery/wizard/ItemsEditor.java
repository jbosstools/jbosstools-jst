/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ItemsEditor implements SelectionListener, JQueryConstants {

	public static class ItemData {
		/**
		 * Maps editorID to editorValue for this item.
		 */
		Map<String, String> values = new HashMap<String, String>();

		public void setValue(String editorID, String value) {
			values.put(editorID, value);
		}

		public String getValue(String editorID) {
			return values.get(editorID);
		}
	}

	protected AbstractNewHTMLWidgetWizardPage page;
	protected TabFolder tab = null;
	protected Composite control = null;

	protected int selected = -1;
	protected int number = -1;

	protected boolean isSwitching = false;

	protected ItemData[] items = new ItemData[8];
	protected int minNumber = 0;
	protected int maxNumber = 8;

	public ItemsEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		this.page = page;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
		for (int i = 0; i < maxNumber; i++) {
			items[i] = new ItemData();
		}
	}

	public int getNumber() {
		return number;
	}

	public ItemData getSelected() {
		return selected < 0 ? null : items[selected];
	}

	public Composite createControl(Composite parent, String folderName) {
		Group panel = new Group(parent,SWT.BORDER);
		panel.setText(folderName);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		panel.setLayoutData(d);		
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		IFieldEditor number = JQueryFieldEditorFactory.createItemsNumberEditor(WizardMessages.numberOfItemsLabel, minNumber, maxNumber);
		page.addEditor(number, panel);

		tab = new TabFolder(panel, SWT.NULL);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		tab.setLayoutData(d);
		tab.addSelectionListener(this);

		control = new Composite(tab, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		control.setLayout(new GridLayout(3, false));

		createItemEditors();

		return panel;
	}

	/**
	 * Override to create field editors for an item.
	 * Register them as addItemEditor(editor)
	 * 
	 * @param control
	 */
	protected void createItemEditors() {
	}

	protected final void addItemEditor(IFieldEditor editor) {
		page.addEditor(editor, control);
	}

	public void setNumber(int n) {
		int sel = selected;
		if(number == n) {
			return;
		}
		if(n == 0 || number == 0) {
			setEditorEnablement(n > 0);
		}
		number = n;
		while(tab.getItemCount() > n && tab.getItemCount() > 1) {
			TabItem b = tab.getItem(tab.getItemCount() - 1);
			b.getParent().setSelection(b);
			b.setControl(null);
			b.dispose();
		}
		for (int i = tab.getItemCount(); i < n; i++) {
			TabItem b1 = new TabItem(tab, SWT.NULL);
			b1.setText("    " + (char)('1' + i) + "    ");
			b1.setControl(control);
		}
		if(sel >= n) sel = n - 1;
		if(selected != sel) {
			tab.setSelection(sel);
			setSelected(sel);
		}
	}

	public void setSelected(int n) {
		if(selected != n && n >= 0) {
			selected = n;
			isSwitching = true;
			fillEditorValues();
			isSwitching = false;
		}
	}

	/**
	 * When number of items is set to 0, b is false, otherwise it is true.
	 * 
	 * @param b
	 */
	protected void setEditorEnablement(boolean b) {
		ItemData s = items[0];
		for (String editorID: s.values.keySet()) {
			page.getEditor(editorID).setEnabled(b);
		}
	}

	/**
	 * When tab selection is changed, specific field editors should 
	 * get values from the selection.
	 */
	protected void fillEditorValues() {
		ItemData s = getSelected();
		for (String editorID: s.values.keySet()) {
			page.getEditor(editorID).setValue(s.getValue(editorID));
		}
	}

	/**
	 * Returns true if editor belongs to the set of editors.
	 * In this case new value is set to the selected item.	 * 
	 * 
	 * @param editorID
	 * @param value
	 * @return
	 */
	public boolean onPropertyChange(String editorID, String value) {
		if(EDITOR_ID_NUMBER_OF_ITEMS.equals(editorID)) {
			setNumber(Integer.parseInt(value));
			return true;
		} else if(getSelected() != null && getSelected().getValue(editorID) != null) {
			getSelected().setValue(editorID, value);
			return true;
		}
		return false;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		setSelected(tab.getSelectionIndex());
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

}
