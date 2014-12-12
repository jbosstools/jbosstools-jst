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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCanvasWizardPage extends NewHTMLWidgetWizardPage {

	public NewCanvasWizardPage() {
		super("newCanvas", WizardMessages.newCanvasWizardTitle);
		setDescription(WizardMessages.newCanvasWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		createIDEditor(parent, false);

		IFieldEditor width = JQueryFieldEditorFactory.createWidthEditor(WizardDescriptions.canvasWidth);
		addEditor(width, parent);

		IFieldEditor height = JQueryFieldEditorFactory.createHeightEditor(WizardDescriptions.canvasHeight);
		addEditor(height, parent);

		createSeparator(parent);

		IFieldEditor addScriptTemplate = HTMLFieldEditorFactory.createAddScriptTemplateEditor();
		addEditor(addScriptTemplate, parent);
	}

}
