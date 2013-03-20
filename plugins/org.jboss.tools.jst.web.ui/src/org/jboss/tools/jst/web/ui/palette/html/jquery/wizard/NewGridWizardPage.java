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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewGridWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewGridWizardPage() {
		super("newGrid", WizardMessages.newGridWizardTitle);
		setDescription(WizardMessages.newGridWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		Composite left = new Composite(parent, SWT.NONE);
		left.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginRight = 20;
		left.setLayout(layout);
		
		Label center = new Label(parent, SWT.NONE);
		center.setText("x");
		GridData d = new GridData();
		center.setLayoutData(d);

		Composite right = new Composite(parent, SWT.NONE);
		right.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 20;
		right.setLayout(layout);

		IFieldEditor columns = JQueryFieldEditorFactory.createGridColumnsEditor();
		addEditor(columns, left, true);

		IFieldEditor rows = JQueryFieldEditorFactory.createGridRowsEditor();
		addEditor(rows, right, true);
	}

}
