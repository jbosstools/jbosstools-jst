/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.wizards.project;

import org.jboss.tools.common.propertieseditor.PropertiesEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.project.helpers.*;
import org.jboss.tools.jst.web.ui.Messages;

public class NewWebProjectWizardPreprocessingPage extends WizardPage {
	NewWebProjectContext context;

	public NewWebProjectWizardPreprocessingPage(NewWebProjectContext context) {
		super(Messages.NewWebProjectWizardPreprocessingPage_PreprocessingPage);
		this.context = context;
	}

	PropertiesEditor propertiesEditor = new PropertiesEditor();
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		Control c = propertiesEditor.createControl(composite);
		GridData d = new GridData(GridData.FILL_BOTH);
		d.widthHint = 400;
		c.setLayoutData(d);
		this.setControl(composite);
	}
	
	public void setVisible(boolean visible) {
		if(visible) {
			ProjectTemplate t = context.getProjectTemplate();
			XModelObject o = (t == null) ? null : t.getProperties();
			propertiesEditor.setObject(null);
			propertiesEditor.update();
			propertiesEditor.setObject(o);
			propertiesEditor.update();
		}
		super.setVisible(visible);
	}

}
