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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewMenuitemWizardPage extends NewHTMLWidgetWizardPage {

	public NewMenuitemWizardPage() {
		super("newMenuitem", WizardMessages.newMenuitemWizardTitle);
		setDescription(WizardMessages.newMenuitemWizardDescription);
		
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = HTMLFieldEditorFactory.createLabelEditor(WizardDescriptions.menuitemLabel);
		label.setValue("My item");
		addEditor(label, parent);
		createIDEditor(parent, true);
		IFieldEditor type = HTMLFieldEditorFactory.createMenuitemTypeEditor();
		addEditor(type, parent);
		addEditor(HTMLFieldEditorFactory.createIconEditor(getWizard().getFile()), parent);
		addEditor(HTMLFieldEditorFactory.createDefaultEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createCheckedEditor(WizardDescriptions.radioIsSelected), parent);
		addEditor(JQueryFieldEditorFactory.createDisabledEditor(), parent);
		addEditor(HTMLFieldEditorFactory.createRadiogroupEditor(), parent);

		updateEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(ATTR_TYPE.equals(evt.getPropertyName())) {
			updateEnablement();
		}
		super.propertyChange(evt);
	}

	protected void updateEnablement() {
		boolean isCommand = MENUITEM_TYPE_COMMAND.equals(getEditorValue(ATTR_TYPE));
		setEnabled(CHECKED, !isCommand);
		boolean isRadio = MENUITEM_TYPE_RADIO.equals(getEditorValue(ATTR_TYPE));
		setEnabled(ATTR_RADIOGROUP, isRadio);
	}

}
