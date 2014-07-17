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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.ItemsEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
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
			setTitle(i, "Tab " + (i + 1));
			items[i].setValue(EDITOR_ID_URL, "");
			items[i].setValue(ATTR_ICON, "");
		}
	}

	//TODO move up
	protected boolean isSwitching() {
		return isSwitching;
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createTitleEditor(IonicWizardMessages.tabTitleDescription));
		addItemEditor(JQueryFieldEditorFactory.createURLEditor());
		addItemEditor(IonicFieldEditorFactory.createIconEditor(ATTR_ICON));
	}

	public String getTitle(int i) {
		return items[i].getValue(EDITOR_ID_TITLE);
	}

	public void setTitle(int i, String value) {
		items[i].setValue(EDITOR_ID_TITLE, value);
	}

	public String getURL(int i) {
		return items[i].getValue(EDITOR_ID_URL);
	}

	public String getIcon(int i) {
		return items[i].getValue(ATTR_ICON);
	}

//	public void onActiveModified() {
//		if(selected >= 0 && isActive(selected)) {
//			for (int i = 0; i < maxNumber; i++) {
//				if(i != selected) items[i].setValue(EDITOR_ID_TABS_ACTIVE, FALSE);
//			}
//		}
//	}


	public void updateEnablement() {
	}
}
