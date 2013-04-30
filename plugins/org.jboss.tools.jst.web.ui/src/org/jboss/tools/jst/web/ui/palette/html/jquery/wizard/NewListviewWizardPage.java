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
public class NewListviewWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ListEditor items = new ListEditor(this, 1, 8);

	public NewListviewWizardPage() {
		super("newListview", WizardMessages.newListviewWizardTitle);
		setDescription(WizardMessages.newListviewWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor numbered = JQueryFieldEditorFactory.createNumberedEditor();
		addEditor(numbered, left);
		
		IFieldEditor readonly = JQueryFieldEditorFactory.createReadonlyEditor();
		addEditor(readonly, right);

		IFieldEditor autodividers = JQueryFieldEditorFactory.createAutodividersEditor();
		addEditor(autodividers, left);

		IFieldEditor searchFilter = JQueryFieldEditorFactory.createSearchFilterEditor();
		addEditor(searchFilter, right);

		IFieldEditor inset = JQueryFieldEditorFactory.createInsetEditor();
		addEditor(inset, left);

		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
		addEditor(span, right);

		items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		IFieldEditor dividerTheme = JQueryFieldEditorFactory.createDividerThemeEditor();
		addEditor(dividerTheme, parent, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching) {
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
