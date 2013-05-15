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
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPageWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewPageWizardPage() {
		super("newPage", WizardMessages.newPageWizardTitle);
		setDescription(WizardMessages.newPageWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor addHeader = JQueryFieldEditorFactory.createAddHeaderEditor();
		addEditor(addHeader, parent);

		IFieldEditor headerTitle = JQueryFieldEditorFactory.createHeaderTitleEditor();
		addEditor(headerTitle, parent);

		IFieldEditor addFooter = JQueryFieldEditorFactory.createAddFooterEditor();
		addEditor(addFooter, parent);

		IFieldEditor footerTitle = JQueryFieldEditorFactory.createFooterTitleEditor();
		addEditor(footerTitle, parent);

		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);

		createSeparator(parent);

		IFieldEditor backButton = JQueryFieldEditorFactory.createBackButtonEditor();
		addEditor(backButton, parent);

		Composite backParent = new Composite(parent, SWT.BORDER);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		backParent.setLayoutData(d);
		backParent.setLayout(new GridLayout(3, false));
		
		IFieldEditor backButtonLabel = JQueryFieldEditorFactory.createLabelEditor(EDITOR_ID_LABEL);
		backButtonLabel.setValue("Back");
		addEditor(backButtonLabel, backParent);

		IFieldEditor backButtonIcon = JQueryFieldEditorFactory.createIconEditor(EDITOR_ID_ICON);
		backButtonIcon.setValue("back");
		addEditor(backButtonIcon, backParent);

		IFieldEditor backButtonIconOnly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(backButtonIconOnly, backParent);
		
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		updateBackButtonEnablement();
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if(EDITOR_ID_BACK_BUTTON.equals(name)) {
			updateBackButtonEnablement();
		} else if(EDITOR_ID_ADD_HEADER.equals(name)) {
			getEditor(EDITOR_ID_HEADER_TITLE).setEnabled(TRUE.equals(getEditorValue(EDITOR_ID_ADD_HEADER)));
		} else if(EDITOR_ID_ADD_FOOTER.equals(name)) {
			getEditor(EDITOR_ID_FOOTER_TITLE).setEnabled(TRUE.equals(getEditorValue(EDITOR_ID_ADD_FOOTER)));
		}
		super.propertyChange(evt);
	}

	private void updateBackButtonEnablement() {
		boolean backButtonEnabled = TRUE.equals(getEditorValue(EDITOR_ID_BACK_BUTTON));
		getEditor(EDITOR_ID_LABEL).setEnabled(backButtonEnabled);
		getEditor(EDITOR_ID_ICON).setEnabled(backButtonEnabled);
		getEditor(EDITOR_ID_ICON_ONLY).setEnabled(backButtonEnabled);
	}
}
