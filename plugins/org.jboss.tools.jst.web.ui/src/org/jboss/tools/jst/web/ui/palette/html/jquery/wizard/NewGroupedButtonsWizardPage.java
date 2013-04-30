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
public class NewGroupedButtonsWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ButtonsEditor buttons = new ButtonsEditor(this, 1, 6);

	public NewGroupedButtonsWizardPage() {
		super("newGroupedButtons", WizardMessages.newGroupedButtonsTitle);
		setDescription(WizardMessages.newGroupedButtonsDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor layoutEditor = JQueryFieldEditorFactory.createLayoutEditor();
		layoutEditor.setValue(LAYOUT_VERTICAL);
		addEditor(layoutEditor, parent, true);

		Composite panel = buttons.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, panel);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, panel, true);

		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly, panel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		iconpos.setEnabled(false);
		icononly.setEnabled(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		buttons.onPropertyChange(name, value);

		IFieldEditor iconpos = getEditor(EDITOR_ID_ICON_POS);
		IFieldEditor icononly = getEditor(EDITOR_ID_ICON_ONLY);
		if(iconpos != null && icononly != null) {
			boolean hasIcons = buttons.hasIcons();
			icononly.setEnabled(hasIcons);
			iconpos.setEnabled(hasIcons && !TRUE.equals(getEditorValue(EDITOR_ID_ICON_ONLY)));
		}		

		super.propertyChange(evt);
	}

}
