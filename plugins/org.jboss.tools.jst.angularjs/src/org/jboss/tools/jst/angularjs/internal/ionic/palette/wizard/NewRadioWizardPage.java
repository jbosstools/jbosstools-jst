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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewRadioWizardPage extends NewIonicWidgetWizardPage {
	RadioEditor buttons = new RadioEditor(this, 1, 8);

	public NewRadioWizardPage() {
		super("newRadio", WizardMessages.newRadioTitle);
		setDescription(IonicWizardMessages.newRadioWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		addEditor(IonicFieldEditorFactory.createNgModelEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createNameEditor(), parent);

		buttons.createControl(parent, WizardMessages.itemsLabel);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(buttons.onPropertyChange(name, value)) {
			if(JQueryConstants.EDITOR_ID_CHECKED.equals(name)) {
				buttons.onCheckedModified();
			}
		}

		super.propertyChange(evt);
	}

}
