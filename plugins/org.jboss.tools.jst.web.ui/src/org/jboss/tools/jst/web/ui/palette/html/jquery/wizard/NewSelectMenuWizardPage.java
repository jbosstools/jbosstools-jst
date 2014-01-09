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
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewSelectMenuWizardPage extends NewJQueryWidgetWizardPage {
	SelectMenuEditor items = new SelectMenuEditor(this, 1, 8);

	public NewSelectMenuWizardPage() {
		super("newSelectMenu", WizardMessages.newSelectMenuWizardTitle);
		setDescription(WizardMessages.newSelectMenuWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Select:");
		addEditor(label, parent);

		createIDEditor(parent, false);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, columns.left());

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, columns.right());

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, columns.left());
		
		IFieldEditor inline = JQueryFieldEditorFactory.createInlineEditor();
		addEditor(inline, columns.right());

//		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
//		addEditor(span, right);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent, true);

		items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
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
