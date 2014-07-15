/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFooterBarWizardPage extends NewIonicWidgetWizardPage {

	public NewFooterBarWizardPage() {
		super("newFooterBar", WizardMessages.newFooterWizardTitle);
		setDescription(IonicWizardMessages.newFooterWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor(WizardDescriptions.headerTitle);
		title.setValue("Title");
		addEditor(title, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		IFieldEditor barColor = IonicFieldEditorFactory.createBarColorEditor(EDITOR_ID_BAR_COLOR);
		addEditor(barColor, parent);

		IFieldEditor alignTitle = IonicFieldEditorFactory.createAlignTitleEditor();
		addEditor(alignTitle, parent);

		IFieldEditor subfooter = IonicFieldEditorFactory.createSubfooterEditor();
		addEditor(subfooter, parent);

		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor leftButton = JQueryFieldEditorFactory.createLeftButtonEditor();
		addEditor(leftButton, columns.left());

		Composite leftParent = null;
		if(parent != null) {
			leftParent = new Composite(columns.left(), SWT.BORDER);
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			leftParent.setLayoutData(d);
			leftParent.setLayout(new GridLayout(3, false));
		}

		IFieldEditor leftButtonLabel = JQueryFieldEditorFactory.createLabelEditor(JQueryConstants.EDITOR_ID_LEFT_BUTTON_LABEL);
		leftButtonLabel.setValue("Left button");
		addEditor(leftButtonLabel, leftParent);

		IFieldEditor leftButtonClick = IonicFieldEditorFactory.createNgClickEditor(EDITOR_ID_LEFT_BUTTON_CLICK);
		addEditor(leftButtonClick, leftParent);

		IFieldEditor leftButtonIcon = IonicFieldEditorFactory.createIconEditor(JQueryConstants.EDITOR_ID_LEFT_BUTTON_ICON);
		leftButtonIcon.setValue("");
		addEditor(leftButtonIcon, leftParent);

		IFieldEditor rightButton = JQueryFieldEditorFactory.createRightButtonEditor();
		addEditor(rightButton, columns.right());

		Composite rightParent = null;
		if(parent != null) {
			rightParent = new Composite(columns.right(), SWT.BORDER);
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			rightParent.setLayoutData(d);
			rightParent.setLayout(new GridLayout(3, false));
		}

		IFieldEditor rightButtonLabel = JQueryFieldEditorFactory.createLabelEditor(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_LABEL);
		rightButtonLabel.setValue("Right button");
		addEditor(rightButtonLabel, rightParent);

		IFieldEditor rightButtonClick = IonicFieldEditorFactory.createNgClickEditor(EDITOR_ID_RIGHT_BUTTON_CLICK);
		addEditor(rightButtonClick, rightParent);

		IFieldEditor rightButtonIcon = IonicFieldEditorFactory.createIconEditor(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_ICON);
		rightButtonIcon.setValue("");
		addEditor(rightButtonIcon, rightParent);
	}

	@Override
	public void validate() throws ValidationException {
		boolean leftButtonEnabled = isTrue(JQueryConstants.EDITOR_ID_LEFT_BUTTON);
		setEnabled(JQueryConstants.EDITOR_ID_LEFT_BUTTON_LABEL, leftButtonEnabled);
		setEnabled(JQueryConstants.EDITOR_ID_LEFT_BUTTON_ICON, leftButtonEnabled);
		setEnabled(IonicConstants.EDITOR_ID_LEFT_BUTTON_CLICK, leftButtonEnabled);

		boolean rightButtonEnabled = isTrue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON);
		setEnabled(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_LABEL, rightButtonEnabled);
		setEnabled(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_ICON, rightButtonEnabled);
		setEnabled(IonicConstants.EDITOR_ID_RIGHT_BUTTON_CLICK, rightButtonEnabled);

		super.validate();
	}
}