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

import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.ItemsEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

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
			items[i].setValue(EDITOR_ID_CONTENT, "" + (char)('a' + i));
			items[i].setValue(HTMLConstants.EDITOR_ID_FOOTER_CONTENT, "" + (char)('A' + i));
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createColumnNameEditor());
		addItemEditor(JQueryFieldEditorFactory.createColumnContentEditor());
		addItemEditor(HTMLFieldEditorFactory.createColumnFootContentEditor());
	}

	public String getColumnName(int i) {
		return items[i].getValue(EDITOR_ID_COLUMN_NAME);
	}

	public String getFooterContent(int i) {
		return items[i].getValue(HTMLConstants.EDITOR_ID_FOOTER_CONTENT);
	}

	public String getContent(int i) {
		return items[i].getValue(EDITOR_ID_CONTENT);
	}

	@Override
	public void updateEnablement() {
		if(control != null) {
			boolean isAdvanced = HTMLConstants.TABLE_KIND_ADVANCED.equals(page.getEditorValue(HTMLConstants.EDITOR_ID_TABLE_KIND));
			boolean addHeader = TRUE.equals(page.getEditorValue(TAG_THEAD));
			boolean addFooter = TRUE.equals(page.getEditorValue(TAG_TFOOT));
			page.getEditor(EDITOR_ID_COLUMN_NAME).setEnabled(addHeader);
			page.getEditor(HTMLConstants.EDITOR_ID_FOOTER_CONTENT).setEnabled(isAdvanced && addFooter);
		}
	}
	
}
