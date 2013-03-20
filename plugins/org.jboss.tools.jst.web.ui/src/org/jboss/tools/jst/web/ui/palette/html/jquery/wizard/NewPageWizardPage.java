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

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		IFieldEditor headerTitle = getEditor(EDITOR_ID_HEADER_TITLE);
		headerTitle.setEnabled(TRUE.equals(getEditorValue(EDITOR_ID_ADD_HEADER)));
		IFieldEditor footerTitle = getEditor(EDITOR_ID_FOOTER_TITLE);
		footerTitle.setEnabled(TRUE.equals(getEditorValue(EDITOR_ID_ADD_FOOTER)));

		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}

}
