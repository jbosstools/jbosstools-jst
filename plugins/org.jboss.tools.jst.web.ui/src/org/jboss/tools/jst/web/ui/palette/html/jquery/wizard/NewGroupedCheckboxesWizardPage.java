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
public class NewGroupedCheckboxesWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	CheckboxesEditor buttons = new CheckboxesEditor(this, 1, 8);

	public NewGroupedCheckboxesWizardPage() {
		super("newGroupedCheckboxes", WizardMessages.newGroupedCheckboxesTitle);
		setDescription(WizardMessages.newGroupedCheckboxesDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor legend = JQueryFieldEditorFactory.createLegendEditor();
		addEditor(legend, parent);

		IFieldEditor layoutEditor = JQueryFieldEditorFactory.createLayoutEditor();
		layoutEditor.setValue(LAYOUT_VERTICAL);
		addEditor(layoutEditor, parent, true);

		Composite panel = buttons.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, panel);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, panel, true);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		iconpos.setEnabled(false);
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
		if(iconpos != null) {
			boolean h = (LAYOUT_HORIZONTAL.equals(getEditorValue(EDITOR_ID_LAYOUT)));
			iconpos.setEnabled(!h);
		}		

		super.propertyChange(evt);
	}

}
