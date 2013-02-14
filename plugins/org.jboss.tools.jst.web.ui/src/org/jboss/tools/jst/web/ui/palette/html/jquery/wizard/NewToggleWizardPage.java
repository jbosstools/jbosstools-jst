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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewToggleWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewToggleWizardPage() {
		super("newToggle", WizardMessages.newToggleWizardTitle);
		setDescription(WizardMessages.newToggleWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		addEditor(label, parent);

		IFieldEditor offLabel = JQueryFieldEditorFactory.createOffLabelEditor();
		addEditor(offLabel, parent);
		
		IFieldEditor onLabel = JQueryFieldEditorFactory.createOnLabelEditor();
		addEditor(onLabel, parent);
		
		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);
		
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sd = new GridData(GridData.FILL_HORIZONTAL);
		sd.horizontalSpan = 3;
		separator.setLayoutData(sd);
		
		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, parent);

		IFieldEditor layout = JQueryFieldEditorFactory.createLayoutEditor();
		addEditor(layout, parent);
		Control c = (Control) (layout.getEditorControls()[1]);
		GridData d = (GridData)c.getLayoutData();
		d.horizontalAlignment = SWT.FILL;
		d.grabExcessHorizontalSpace = true;
		c.setLayoutData(d);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent);
		c = (Control) (theme.getEditorControls()[1]);
		d = (GridData)c.getLayoutData();
		d.horizontalAlignment = SWT.FILL;
		d.grabExcessHorizontalSpace = true;
		c.setLayoutData(d);
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException("ID is already used.");
		}
	}
}
