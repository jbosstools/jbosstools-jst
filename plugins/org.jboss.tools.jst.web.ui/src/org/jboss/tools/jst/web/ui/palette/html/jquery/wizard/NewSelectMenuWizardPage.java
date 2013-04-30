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
public class NewSelectMenuWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	SelectMenuEditor items = new SelectMenuEditor(this, 1, 8);

	public NewSelectMenuWizardPage() {
		super("newSelectMenu", WizardMessages.newSelectMenuWizardTitle);
		setDescription(WizardMessages.newSelectMenuWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Select:");
		addEditor(label, parent);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, left);

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, right);

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, left);
		
		IFieldEditor inline = JQueryFieldEditorFactory.createInlineEditor();
		addEditor(inline, right);

//		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
//		addEditor(span, right);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent, true);

		items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(items.onPropertyChange(name, value)) {
			if(EDITOR_ID_SELECTED.equals(name)) {
				items.onSelectedModified();
			}
		}
		super.propertyChange(evt);
	}

}
