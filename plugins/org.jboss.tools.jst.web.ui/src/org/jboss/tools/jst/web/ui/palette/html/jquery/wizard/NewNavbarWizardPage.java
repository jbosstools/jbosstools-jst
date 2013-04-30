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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewNavbarWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ButtonsEditor buttons = new ButtonsEditor(this, 1, 8);

	public NewNavbarWizardPage() {
		super("newNavbar", WizardMessages.newNavbarWizardTitle);
		setDescription(WizardMessages.newNavbarWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		Composite panel = buttons.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, panel, true);

		iconpos.setEnabled(false);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(buttons.onPropertyChange(name, value)) {
			if(getEditor(EDITOR_ID_ICON_POS) != null) {
				getEditor(EDITOR_ID_ICON_POS).setEnabled(buttons.hasIcons());
			}
		}
		super.propertyChange(evt);
	}

}
