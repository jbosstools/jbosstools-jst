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

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTableWizardPage extends NewHTMLWidgetWizardPage {
	ColumnEditor columns = new ColumnEditor(this, 1, 6);

	public NewTableWizardPage() {
		super("newTable", WizardMessages.newTableTitle);
		setDescription(WizardMessages.newHTML5TableDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor caption = HTMLFieldEditorFactory.createCaptionEditor();
		addEditor(caption, parent);

		createIDEditor(parent, true);

		IFieldEditor kindEditor = HTMLFieldEditorFactory.createTableKindEditor();
		addEditor(kindEditor, parent);

		TwoColumns cs = createTwoColumns(parent);
		addEditor(HTMLFieldEditorFactory.createAddHeaderEditor(), cs.left());
		addEditor(HTMLFieldEditorFactory.createAddFooterEditor(), cs.right());

		columns.createControl(parent, WizardMessages.columnsLabel);


	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(columns.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		columns.onPropertyChange(name, value);

		boolean isAdvanced = TABLE_KIND_ADVANCED.equals(getEditorValue(EDITOR_ID_TABLE_KIND));
		setEnabled(TAG_TFOOT, isAdvanced);

		columns.updateEnablement();

		super.propertyChange(evt);
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

}
