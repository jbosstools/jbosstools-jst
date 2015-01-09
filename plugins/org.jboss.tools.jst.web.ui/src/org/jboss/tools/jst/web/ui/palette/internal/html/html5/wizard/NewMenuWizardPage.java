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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewMenuWizardPage extends NewHTMLWidgetWizardPage {
	MenuitemsEditor items = new MenuitemsEditor(this, 1, 8);

	public NewMenuWizardPage() {
		super("newMenu", WizardMessages.newMenuWizardTitle);
		setDescription(WizardMessages.newMenuWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = HTMLFieldEditorFactory.createLabelEditor(HTMLFieldEditorFactory.EDITOR_ID_MENU_LABEL, WizardDescriptions.menuLabel);
		label.setValue("My menu");
		addEditor(label, parent);
		createIDEditor(parent, true);
		addEditor(HTMLFieldEditorFactory.createMenuTypeEditor(), parent);

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
		
		items.updateEnablement();

		super.propertyChange(evt);
	}

	public void setMenuitemProperty(int index, String name, String value) {
		items.setMenuitemProperty(index, name, value);
	}

	public void editMenuItem(int index, boolean showDialog) {
		items.editMenuitem(index, showDialog);
	}

}
