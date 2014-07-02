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
package org.jboss.tools.jst.angularjs.internal.palette.wizard;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewContentWizardPage extends NewIonicWidgetWizardPage {

	public NewContentWizardPage() {
		super("newContent", IonicWizardMessages.newContentWizardTitle);
		setDescription(IonicWizardMessages.newContentWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor addHeader = JQueryFieldEditorFactory.createAddHeaderEditor();
		addEditor(addHeader, parent);

		IFieldEditor headerTitle = JQueryFieldEditorFactory.createHeaderTitleEditor();
		addEditor(headerTitle, parent);

		IFieldEditor addFooter = JQueryFieldEditorFactory.createAddFooterEditor();
		addEditor(addFooter, parent);

		IFieldEditor footerTitle = JQueryFieldEditorFactory.createFooterTitleEditor();
		addEditor(footerTitle, parent);

		createIDEditor(parent, false);

		createSeparator(parent);
	}
}