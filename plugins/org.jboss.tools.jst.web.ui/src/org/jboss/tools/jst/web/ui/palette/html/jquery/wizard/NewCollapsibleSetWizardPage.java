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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCollapsibleSetWizardPage extends NewJQueryWidgetWizardPage {
	CollapsiblesEditor items = new CollapsiblesEditor(this, 1, 8);

	public NewCollapsibleSetWizardPage() {
		super("newCollapsibleSet", WizardMessages.newCollapsibleSetWizardTitle);
		setDescription(WizardMessages.newCollapsibleSetWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, columns.left());

		IFieldEditor inset = JQueryFieldEditorFactory.createInsetEditor(WizardDescriptions.collapsibleInset);
		addEditor(inset, columns.right());

		createIDEditor(parent, true);

		Composite panel = items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor collapsedIcon = JQueryFieldEditorFactory.createCollapsedIconEditor();
		addEditor(collapsedIcon, panel, true);

		IFieldEditor expandedIcon = JQueryFieldEditorFactory.createExpandedIconEditor();
		addEditor(expandedIcon, panel, true);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, panel, true);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);

		IFieldEditor contentTheme = JQueryFieldEditorFactory.createDataContentThemeEditor();
		addEditor(contentTheme, parent, true);

		inset.setValue(TRUE);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(items.onPropertyChange(name, value)) {
			if(EDITOR_ID_COLLAPSED.equals(name)) {
				items.onCollapsedModified();
			}
		}
		super.propertyChange(evt);
	}

}
