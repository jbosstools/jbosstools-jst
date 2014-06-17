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
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewLabelWizardPage extends NewHTMLWidgetWizardPage {

	public NewLabelWizardPage() {
		super("newLabel", WizardMessages.newLabelWizardTitle);
		setDescription(WizardMessages.newLabelWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelTextEditor();
		label.setValue("Name:");
		addEditor(label, parent);

		createIDEditor(parent, true);

		IFieldEditor f = JQueryFieldEditorFactory.createForEditor();
		addEditor(f, parent);

		IFieldEditor form = JQueryFieldEditorFactory.createFormReferenceEditor();
		addEditor(form, parent);

	}

}
