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

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTableWizard extends NewHTMLWidgetWizard<NewTableWizardPage> implements HTMLConstants {

	public NewTableWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TABLE_IMAGE));
	}

	@Override
	protected NewTableWizardPage createPage() {
		return new NewTableWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		ElementNode table = parent.addChild(TAG_TABLE);

		addID("table-", table);
	
		String caption = page.getEditorValue(TAG_CAPTION);
		if(caption.length() > 0) {
			table.addChild(TAG_CAPTION, caption);
		}
		
		boolean isAdvanced = TABLE_KIND_ADVANCED.equals(page.getEditorValue(EDITOR_ID_TABLE_KIND));

		ElementNode tr = null;

		if(isTrue(TAG_THEAD)) {
			ElementNode thead = isAdvanced ? table.addChild(TAG_THEAD) : table;
			tr = thead.addChild(TAG_TR);
			for (int i = 0; i < page.columns.getNumber(); i++) {
				String columnName = page.columns.getColumnName(i);
				tr.addChild(TAG_TH, columnName);
			}
		}

		ElementNode tbody = isAdvanced ? table.addChild(TAG_TBODY) : table;
		tr = tbody.addChild(TAG_TR);		
		for (int i = 0; i < page.columns.getNumber(); i++) {
			String firstRowContent = page.columns.getContent(i);
			tr.addChild(TAG_TD, firstRowContent);
		}

		if(isAdvanced && isTrue(TAG_TFOOT)) {
			ElementNode thead = table.addChild(TAG_TFOOT);
			tr = thead.addChild(TAG_TR);
			for (int i = 0; i < page.columns.getNumber(); i++) {
				String columnName = page.columns.getFooterContent(i);
				tr.addChild(TAG_TH, columnName);
			}
		}
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
