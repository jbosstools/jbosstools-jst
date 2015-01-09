/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor.ButtonPressedAction;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.ItemsEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class MenuitemsEditor extends ItemsEditor {
	static String MENUITEM_NODE_EVENT = "menuitemNode";

	static String EDITOR_ID_ADD_ITEM_ID = "add-item-id";
	static String EDITOR_ID_ITEM_ID = "item-id";

	static String[] PROPERTIES = {
		ATTR_TYPE, ATTR_LABEL, ATTR_ICON, ATTR_DEFAULT, ATTR_DISABLED, CHECKED, ATTR_RADIOGROUP,
		EDITOR_ID_ADD_ITEM_ID, EDITOR_ID_ITEM_ID
	};
	
	static String[] DIALOG_PROPERTIES = {
		ATTR_TYPE, ATTR_LABEL, ATTR_ICON, ATTR_DEFAULT, ATTR_DISABLED, CHECKED, ATTR_RADIOGROUP,
		HTMLConstants.EDITOR_ID_ADD_ID, HTMLConstants.EDITOR_ID_ID
	};

	Button editSelectedMenuItem = null;
	String[] itemIDs = new String[8];

	public MenuitemsEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < items.length; i++) {
			items[i].setValue(ATTR_LABEL, "Item " + i);
			items[i].setValue(ATTR_TYPE, MENUITEM_TYPE_COMMAND);
			items[i].setValue(EDITOR_ID_ADD_ITEM_ID, FALSE);
			items[i].setValue(EDITOR_ID_ITEM_ID, "");
		}
	}

	@Override
	protected void createItemEditors() {
		addItemEditor(HTMLFieldEditorFactory.createLabelEditor(WizardDescriptions.menuitemLabel));
		IFieldEditor editMenuitem = HTMLFieldEditorFactory.createEditSelectedMenuitemEditor(new EditMenuitemAction());
		addItemEditor(editMenuitem);
		if(control != null) {
			for (Object o: editMenuitem.getEditorControls()) {
				if(o instanceof Button) {
					editSelectedMenuItem = (Button)o;
				}
			}
		}
	}

	public void setMenuitemProperty(int index, String name, String value) {
		items[index].setValue(name, value);
	}

	public String getValue(int i, String name) {
		return items[i].getValue(name);
	}

	public boolean onPropertyChange(String editorID, String value) {
		boolean b = super.onPropertyChange(editorID, value);
		if(editorID.equals(MENUITEM_NODE_EVENT) && value.equals("" + selected)) {
			page.getEditor(ATTR_LABEL).setValue(getValue(selected, ATTR_LABEL));
		}
		return b;
	}

	class EditMenuitemAction extends ButtonPressedAction {

		public EditMenuitemAction() {
			super(WizardMessages.editLabel);
		}

		@Override
		public void run() {
			if(selected >= 0) {
				editMenuitem(selected, page.getControl() != null);
			}
		}
	}

	/**
	 * Call to sub-wizard editing i-th menuitem element.
	 * If showDialog is false wizard generates element with initial values.
	 * 
	 * @param showDialog
	 */
	public void editMenuitem(int i, boolean showDialog) {
		new NewMenuitemWizardEx(page.getWizard().getPaletteItem(), i, showDialog);
	}

	class NewMenuitemWizardEx extends NewMenuitemWizard {
		int index;

		public NewMenuitemWizardEx(IPaletteItem item, int index, boolean showDialog) {
			this.index = index;
			JSPMultiPageEditor e = (JSPMultiPageEditor)WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			setPaletteItem(item);
			setCommand(createDropCommand(e.getJspEditor()));
			WizardDialog d = new WizardDialog(NewMenuitemWizardEx.this.getShell(), this);
			d.create();
			loadValues();
			if(showDialog) {
				d.open();
			} else {
				doPerformFinish();
			}
			dispose();
		}

		@Override
		protected void doPerformFinish() {
			saveValues();
			MenuitemsEditor.this.page.propertyChange(new PropertyChangeEvent(editSelectedMenuItem, MENUITEM_NODE_EVENT, "", "" + index));
		}
	
		protected void saveValues() {
			for (int i = 0; i < PROPERTIES.length; i++) {
				setMenuitemProperty(index, PROPERTIES[i], page.getEditorValue(DIALOG_PROPERTIES[i]));
			}
			itemIDs[index] = getID(NewMenuitemWizard.prefix);
		}
	
		protected void loadValues() {
			for (int i = 0; i < PROPERTIES.length; i++) {
				String value = getValue(index, PROPERTIES[i]);
				if(value != null) {
					page.setEditorValue(DIALOG_PROPERTIES[i], value);
				}
			}
			for (int i = 0; i < items.length; i++) {
				if(i != index && itemIDs[i] != null) {
					ids.add(itemIDs[i]);
				}
			}
			ids.add(((NewMenuWizard)MenuitemsEditor.this.page.getWizard()).getMenuID());
		}

	}

}
