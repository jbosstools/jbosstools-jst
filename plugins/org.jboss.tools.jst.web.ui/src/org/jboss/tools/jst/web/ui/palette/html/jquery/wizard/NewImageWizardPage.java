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
public class NewImageWizardPage extends NewJQueryWidgetWizardPage {

	public NewImageWizardPage() {
		super("newImage", WizardMessages.newImageWizardTitle);
		setDescription(WizardMessages.newImageWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		addEditor(JQueryFieldEditorFactory.createSrcEditor(getWizard().getFile()), parent);
		createIDEditor(parent, true);
		addEditor(JQueryFieldEditorFactory.createAltEditor(), parent);
		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createWidthEditor(WizardDescriptions.imageWidth), columns.left());
		addEditor(JQueryFieldEditorFactory.createHeightEditor(WizardDescriptions.imageHeight), columns.right());
		addEditor(JQueryFieldEditorFactory.createIsmapEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createUsemapEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createCrossoriginEditor(), parent);
	}

//	protected boolean hasVisualPreview() {
//		return false;
//	}
//	
}
