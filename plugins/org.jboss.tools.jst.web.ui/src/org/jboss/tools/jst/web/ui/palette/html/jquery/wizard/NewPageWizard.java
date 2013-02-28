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
public class NewPageWizard extends NewJQueryWidgetWizard<NewPageWizardPage> implements JQueryConstants {

	public NewPageWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.PAGE_IMAGE));
	}

	protected NewPageWizardPage createPage() {
		return new NewPageWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String id = page.getEditorValue(EDITOR_ID_ID);
		
		if(id.length() == 0) {
			id = "page-" + generateIndex("page-", "", 1);
		}

		ElementNode pg = parent.addChild(TAG_DIV, page.getEditorValue(EDITOR_ID_LABEL));
		pg.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		pg.addAttribute(ATTR_ID, id);
		
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		if(isTrue(EDITOR_ID_ADD_HEADER)) {
			ElementNode header = pg.addChild(TAG_DIV);
			header.addAttribute(ATTR_DATA_ROLE, ROLE_HEADER);
			header.addChild("h1", page.getEditorValue(EDITOR_ID_HEADER_TITLE));
			if(themeValue.length() > 0) {
				header.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}

		ElementNode content = pg.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		content.addChild("p", "Page content goes here.");

		if(isTrue(EDITOR_ID_ADD_FOOTER)) {
			ElementNode footer = pg.addChild(TAG_DIV);
			footer.addAttribute(ATTR_DATA_ROLE, ROLE_FOOTER);
			footer.addChild("h4", page.getEditorValue(EDITOR_ID_FOOTER_TITLE));
			if(themeValue.length() > 0) {
				footer.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}
		
		if(themeValue.length() > 0) {
			pg.addAttribute(ATTR_DATA_THEME, themeValue);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		addContent(body);
	}
	
}
