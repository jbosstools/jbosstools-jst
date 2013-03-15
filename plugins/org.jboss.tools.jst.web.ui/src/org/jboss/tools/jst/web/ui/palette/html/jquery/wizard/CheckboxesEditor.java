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

import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CheckboxesEditor extends ItemsEditor {

	public CheckboxesEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			items[i].setValue(EDITOR_ID_LABEL, "" + (char)(65 + i));
			items[i].setValue(EDITOR_ID_CHECKED, FALSE);
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor());
		addItemEditor(JQueryFieldEditorFactory.createCheckedEditor());
	}

	public String getLabel(int i) {
		return items[i].getValue(EDITOR_ID_LABEL);
	}

	public boolean isChecked(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_CHECKED));
	}
}
