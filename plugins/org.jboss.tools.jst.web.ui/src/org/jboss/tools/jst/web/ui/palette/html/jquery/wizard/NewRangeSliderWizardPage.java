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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewRangeSliderWizardPage extends NewJQueryWidgetWizardPage {

	public NewRangeSliderWizardPage() {
		super("newLink", WizardMessages.newRangeSliderWizardTitle);
		setDescription(WizardMessages.newRangeSliderWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Slider:");
		addEditor(label, parent);

		createIDEditor(parent, false);
		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor min = JQueryFieldEditorFactory.createMinEditor(WizardDescriptions.rangeSliderMin);
		addEditor(min, columns.right());

		IFieldEditor max = JQueryFieldEditorFactory.createMaxEditor(WizardDescriptions.rangeSliderMax);
		addEditor(max, columns.right());

		IFieldEditor step = JQueryFieldEditorFactory.createStepEditor(WizardDescriptions.rangeSliderStep);
		addEditor(step, columns.right());

		IFieldEditor value = JQueryFieldEditorFactory.createValueEditor();
		value.setValue("40");
		addEditor(value, columns.right());

		IFieldEditor rightValue = JQueryFieldEditorFactory.createRightValueEditor();
		addEditor(rightValue, columns.right());

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, columns.left());

		IFieldEditor highlight = JQueryFieldEditorFactory.createHighlightEditor();
		addEditor(highlight, columns.left());

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, columns.left());

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, columns.left());

		IFieldEditor range = JQueryFieldEditorFactory.createRangeEditor();
		addEditor(range, columns.left());

		createSeparator(parent);
		
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		IFieldEditor trackTheme = JQueryFieldEditorFactory.createDataTrackThemeEditor(WizardDescriptions.rangeSliderTrackTheme);
		addEditor(trackTheme, parent, true);
	}

	@Override
	public void validate() throws ValidationException {
		setEnabled(EDITOR_ID_RVALUE, isTrue(EDITOR_ID_RANGE));
		super.validate();
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

}
