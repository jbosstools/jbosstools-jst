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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class TabEditor extends ItemsEditor {

	public TabEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			setLabel(i, "Tab " + (i + 1));
			items[i].setValue(EDITOR_ID_TABS_ACTIVE, i == 0 ? TRUE : FALSE);
			items[i].setValue(EDITOR_ID_DISABLED, FALSE);
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor());
		addItemEditor(JQueryFieldEditorFactory.createTabsActiveEditor());
		addItemEditor(JQueryFieldEditorFactory.createDisabledEditor());
	}

	public String getLabel(int i) {
		return items[i].getValue(EDITOR_ID_LABEL);
	}

	public void setLabel(int i, String value) {
		items[i].setValue(EDITOR_ID_LABEL, value);
	}

	public boolean isActive(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_TABS_ACTIVE));
	}

	public boolean isDisabled(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_DISABLED));
	}

	public void onActiveModified() {
		if(selected >= 0 && isActive(selected)) {
			for (int i = 0; i < maxNumber; i++) {
				if(i != selected) items[i].setValue(EDITOR_ID_TABS_ACTIVE, FALSE);
			}
		}
	}


	public void updateEnablement() {
	}
}
