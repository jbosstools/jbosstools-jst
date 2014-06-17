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
public class ListEditor extends ItemsEditor {

	public ListEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			setLabel(i, "Item " + (i + 1));
			setURL(i, "item" + (i + 1) + ".html");
			items[i].setValue(EDITOR_ID_DIVIDER, FALSE);
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor());
		addItemEditor(JQueryFieldEditorFactory.createDividerEditor());
		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addItemEditor(url);
		if(page.getLeftPanel() != null) {
			new IDContentProposalProvider(page.getWizard().getIDs(), url);
		}
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

	public void setURL(int i, String value) {
		items[i].setValue(EDITOR_ID_URL, value);
	}

	public boolean isDivider(int i) {
		return TRUE.equals(items[i].getValue(EDITOR_ID_DIVIDER));
	}

	public void updateEnablement() {
		IFieldEditor read = page.getEditor(EDITOR_ID_READ_ONLY);
		IFieldEditor url = page.getEditor(EDITOR_ID_URL);
		IFieldEditor div = page.getEditor(EDITOR_ID_DIVIDER);
		if(read != null && url != null && div != null) {
			boolean en = !TRUE.equals(page.getEditorValue(EDITOR_ID_READ_ONLY)) && !TRUE.equals(page.getEditorValue(EDITOR_ID_DIVIDER));
			url.setEnabled(en);
		}		
	}
}
