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
public class NewCollapsibleWizardPage extends NewJQueryWidgetWizardPage {

	public NewCollapsibleWizardPage() {
		super("newCollapsible", WizardMessages.newCollapsibleWizardTitle);
		setDescription(WizardMessages.newCollapsibleWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor header = JQueryFieldEditorFactory.createCollapsibleHeaderEditor();
		header.setValue("Header");
		addEditor(header, parent);

		createIDEditor(parent, true);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor fieldSet = JQueryFieldEditorFactory.createFieldSetEditor();
		addEditor(fieldSet, columns.left());

		IFieldEditor collapsed = JQueryFieldEditorFactory.createCollapsedEditor();
		addEditor(collapsed, columns.right());

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, columns.left());

		IFieldEditor inset = JQueryFieldEditorFactory.createInsetEditor();
		addEditor(inset, columns.right());

		createSeparator(parent);
	
		IFieldEditor collapsedIcon = JQueryFieldEditorFactory.createCollapsedIconEditor();
		addEditor(collapsedIcon, parent, true);

		IFieldEditor expandedIcon = JQueryFieldEditorFactory.createExpandedIconEditor();
		addEditor(expandedIcon, parent, true);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent, true);

		createSeparator(parent);
	
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);

		IFieldEditor contentTheme = JQueryFieldEditorFactory.createDataContentThemeEditor();
		addEditor(contentTheme, parent, true);

		inset.setValue(TRUE);
	}

}
