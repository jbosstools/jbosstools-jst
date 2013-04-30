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

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ColumnEditor extends ItemsEditor {

	public ColumnEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			items[i].setValue(EDITOR_ID_COLUMN_NAME, "" + (char)('A' + i));
			items[i].setValue(EDITOR_ID_PRIORITY, (i > 0) ? "" + (char)('0' + i) : "");
			items[i].setValue(EDITOR_ID_CONTENT, "" + (char)('a' + i));
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createColumnNameEditor());
		IFieldEditor priority = JQueryFieldEditorFactory.createPriorityEditor();
		addItemEditor(priority);
		page.expandCombo(priority);
		addItemEditor(JQueryFieldEditorFactory.createColumnContentEditor());
	}

	public String getColumnName(int i) {
		return items[i].getValue(EDITOR_ID_COLUMN_NAME);
	}

	public String getPriority(int i) {
		return items[i].getValue(EDITOR_ID_PRIORITY);
	}

	public String getContent(int i) {
		return items[i].getValue(EDITOR_ID_CONTENT);
	}

}
