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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor.ButtonPressedAction;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewDatalistWizardPage extends NewHTMLWidgetWizardPage {
	static String TEXT_INPUT_NODE_EVENT = "textInputNode";

	ListEditor items = new ListEditor(this, 1, 8);

	ElementNode textInputNode = null;
	Button editTextInputButton = null;
	Map<String, String> textInputValues = new HashMap<String, String>();

	public NewDatalistWizardPage() {
		super("newDatalist", WizardMessages.newDatalistwWizardTitle);
		setDescription(WizardMessages.newDatalistWizardDescription);
		textInputValues.put(JQueryConstants.EDITOR_ID_LABEL, "");
		textInputValues.put(JQueryConstants.EDITOR_ID_PLACEHOLDER, "");
	}

	/**
	 * Set initial values for sub-wizard editing text input element.
	 * 
	 * @param name
	 * @param value
	 */
	public void setTextInputProperty(String name, String value) {
		textInputValues.put(name, value);
	}

	/**
	 * Call to sub-wizard editing text input element.
	 * If showDialog is false wizard generates element with initial values.
	 * 
	 * @param showDialog
	 */
	public void editTextInput(boolean showDialog) {
		new NewTextInputWizardEx(getWizard().getPaletteItem(), showDialog);
	}

	/**
	 * Returns result of sub-wizard editing text input element.
	 * @return
	 */
	ElementNode getTextInputNode() {
		return textInputNode;
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		createIDEditor(parent, false);

		items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor addInput = HTMLFieldEditorFactory.createAddInputEditor(new EditTextInputAction());
		addEditor(addInput, parent);
		if(parent != null) {
			for (Object o: addInput.getEditorControls()) {
				if(o instanceof Button) {
					editTextInputButton = (Button)o;
				}
			}
		}

		updateTextInputButtonEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(items.onPropertyChange(name, value)) {
		}
		if(EDITOR_ID_ID.equals(name) && textInputNode != null && isTrue(TAG_INPUT)) {
			setTextInputProperty(ATTR_LIST, getEditorValue(EDITOR_ID_ID));
			editTextInput(false);
		}
		if(TAG_INPUT.equals(name)) {
			if(!isTrue(TAG_INPUT)) {
				textInputNode = null;
			}
			updateTextInputButtonEnablement();
		}
		
		items.updateEnablement();

		super.propertyChange(evt);
	}

	void updateTextInputButtonEnablement() {
		if(editTextInputButton != null && !editTextInputButton.isDisposed()) {
			editTextInputButton.setEnabled(isTrue(TAG_INPUT));
		}		
	}

	class EditTextInputAction extends ButtonPressedAction {

		public EditTextInputAction() {
			super(WizardMessages.editLabel);
		}

		@Override
		public void run() {
			editTextInput(getControl() != null);
		}
	}

	class NewTextInputWizardEx extends NewTextInputWizard {
		NewTextInputWizardEx(IPaletteItem item, boolean showDialog) {
			JSPMultiPageEditor e = (JSPMultiPageEditor)WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			setPaletteItem(item);
			setCommand(createDropCommand(e.getJspEditor()));
			WizardDialog d = new WizardDialog(NewDatalistWizardPage.this.getShell(), this);
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
			ElementNode root = createRoot();
			addContent(root);
			textInputNode = (ElementNode)root;
			NewDatalistWizardPage.this.propertyChange(new PropertyChangeEvent(editTextInputButton, TEXT_INPUT_NODE_EVENT, "", ""));
		}
	
		protected void saveValues() {
			textInputValues.clear();
			for (String key: NewTextInputWizardPage.PROPERTIES) {
				setTextInputProperty(key, page.getEditorValue(key));
			}
		}
	
		protected void loadValues() {
			if(getControl() != null) {
				page.setListEnabled(false);
			}
			for (Map.Entry<String, String> e: textInputValues.entrySet()) {
				page.setEditorValue(e.getKey(), e.getValue());
			}
			page.setEditorValue(ATTR_LIST, ((NewDatalistWizard)NewDatalistWizardPage.this.getWizard()).getDatalistID());
		}

	}
}
