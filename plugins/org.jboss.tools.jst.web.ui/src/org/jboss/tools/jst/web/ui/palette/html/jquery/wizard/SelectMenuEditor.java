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

import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizardPage.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class SelectMenuEditor extends ItemsEditor {

	public SelectMenuEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			items[i].setValue(EDITOR_ID_OPTION, "" + (char)(65 + i));
			items[i].setValue(EDITOR_ID_VALUE, "" + (char)(65 + i));
			items[i].setValue(EDITOR_ID_SELECTED, FALSE);
			items[i].setValue(EDITOR_ID_DISABLED, FALSE);
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor(EDITOR_ID_OPTION));
		addItemEditor(JQueryFieldEditorFactory.createValueEditor());
		TwoColumns columns = NewJQueryWidgetWizardPage.createTwoColumns(control);
		page.addEditor(JQueryFieldEditorFactory.createSelectedEditor(), columns.left());
		page.addEditor(JQueryFieldEditorFactory.createDisabledEditor(), columns.right());
	}

	public String getLabel(int i) {
		return items[i].getValue(EDITOR_ID_OPTION);
	}

	public String getValue(int i) {
		return items[i].getValue(EDITOR_ID_VALUE);
	}

	public boolean isSelected(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_SELECTED));
	}

	public boolean isDisabled(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_DISABLED));
	}

	public void onSelectedModified() {
		if(selected >= 0 && isSelected(selected)) {
			for (int i = 0; i < maxNumber; i++) {
				if(i != selected) items[i].setValue(EDITOR_ID_SELECTED, FALSE);
			}
		}
	}
}
