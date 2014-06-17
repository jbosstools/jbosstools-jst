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
public class ButtonsEditor extends ItemsEditor {

	public ButtonsEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			setLabel(i, "" + (char)(65 + i));
			items[i].setValue(EDITOR_ID_URL, "#");
			setIcon(i, "");
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor());
		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addItemEditor(url);
		if(page.getLeftPanel() != null) {
			new IDContentProposalProvider(page.getWizard().getIDs(), url);
		}
		addItemEditor(JQueryFieldEditorFactory.createIconEditor(((NewJQueryWidgetWizardPage)page).getVersion()));
	}

	public String getLabel(int i) {
		return items[i].getValue(EDITOR_ID_LABEL);
	}

	public void setLabel(int i, String value) {
		items[i].setValue(EDITOR_ID_LABEL, value);
	}

	public String getURL(int i) {
		return items[i].getValue(EDITOR_ID_URL);
	}

	public String getIcon(int i) {
		return items[i].getValue(EDITOR_ID_ICON);
	}

	public void setIcon(int i, String value) {
		items[i].setValue(EDITOR_ID_ICON, value);
	}

	public boolean hasIcons() {
		for (int i = 0; i < getNumber(); i++) {
			if(getIcon(i).length() > 0) {
				return true;
			}
		}
		return false;
	}
}
