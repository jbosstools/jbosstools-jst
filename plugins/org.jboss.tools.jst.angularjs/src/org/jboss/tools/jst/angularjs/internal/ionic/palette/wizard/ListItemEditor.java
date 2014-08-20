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

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.ItemsEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ListItemEditor extends ItemsEditor {

	public ListItemEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber);
		for (int i = 0; i < maxNumber; i++) {
			setLabel(i, "Item " + (i + 1));
			items[i].setValue(JQueryConstants.EDITOR_ID_DIVIDER, FALSE);
			items[i].setValue(JQueryConstants.ATTR_ICON, "");
			items[i].setValue(IonicConstants.EDITOR_ID_ITEM_STYLE, "");
		}
	}

	@Override
	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createLabelEditor(IonicConstants.EDITOR_ID_LIST_ITEM_LABEL));
		addItemEditor(JQueryFieldEditorFactory.createDividerEditor());
		addItemEditor(IonicFieldEditorFactory.createIconEditor(IonicConstants.ATTR_ICON));
		addItemEditor(IonicFieldEditorFactory.createItemStyleEditor());
	}

	public String getLabel(int i) {
		return items[i].getValue(IonicConstants.EDITOR_ID_LIST_ITEM_LABEL);
	}

	public void setLabel(int i, String value) {
		items[i].setValue(IonicConstants.EDITOR_ID_LIST_ITEM_LABEL, value);
	}

	public boolean isDivider(int i) {
		return TRUE.equals(items[i].getValue(JQueryConstants.EDITOR_ID_DIVIDER));
	}

	public String getIcon(int i) {
		return items[i].getValue(JQueryConstants.ATTR_ICON);
	}

	public String getStyle(int i) {
		return items[i].getValue(IonicConstants.EDITOR_ID_ITEM_STYLE);
	}

	@Override
	public boolean onPropertyChange(String editorID, String value) {
		boolean result = super.onPropertyChange(editorID, value);
		if(JQueryConstants.EDITOR_ID_DIVIDER.equals(editorID)) {
			updateEnablement();
		}
		return result;
	}

	@Override
	public void updateEnablement() {
		IFieldEditor divider = page.getEditor(JQueryConstants.EDITOR_ID_DIVIDER);
		IFieldEditor style = page.getEditor(IonicConstants.EDITOR_ID_ITEM_STYLE);
		if(divider != null && style != null) {
			boolean en = !TRUE.equals(page.getEditorValue(JQueryConstants.EDITOR_ID_DIVIDER));
			style.setEnabled(en);
		}
	}
}
