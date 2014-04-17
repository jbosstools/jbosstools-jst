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
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewHTMLWidgetWizardPage extends VersionedNewHTMLWidgetWizardPage {

	public NewHTMLWidgetWizardPage(String pageName, String title) {
		super(pageName, title);
	}

	@Override
    public NewHTMLWidgetWizard<?> getWizard() {
        return (NewHTMLWidgetWizard<?>)super.getWizard();
    }

	protected IFieldEditor createAddLibsEditor(Composite parent) {
		Label label = new Label(parent, 0);
		label.setText(WizardMessages.addReferencesToJSCSSLabel);
		label.setVisible(false);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(d);
		return null;
	}

}
