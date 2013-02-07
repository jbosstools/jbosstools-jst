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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCheckBoxWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewCheckBoxWizardPage() {
		super("newCheckBox", WizardMessages.newCheckboxWizardTitle);
		setDescription(WizardMessages.newCheckboxWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.doFillIntoGrid(parent);
		label.addPropertyChangeListener(this);
		addEditor(label);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		mini.doFillIntoGrid(parent);
		mini.addPropertyChangeListener(this);
		addEditor(mini);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		theme.doFillIntoGrid(parent);
		theme.addPropertyChangeListener(this);
		addEditor(theme);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		((Control) (theme.getEditorControls()[1])).setLayoutData(d);
	}

}
