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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFormWizardPage extends NewJQueryWidgetWizardPage {

	public NewFormWizardPage() {
		super("newForm", WizardMessages.newFormWizardTitle);
		setDescription(WizardMessages.newFormWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		addEditor(JQueryFieldEditorFactory.createNameEditor(), parent);
		createIDEditor(parent, true);
		addEditor(JQueryFieldEditorFactory.createFormActionEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createFormMethodEditor(), parent);
		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createValidateEditor(), columns.left());
		addEditor(JQueryFieldEditorFactory.createAutocompleteEditor(), columns.right());
	}

	protected boolean hasVisualPreview() {
		return false;
	}
	
}
