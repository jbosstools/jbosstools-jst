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

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ButtonsEditor implements SelectionListener, JQueryConstants {

	public static class ButtonData {
		String label = "";
		String url = "#";
		String icon = "";

		ButtonData(String label) {
			this.label = label;
		}
	}

	AbstractNewHTMLWidgetWizardPage page;
	TabFolder tab = null;
	Composite control = null;
	ButtonData[] buttons = new ButtonData[8];
	int selected = -1;
	int number = -1;

	boolean isSwitching = false;

	public ButtonsEditor(AbstractNewHTMLWidgetWizardPage page) {
		this.page = page;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new ButtonData("" + (char)(65 + i));
		}
	}

	public TabFolder createItemsFolder(Composite parent, String folderName) {
		Group panel = new Group(parent,SWT.BORDER);
		panel.setText(folderName);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		panel.setLayoutData(d);		
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		IFieldEditor number = page.getEditor(EDITOR_ID_NUMBER_OF_ITEMS);
		if(number != null) {
			page.addEditor(number, panel);
		}

		tab = new TabFolder(panel, SWT.NULL);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		tab.setLayoutData(d);
		tab.addSelectionListener(this);

		IFieldEditor arragement = page.getEditor(EDITOR_ID_ARRAGEMENT);
		if(arragement != null) {
			page.addEditor(arragement, panel);
		}

		IFieldEditor iconpos = page.getEditor(EDITOR_ID_ICON_POS);
		IFieldEditor icononly = page.getEditor(EDITOR_ID_ICON_ONLY);
		Composite left = panel;
		Composite right = panel;
		if(iconpos != null && icononly != null) {
			Composite[] cs = NewRangeSliderWizardPage.createTwoColumns(panel);
			GridLayout l = (GridLayout)cs[0].getLayout();
			l.marginBottom = 2;
			cs[0].setLayout(l);
			left = cs[0];
			right = cs[1];
		}
		if(iconpos != null) {
			page.addEditor(iconpos, left);
			page.expandCombo(iconpos);
		}

		if(icononly != null) {
			page.addEditor(icononly, right);
		}

		return tab;
	}

	public void setSelected(int n) {
		if(selected != n && n >= 0) {
			selected = n;
			isSwitching = true;
			page.getEditor(EDITOR_ID_LABEL).setValue(buttons[n].label);
			page.getEditor(EDITOR_ID_URL).setValue(buttons[n].url);
			page.getEditor(EDITOR_ID_ICON).setValue(buttons[n].icon);			
			isSwitching = false;
		}
	}

	public ButtonData getSelected() {
		return buttons[selected];
	}

	public void setNumber(int n) {
		int sel = selected;
		if(number == n) {
			return;
		}
		if(n == 0 || number == 0) {
			page.getEditor(EDITOR_ID_LABEL).setEnabled(n > 0);
			page.getEditor(EDITOR_ID_URL).setEnabled(n > 0);
			page.getEditor(EDITOR_ID_ICON).setEnabled(n > 0);
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

	public int getNumber() {
		return number;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		setSelected(tab.getSelectionIndex());
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	public boolean hasIcons() {
		for (int i = 0; i < getNumber(); i++) {
			if(buttons[i].icon.length() > 0) {
				return true;
			}
		}
		return false;
	}
}
