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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewListWizardPage extends NewHTMLWidgetWizardPage {
	ListEditor items = new ListEditor(this, 1, 8) {
		@Override
		protected void setDefaultValues(int i) {
			setLabel(i, "Item " + (i + 1));
			setValue(i, "");
		}
		@Override
		protected void createItemEditors() {
			addItemEditor(HTMLFieldEditorFactory.createLabelEditor(""));
			addItemEditor(HTMLFieldEditorFactory.createListValueEditor());
		}
		@Override
		public void updateEnablement() {
			boolean valueEnabled = isTrue(HTMLFieldEditorFactory.EDITOR_ID_ORDERED);
			setEnabled(ATTR_VALUE, valueEnabled);
		}
	};

	public NewListWizardPage() {
		super("newList", WizardMessages.newListWizardTitle);
		setDescription(WizardMessages.newListWizardDescription);
		
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		addEditor(HTMLFieldEditorFactory.createOrderedListEditor(), parent);
		createIDEditor(parent, true);
		setEditorValue(EDITOR_ID_ADD_ID, FALSE);

		addEditor(HTMLFieldEditorFactory.createOrderedListStartEditor(), parent);
		addEditor(HTMLFieldEditorFactory.createOrderedListTypeEditor(), parent);
		addEditor(HTMLFieldEditorFactory.createOrderedListReversedEditor(), parent);
		
		items.createControl(parent, WizardMessages.itemsLabel);
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

		if(HTMLFieldEditorFactory.EDITOR_ID_ORDERED.equals(name)) {
			updateEnablement();
		}
		
		items.updateEnablement();

		super.propertyChange(evt);
	}

	protected void updateEnablement() {
		boolean valueEnabled = isTrue(HTMLFieldEditorFactory.EDITOR_ID_ORDERED);
		setEnabled(ATTR_TYPE, valueEnabled);
		setEnabled(ATTR_START, valueEnabled);
		setEnabled(ATTR_REVERSED, valueEnabled);
	}

	protected int getPreferredBrowser() {
		return SWT.WEBKIT;
	}
}
