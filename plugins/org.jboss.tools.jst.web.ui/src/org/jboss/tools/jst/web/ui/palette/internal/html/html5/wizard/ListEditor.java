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
			setDefaultValues(i);
		}
	}

	protected void setDefaultValues(int i) {
		setLabel(i, "Option" + (i + 1));
		setValue(i, "Value" + (i + 1));
	}

	@Override
	protected void createItemEditors() {
		addItemEditor(HTMLFieldEditorFactory.createLabelEditor());
		addItemEditor(HTMLFieldEditorFactory.createValueEditor());
	}

	public String getLabel(int i) {
		return items[i].getValue(ATTR_LABEL);
	}

	public void setLabel(int i, String value) {
		items[i].setValue(ATTR_LABEL, value);
	}

	public String getValue(int i) {
		return items[i].getValue(ATTR_VALUE);
	}

	public void setValue(int i, String value) {
		items[i].setValue(ATTR_VALUE, value);
	}

}
