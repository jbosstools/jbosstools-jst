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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewDatalistWizardPage extends NewHTMLWidgetWizardPage {
	ListEditor items = new ListEditor(this, 1, 8);

	public NewDatalistWizardPage() {
		super("newDatalist", WizardMessages.newDatalistwWizardTitle);
		setDescription(WizardMessages.newDatalistWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		createIDEditor(parent, false);

		items.createControl(parent, WizardMessages.itemsLabel);

		addEditor(HTMLFieldEditorFactory.createAddInputEditor(), parent);
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

}
