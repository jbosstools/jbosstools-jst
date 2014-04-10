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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTextInputWizardPage extends NewJQueryWidgetWizardPage {

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

		createIDEditor(parent, false);

		IFieldEditor value = JQueryFieldEditorFactory.createValueEditor();
		addEditor(value, parent);
		
		IFieldEditor pattern = JQueryFieldEditorFactory.createPatternEditor();
		addEditor(pattern, parent);

		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createPlaceholderEditor(), columns.left());
		IFieldEditor maxlength = JQueryFieldEditorFactory.createMaxlengthEditor();
		addEditor(maxlength, columns.right());
		
		if(parent != null) {
			Object[] cs = maxlength.getEditorControls();
			GridData d = (GridData)((Text)cs[1]).getLayoutData();
			d.widthHint = 20;
			((Text)cs[1]).setLayoutData(d);
		}
		
		Group panel = null;
		if(parent != null) {
			panel = new Group(parent,SWT.BORDER);
			panel.setText("Number");
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			panel.setLayoutData(d);		
			panel.setLayout(new GridLayout(3, false));
		}

		Composite[] columns3 = createColumns(panel, 3);
		
		IFieldEditor min = JQueryFieldEditorFactory.createMinEditor(WizardDescriptions.textInputMin);
		min.setValue("");
		addEditor(min, columns3[0]);

		IFieldEditor max = JQueryFieldEditorFactory.createMaxEditor(WizardDescriptions.textInputMax);
		max.setValue("");
		addEditor(max, columns3[1]);

		IFieldEditor step = JQueryFieldEditorFactory.createStepEditor(WizardDescriptions.textInputStep);
		addEditor(step, columns3[2]);

		createSeparator(parent);
		
		columns3 = createColumns(parent, 3);
		addEditor(JQueryFieldEditorFactory.createClearInputEditor(), columns3[0]);
		addEditor(JQueryFieldEditorFactory.createMiniEditor(), columns3[1]);
		addEditor(JQueryFieldEditorFactory.createRequiredEditor(), columns3[2]);
		addEditor(JQueryFieldEditorFactory.createHideLabelEditor(), columns3[0]);
		addEditor(JQueryFieldEditorFactory.createDisabledEditor(), columns3[1]);
		addEditor(JQueryFieldEditorFactory.createAutofocusEditor(), columns3[2]);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent);

		createSeparator(parent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);

		updateNumberFieldsEnablement();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(EDITOR_ID_TEXT_TYPE.equals(evt.getPropertyName())) {
			updateNumberFieldsEnablement();
		}
		super.propertyChange(evt);
	}

	void updateNumberFieldsEnablement() {
		boolean isNumber = TYPE_NUMBER.equals(getEditorValue(EDITOR_ID_TEXT_TYPE));
		setEnabled(EDITOR_ID_MIN, isNumber);
		setEnabled(EDITOR_ID_MAX, isNumber);
		setEnabled(EDITOR_ID_STEP, isNumber);
	}

	public void validate() throws ValidationException {
		super.validate();
		String pattern = getEditorValue(EDITOR_ID_PATTERN);
		if(pattern.length() > 0) {
			try {
				Pattern.compile(pattern);
			} catch (PatternSyntaxException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}
