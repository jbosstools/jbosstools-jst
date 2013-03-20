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
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewListviewWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewListviewWizardPage() {
		super("newListview", WizardMessages.newListviewWizardTitle);
		setDescription(WizardMessages.newListviewWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor numbered = JQueryFieldEditorFactory.createNumberedEditor();
		addEditor(numbered, parent);
		
		IFieldEditor readonly = JQueryFieldEditorFactory.createReadonlyEditor();
		addEditor(readonly, parent);

		IFieldEditor autodividers = JQueryFieldEditorFactory.createAutodividersEditor();
		addEditor(autodividers, parent);

		IFieldEditor searchFilter = JQueryFieldEditorFactory.createSearchFilterEditor();
		addEditor(searchFilter, parent);

		IFieldEditor inset = JQueryFieldEditorFactory.createInsetEditor();
		addEditor(inset, parent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

}
