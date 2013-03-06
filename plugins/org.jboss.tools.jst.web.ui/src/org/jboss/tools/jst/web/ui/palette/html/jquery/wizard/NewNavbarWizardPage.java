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

import java.beans.PropertyChangeEvent;

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
public class NewNavbarWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	Buttons buttons = new Buttons();

	public NewNavbarWizardPage() {
		super("newNavbar", WizardMessages.newNavbarWizardTitle);
		setDescription(WizardMessages.newNavbarWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor number = JQueryFieldEditorFactory.createItemsNumberEditor();
		addEditor(number, parent);

		TabFolder tab = createItemsFolder(parent);
		buttons.tab = tab;
		
		Composite p1 = new Composite(tab, SWT.NONE);
		buttons.control = p1;
		p1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(3, false);
		p1.setLayout(layout);

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		addEditor(label, p1);

		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addEditor(url, p1);

		IFieldEditor icon = JQueryFieldEditorFactory.createIconEditor();
		addEditor(icon, p1);

		getEditor(EDITOR_ID_NUMBER_OF_ITEMS).setValue("3");

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent);
		expandCombo(iconpos);
		iconpos.setEnabled(false);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent);
		expandCombo(theme);
	}

	private TabFolder createItemsFolder(Composite parent) {
		Group panel = new Group(parent,SWT.BORDER);
		panel.setText("Items");
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		panel.setLayoutData(d);		
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		TabFolder folder = new TabFolder(panel, SWT.NULL);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		folder.setLayoutData(d);
		folder.addSelectionListener(buttons);
		return folder;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(EDITOR_ID_LABEL.equals(name)) {
			buttons.getSelected().label = value;
		} else if(EDITOR_ID_URL.equals(name)) {
			buttons.getSelected().url = value;
		} else if(EDITOR_ID_ICON.equals(name)) {
			buttons.getSelected().icon = value;
			boolean hasIcons = false;
			for (int i = 0; i < buttons.getNumber(); i++) {
				if(buttons.buttons[i].icon.length() > 0) hasIcons = true;
			}
			getEditor(EDITOR_ID_ICON_POS).setEnabled(hasIcons);
		} else if(EDITOR_ID_NUMBER_OF_ITEMS.equals(name)) {
			buttons.setNumber(Integer.parseInt(value));
			boolean hasIcons = false;
			for (int i = 0; i < buttons.getNumber(); i++) {
				if(buttons.buttons[i].icon.length() > 0) hasIcons = true;
			}
			if(getEditor(EDITOR_ID_ICON_POS) != null) {
				getEditor(EDITOR_ID_ICON_POS).setEnabled(hasIcons);
			}
		}
		super.propertyChange(evt);
	}

	protected int getAdditionalHeight() {
		return 100;
	}

	class Buttons implements SelectionListener {
		TabFolder tab = null;
		Composite control = null;
		ButtonData[] buttons = new ButtonData[8];
		int selected = -1;
		int number = -1;

		boolean isSwitching = false;

		public Buttons() {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new ButtonData("" + (char)(65 + i));
			}
		}
	
		public void setSelected(int n) {
			if(selected != n) {
				selected = n;
				isSwitching = true;
				getEditor(EDITOR_ID_LABEL).setValue(buttons[n].label);
				getEditor(EDITOR_ID_URL).setValue(buttons[n].url);
				getEditor(EDITOR_ID_ICON).setValue(buttons[n].icon);			
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
			number = n;
			while(tab.getItemCount() > n) {
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
	}

	static class ButtonData {
		String label = "";
		String url = "#";
		String icon = "";

		ButtonData(String label) {
			this.label = label;
		}
	}

}
