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
public class NewButtonWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewButtonWizardPage() {
		super("newButton", WizardMessages.newButtonWizardTitle);
		setDescription(WizardMessages.newButtonWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Link button");
		addEditor(label, parent);

		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addEditor(url, parent);
		
		IFieldEditor action = JQueryFieldEditorFactory.createActionEditor();
		addEditor(action, parent, true);

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, parent);

		createSeparator(parent);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, left);

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, right);

		IFieldEditor inline = JQueryFieldEditorFactory.createInlineEditor();
		addEditor(inline, left);

		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
		addEditor(span, right);

		createSeparator(parent);
	
		IFieldEditor icon = JQueryFieldEditorFactory.createIconEditor();
		addEditor(icon, parent, true);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent, true);

		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly, parent);

		createSeparator(parent);
	
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		boolean icononly = TRUE.equals(getEditorValue(EDITOR_ID_ICON_ONLY));
		IFieldEditor iconpos = getEditor(EDITOR_ID_ICON_POS);
		iconpos.setEnabled(!icononly);
	}
}
