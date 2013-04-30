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
public class NewHeaderBarWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewHeaderBarWizardPage() {
		super("newHeaderBar", WizardMessages.newHeaderWizardTitle);
		setDescription(WizardMessages.newHeaderWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor();
		title.setValue("Edit Contact");
		addEditor(title, parent);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor();
		addEditor(fixed, left);
		
		IFieldEditor fullScreen = JQueryFieldEditorFactory.createFullScreenEditor();
		addEditor(fullScreen, right);
		
		IFieldEditor leftButton = JQueryFieldEditorFactory.createLeftButtonEditor();
		addEditor(leftButton, parent);

		Composite leftParent = new Composite(parent, SWT.BORDER);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		leftParent.setLayoutData(d);
		leftParent.setLayout(new GridLayout(3, false));
		
		IFieldEditor leftButtonLabel = JQueryFieldEditorFactory.createLabelEditor(EDITOR_ID_LEFT_BUTTON_LABEL);
		leftButtonLabel.setValue("Cancel");
		addEditor(leftButtonLabel, leftParent);

		IFieldEditor leftButtonURL = JQueryFieldEditorFactory.createURLEditor(EDITOR_ID_LEFT_BUTTON_URL);
		leftButtonURL.setValue("#");
		addEditor(leftButtonURL, leftParent);

		IFieldEditor leftButtonIcon = JQueryFieldEditorFactory.createIconEditor(EDITOR_ID_LEFT_BUTTON_ICON);
		leftButtonIcon.setValue("delete");
		addEditor(leftButtonIcon, leftParent);

		IFieldEditor rightButton = JQueryFieldEditorFactory.createRightButtonEditor();
		addEditor(rightButton, parent);
		
		Composite rightParent = new Composite(parent, SWT.BORDER);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		rightParent.setLayoutData(d);
		rightParent.setLayout(new GridLayout(3, false));
		
		IFieldEditor rightButtonLabel = JQueryFieldEditorFactory.createLabelEditor(EDITOR_ID_RIGHT_BUTTON_LABEL);
		rightButtonLabel.setValue("Save");
		addEditor(rightButtonLabel, rightParent);

		IFieldEditor rightButtonURL = JQueryFieldEditorFactory.createURLEditor(EDITOR_ID_RIGHT_BUTTON_URL);
		rightButtonURL.setValue("#");
		addEditor(rightButtonURL, rightParent);

		IFieldEditor rightButtonIcon = JQueryFieldEditorFactory.createIconEditor(EDITOR_ID_RIGHT_BUTTON_ICON);
		rightButtonIcon.setValue("check");
		addEditor(rightButtonIcon, rightParent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		boolean leftButtonEnabled = TRUE.equals(getEditorValue(EDITOR_ID_LEFT_BUTTON));
		IFieldEditor leftButtonLabel = getEditor(EDITOR_ID_LEFT_BUTTON_LABEL);
		leftButtonLabel.setEnabled(leftButtonEnabled);
		IFieldEditor leftButtonURL = getEditor(EDITOR_ID_LEFT_BUTTON_URL);
		leftButtonURL.setEnabled(leftButtonEnabled);
		IFieldEditor leftButtonIcon = getEditor(EDITOR_ID_LEFT_BUTTON_ICON);
		leftButtonIcon.setEnabled(leftButtonEnabled);

		boolean rightButtonEnabled = TRUE.equals(getEditorValue(EDITOR_ID_RIGHT_BUTTON));
		IFieldEditor rightButtonLabel = getEditor(EDITOR_ID_RIGHT_BUTTON_LABEL);
		rightButtonLabel.setEnabled(rightButtonEnabled);
		IFieldEditor rightButtonURL = getEditor(EDITOR_ID_RIGHT_BUTTON_URL);
		rightButtonURL.setEnabled(rightButtonEnabled);
		IFieldEditor rightButtonIcon = getEditor(EDITOR_ID_RIGHT_BUTTON_ICON);
		rightButtonIcon.setEnabled(rightButtonEnabled);

		boolean isFixed = TRUE.equals(getEditorValue(EDITOR_ID_FIXED_POSITION));
		if(getEditor(EDITOR_ID_FULL_SCREEN) != null) {
			getEditor(EDITOR_ID_FULL_SCREEN).setEnabled(isFixed);
		}
	}

}
