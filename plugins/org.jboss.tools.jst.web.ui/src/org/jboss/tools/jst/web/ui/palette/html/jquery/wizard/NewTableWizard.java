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

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTableWizard extends NewJQueryWidgetWizard<NewTableWizardPage> implements JQueryConstants {

	public NewTableWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TABLE_IMAGE));
	}

	protected NewTableWizardPage createPage() {
		return new NewTableWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		ElementNode table = parent.addChild(TAG_TABLE);
		table.addAttribute(ATTR_DATA_ROLE, ROLE_TABLE);

		String id = page.getEditorValue(EDITOR_ID_ID);		
		if(id.length() == 0) {
			id = "table-" + generateIndex("table-", "", 1);
		}
		table.addAttribute(ATTR_ID, id);
		
		String mode = page.getEditorValue(EDITOR_ID_MODE);
		if(MODE_COLUMNTOGGLE.equals(mode)) {
			table.addAttribute(ATTR_DATA_MODE, MODE_COLUMNTOGGLE);
		}

		StringBuilder cls = new StringBuilder();
		if(themeValue.length() > 0) {
			addClass(cls, "ui-body-" + themeValue);
		}
		if(isTrue(EDITOR_ID_RESPONSIVE)) {
			addClass(cls, CLASS_RESPONSIVE);
		}
		if(isTrue(EDITOR_ID_STRIPES)) {
			addClass(cls, CLASS_TABLE_STRIPE);
		}
		table.addAttribute(ATTR_CLASS, cls.toString());

		ElementNode thead = table.addChild(TAG_THEAD);
		ElementNode tr = thead.addChild(TAG_TR);
		if(themeValue.length() > 0) {
			tr.addAttribute(ATTR_CLASS, "ui-bar-" + themeValue);
		}
		for (int i = 0; i < page.columns.getNumber(); i++) {
			String columnName = page.columns.getColumnName(i);
			String priotity = page.columns.getPriority(i);
			ElementNode th = tr.addChild(TAG_TH, columnName);
			if(priotity.length() > 0) {
				th.addAttribute(ATTR_DATA_PRIORITY, priotity);
			}
		}

		ElementNode tbody = table.addChild(TAG_TBODY);
		tr = tbody.addChild(TAG_TR);		
		for (int i = 0; i < page.columns.getNumber(); i++) {
			String firstRowContent = page.columns.getContent(i);
			tr.addChild(TAG_TD, firstRowContent);
		}
	}

	private void addClass(StringBuilder cls, String add) {
		if(cls.length() > 0) {
			cls.append(" ");
		}
		cls.append(add);
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
