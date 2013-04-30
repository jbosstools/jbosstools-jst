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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPopupWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewPopupWizardPage() {
		super("newPopup", WizardMessages.newPopupWizardTitle);
		setDescription(WizardMessages.newPopupWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		Group buttonPanel = new Group(parent,SWT.BORDER);
		buttonPanel.setText("Open Popup Button");
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		buttonPanel.setLayoutData(d);
		buttonPanel.setLayout(new GridLayout(3, false));		

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Popup");
		addEditor(label, buttonPanel);

		IFieldEditor info = JQueryFieldEditorFactory.createInfoStyledEditor();
		addEditor(info, buttonPanel);

		IFieldEditor transition = JQueryFieldEditorFactory.createTransitionEditor();
		addEditor(transition, buttonPanel, true);

		IFieldEditor positionTo = JQueryFieldEditorFactory.createPositionToEditor();
		addEditor(positionTo, buttonPanel, true);

		Group windowPanel = new Group(parent,SWT.BORDER);
		windowPanel.setText("Popup Window");
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		windowPanel.setLayoutData(d);
		windowPanel.setLayout(new GridLayout(3, false));		

		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, windowPanel);

		IFieldEditor close = JQueryFieldEditorFactory.createClosePopupButtonEditor();
		addEditor(close, windowPanel);
		
		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(windowPanel);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor dismissable = JQueryFieldEditorFactory.createDismissableEditor();
		addEditor(dismissable, left);

		IFieldEditor shadow = JQueryFieldEditorFactory.createShadowEditor();
		addEditor(shadow, right);

		IFieldEditor padding = JQueryFieldEditorFactory.createPaddingEditor();
		addEditor(padding, left);

		IFieldEditor overlay = JQueryFieldEditorFactory.createOverlayEditor();
		addEditor(overlay, right);

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, windowPanel);
		
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, windowPanel, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean shadow = TRUE.equals(getEditorValue(EDITOR_ID_SHADOW));
		IFieldEditor theme = getEditor(EDITOR_ID_THEME);
		if(theme != null) {
			theme.setEnabled(shadow);
		}
		
		super.propertyChange(evt);
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}
}
