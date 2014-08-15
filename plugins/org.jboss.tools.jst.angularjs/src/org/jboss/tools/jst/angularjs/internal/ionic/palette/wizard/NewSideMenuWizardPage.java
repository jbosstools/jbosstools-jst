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

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewSideMenuWizardPage extends NewIonicWidgetWizardPage {

	public NewSideMenuWizardPage() {
		super("newSideMenu", IonicWizardMessages.newSideMenuWizardTitle);
		setDescription(IonicWizardMessages.newSideMenuWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createDragContentEditor(), parent);
		addEditor(IonicFieldEditorFactory.createEdgeDragThresholdEditor(), parent);

		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor leftButton = IonicFieldEditorFactory.createLeftMenuEditor();
		addEditor(leftButton, columns.left());

		Composite leftParent = null;
		if(parent != null) {
			leftParent = new Composite(columns.left(), SWT.BORDER);
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			leftParent.setLayoutData(d);
			leftParent.setLayout(new GridLayout(3, false));
		}
		IFieldEditor leftTitle = IonicFieldEditorFactory.createMenuTitleEditor(EDITOR_ID_LEFT_MENU_TITLE);
		leftTitle.setValue("Left");
		addEditor(leftTitle, leftParent);
		addEditor(IonicFieldEditorFactory.createIsEnabledEditor(EDITOR_ID_LEFT_IS_ENABLED), leftParent);
		addEditor(IonicFieldEditorFactory.createMenuWidthEditor(EDITOR_ID_LEFT_WIDTH), leftParent);
		addEditor(IonicFieldEditorFactory.createAddToggleEditor(EDITOR_ID_LEFT_ADD_MENU_TOGGLE), leftParent);
	
		IFieldEditor rightButton =  IonicFieldEditorFactory.createRightMenuEditor();
		addEditor(rightButton, columns.right());
		rightButton.setValue(Boolean.FALSE);

		Composite rightParent = null;
		if(parent != null) {
			rightParent = new Composite(columns.right(), SWT.BORDER);
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			rightParent.setLayoutData(d);
			rightParent.setLayout(new GridLayout(3, false));
		}
		IFieldEditor rightTitle = IonicFieldEditorFactory.createMenuTitleEditor(EDITOR_ID_RIGHT_MENU_TITLE);
		rightTitle.setValue("Right");
		addEditor(rightTitle, rightParent);
		addEditor(IonicFieldEditorFactory.createIsEnabledEditor(EDITOR_ID_RIGHT_IS_ENABLED), rightParent);
		addEditor(IonicFieldEditorFactory.createMenuWidthEditor(EDITOR_ID_RIGHT_WIDTH), rightParent);
		addEditor(IonicFieldEditorFactory.createAddToggleEditor(EDITOR_ID_RIGHT_ADD_MENU_TOGGLE), rightParent);
		
		updateRightMenu();
	}

	public void validate() throws ValidationException {
		boolean leftMenuEnabled = isTrue(EDITOR_ID_LEFT_MENU);
		setEnabled(EDITOR_ID_LEFT_MENU_TITLE, leftMenuEnabled);
		setEnabled(EDITOR_ID_LEFT_IS_ENABLED, leftMenuEnabled);
		setEnabled(EDITOR_ID_LEFT_WIDTH, leftMenuEnabled);
		setEnabled(EDITOR_ID_LEFT_ADD_MENU_TOGGLE, leftMenuEnabled);

		updateRightMenu();

		boolean drag = isTrue(ATTR_DRAG_CONTENT);
		setEnabled(ATTR_EDGE_DRAG_THRESHOLD, drag);

		super.validate();
	}

	void updateRightMenu() {
		boolean rightMenuEnabled = isTrue(EDITOR_ID_RIGHT_MENU);
		setEnabled(EDITOR_ID_RIGHT_MENU_TITLE, rightMenuEnabled);
		setEnabled(EDITOR_ID_RIGHT_ADD_MENU_TOGGLE, rightMenuEnabled);
		setEnabled(EDITOR_ID_RIGHT_IS_ENABLED, rightMenuEnabled);
		setEnabled(EDITOR_ID_RIGHT_WIDTH, rightMenuEnabled);
	}

}
