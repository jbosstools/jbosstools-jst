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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTextInputWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewTextInputWizardPage() {
		super("newText", WizardMessages.newTextInputWizardTitle);
		setDescription(WizardMessages.newTextInputWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor type = JQueryFieldEditorFactory.createTextTypeEditor();
		addEditor(type, parent);

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Input:");
		addEditor(label, parent);

		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);

		IFieldEditor value = JQueryFieldEditorFactory.createValueEditor();
		addEditor(value, parent);

		IFieldEditor placeholder = JQueryFieldEditorFactory.createPlaceholderEditor();
		addEditor(placeholder, parent);

		createSeparator(parent);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor clear = JQueryFieldEditorFactory.createClearInputEditor();
		addEditor(clear, left);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, right);

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, left);

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, right);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent);

		createSeparator(parent);
		
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}

}
