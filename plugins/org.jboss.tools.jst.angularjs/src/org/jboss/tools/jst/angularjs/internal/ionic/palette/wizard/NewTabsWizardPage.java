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
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabsWizardPage extends NewIonicWidgetWizardPage {
	TabEditor tabs = new TabEditor(this, 1, 6);

	public NewTabsWizardPage() {
		super("newTabs", WizardMessages.newTabsTitle);
		setDescription(IonicWizardMessages.newTabsWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);
		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		IFieldEditor color = IonicFieldEditorFactory.createTabsColorEditor();
		addEditor(color, parent);

		IFieldEditor iconPos = IonicFieldEditorFactory.createTabsIconPositionEditor();
		addEditor(iconPos, parent);

		IFieldEditor hideTabbar = IonicFieldEditorFactory.createHideTabsEditor();
		addEditor(hideTabbar, parent);

		tabs.createControl(parent, WizardMessages.itemsLabel);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(tabs.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(tabs.onPropertyChange(name, value)) {
			//
		}
		//TODO
		super.propertyChange(evt);
	}

}
