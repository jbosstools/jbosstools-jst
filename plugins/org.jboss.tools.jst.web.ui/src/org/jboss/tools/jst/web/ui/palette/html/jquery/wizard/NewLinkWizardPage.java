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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewLinkWizardPage extends NewJQueryWidgetWizardPage {

	public NewLinkWizardPage() {
		super("newLink", WizardMessages.newLinkWizardTitle);
		setDescription(WizardMessages.newLinkWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Link");
		addEditor(label, parent);

		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addEditor(url, parent);
		if(parent != null) {
			new IDContentProposalProvider(getWizard().getIDs(), url);
		}
		
		IFieldEditor action = JQueryFieldEditorFactory.createActionEditor();
		addEditor(action, parent, true);

		createIDEditor(parent, true);

		createSeparator(parent);

		IFieldEditor transition = JQueryFieldEditorFactory.createTransitionEditor(WizardDescriptions.transition);
		addEditor(transition, parent, true);
	}

}
