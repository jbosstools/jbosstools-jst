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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewHeaderBarWizardPage extends NewJQueryWidgetWizardPage {

	public NewHeaderBarWizardPage() {
		super("newHeaderBar", WizardMessages.newHeaderWizardTitle);
		setDescription(WizardMessages.newHeaderWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor(WizardDescriptions.headerTitle);
		title.setValue("Edit Contact");
		addEditor(title, parent);

		createIDEditor(parent, true);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor(WizardDescriptions.headerFixedPosition);
		addEditor(fixed, columns.left());
		
		IFieldEditor fullScreen = JQueryFieldEditorFactory.createFullScreenEditor(WizardDescriptions.headerFullScreen);
		addEditor(fullScreen, columns.right());

		createSeparator(parent);

		columns = createTwoColumns(parent);

		IFieldEditor leftButton = JQueryFieldEditorFactory.createLeftButtonEditor();
		addEditor(leftButton, columns.left());

		Composite leftParent = new Composite(columns.left(), SWT.BORDER);
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
		new IDContentProposalProvider(getWizard().getIDs(), leftButtonURL);

		IFieldEditor leftButtonIcon = JQueryFieldEditorFactory.createIconEditor(EDITOR_ID_LEFT_BUTTON_ICON);
		leftButtonIcon.setValue("delete");
		addEditor(leftButtonIcon, leftParent);

		IFieldEditor rightButton = JQueryFieldEditorFactory.createRightButtonEditor();
		addEditor(rightButton, columns.right());
		
		Composite rightParent = new Composite(columns.right(), SWT.BORDER);
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
		new IDContentProposalProvider(getWizard().getIDs(), rightButtonURL);

		IFieldEditor rightButtonIcon = JQueryFieldEditorFactory.createIconEditor(EDITOR_ID_RIGHT_BUTTON_ICON);
		rightButtonIcon.setValue("check");
		addEditor(rightButtonIcon, rightParent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		boolean leftButtonEnabled = isTrue(EDITOR_ID_LEFT_BUTTON);
		setEnabled(EDITOR_ID_LEFT_BUTTON_LABEL, leftButtonEnabled);
		setEnabled(EDITOR_ID_LEFT_BUTTON_URL, leftButtonEnabled);
		setEnabled(EDITOR_ID_LEFT_BUTTON_ICON, leftButtonEnabled);

		boolean rightButtonEnabled = isTrue(EDITOR_ID_RIGHT_BUTTON);
		setEnabled(EDITOR_ID_RIGHT_BUTTON_LABEL, rightButtonEnabled);
		setEnabled(EDITOR_ID_RIGHT_BUTTON_URL, rightButtonEnabled);
		setEnabled(EDITOR_ID_RIGHT_BUTTON_ICON, rightButtonEnabled);

		boolean isFixed = isTrue(EDITOR_ID_FIXED_POSITION);
		setEnabled(EDITOR_ID_FULL_SCREEN, isFixed);

		super.validate();
	}

}
