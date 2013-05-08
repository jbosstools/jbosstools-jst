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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class NewRangeSliderWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewRangeSliderWizardPage() {
		super("newLink", WizardMessages.newRangeSliderWizardTitle);
		setDescription(WizardMessages.newRangeSliderWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Slider:");
		addEditor(label, parent);

		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);
		createSeparator(parent);

		Composite[] columns = createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor min = JQueryFieldEditorFactory.createMinEditor();
		addEditor(min, right);

		IFieldEditor max = JQueryFieldEditorFactory.createMaxEditor();
		addEditor(max, right);

		IFieldEditor step = JQueryFieldEditorFactory.createStepEditor();
		addEditor(step, right);

		IFieldEditor value = JQueryFieldEditorFactory.createValueEditor();
		value.setValue("40");
		addEditor(value, right);

		IFieldEditor rightValue = JQueryFieldEditorFactory.createRightValueEditor();
		addEditor(rightValue, right);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, left);

		IFieldEditor highlight = JQueryFieldEditorFactory.createHighlightEditor();
		addEditor(highlight, left);

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, left);

		IFieldEditor hideLabel = JQueryFieldEditorFactory.createHideLabelEditor();
		addEditor(hideLabel, left);

		IFieldEditor range = JQueryFieldEditorFactory.createRangeEditor();
		addEditor(range, left);

		createSeparator(parent);
		
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		IFieldEditor trackTheme = JQueryFieldEditorFactory.createDataTrackThemeEditor();
		addEditor(trackTheme, parent, true);
	}

	public void validate() throws ValidationException {
		IFieldEditor rightValue = getEditor(EDITOR_ID_RVALUE);
		rightValue.setEnabled(TRUE.equals(getEditorValue(EDITOR_ID_RANGE)));

		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}

	public static Composite[] createTwoColumns(Composite parent) {
		Composite all = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		all.setLayoutData(d);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 20;
		all.setLayout(layout);
		
		Composite left = new Composite(all, SWT.NONE);
		left.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		left.setLayout(layout);

		Composite right = new Composite(all, SWT.NONE);
		right.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		right.setLayout(layout);
		
		return new Composite[]{left, right};
	}

	protected int getPreferredBrowser() {
		return SWT.WEBKIT;
	}

}
