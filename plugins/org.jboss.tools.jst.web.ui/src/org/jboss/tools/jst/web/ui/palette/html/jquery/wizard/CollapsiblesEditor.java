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
public class CollapsiblesEditor extends ItemsEditor {

	public CollapsiblesEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			items[i].setValue(EDITOR_ID_HEADER, "" + (char)(65 + i));
			items[i].setValue(EDITOR_ID_COLLAPSED, TRUE);
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createCollapsibleHeaderEditor());
		addItemEditor(JQueryFieldEditorFactory.createCollapsedEditor());
	}

	public String getLabel(int i) {
		return items[i].getValue(EDITOR_ID_HEADER);
	}

	public boolean isCollapsed(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_COLLAPSED));
	}

	public void onCollapsedModified() {
		if(selected >= 0 && !isCollapsed(selected)) {
			for (int i = 0; i < maxNumber; i++) {
				if(i != selected) items[i].setValue(EDITOR_ID_COLLAPSED, TRUE);
			}
		}
	}
}
