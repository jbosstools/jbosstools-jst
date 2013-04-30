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
public class NewToggleWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewToggleWizardPage() {
		super("newToggle", WizardMessages.newToggleWizardTitle);
		setDescription(WizardMessages.newToggleWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Switch:");
		addEditor(label, parent);

		IFieldEditor offLabel = JQueryFieldEditorFactory.createOffLabelEditor();
		addEditor(offLabel, parent);
		
		IFieldEditor onLabel = JQueryFieldEditorFactory.createOnLabelEditor();
		addEditor(onLabel, parent);
		
		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);
		
		createSeparator(parent);
		
		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, left);

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, right);

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, left);
		
		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
		addEditor(span, right);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent, true);

		createSeparator(parent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		IFieldEditor trackTheme = JQueryFieldEditorFactory.createDataTrackThemeEditor();
		addEditor(trackTheme, parent, true);
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}
}
