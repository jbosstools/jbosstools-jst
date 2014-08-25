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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewButtonWizardPage extends NewIonicWidgetWizardPage {

	public NewButtonWizardPage() {
		super("newButton", WizardMessages.newButtonWizardTitle);
		setDescription(WizardMessages.newButtonWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Home");
		addEditor(label, parent);

		addEditor(IonicFieldEditorFactory.createButtonTypeEditor(), parent);

		createIDEditor(parent, true);
		createSeparator(parent);
		
		Group stylePanel = null;
		if(parent !=null) {
			stylePanel = new Group(parent,SWT.BORDER);
			stylePanel.setText("Style");
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			stylePanel.setLayoutData(d);
			stylePanel.setLayout(new GridLayout(3, false));
		}
		addEditor(IonicFieldEditorFactory.createButtonWidthEditor(), stylePanel);
		addEditor(IonicFieldEditorFactory.createButtonSizeEditor(), stylePanel);
		addEditor(IonicFieldEditorFactory.createButtonFillEditor(), stylePanel);


		createSeparator(parent);
	
		IFieldEditor icon = IonicFieldEditorFactory.createIconEditor(JQueryConstants.ATTR_ICON);
		addEditor(icon, parent, true);

		IFieldEditor iconpos = IonicFieldEditorFactory.createButtonIconPositionEditor();
		addEditor(iconpos, parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createButtonColorEditor(EDITOR_ID_BAR_COLOR), parent);
	
		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, parent);

	}

	public void validate() throws ValidationException {
//		setEnabled(EDITOR_ID_ICON_POS, !isTrue(EDITOR_ID_ICON_ONLY));
		super.validate();
	}
}
