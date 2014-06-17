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
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
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
		ElementNode pg = parent.addChild(TAG_DIV);
		pg.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		addID("page-", pg);
		
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		if(isTrue(EDITOR_ID_ADD_HEADER)) {
			ElementNode header = pg.addChild(TAG_DIV);
			header.addAttribute(ATTR_DATA_ROLE, ROLE_HEADER);
			if(isTrue(EDITOR_ID_BACK_BUTTON)) {
//				pg.addAttribute(ATTR_DATA_ADD_BACK_BUTTON, TRUE);
			}
			addBackButton(header);
			header.addChild("h1", page.getEditorValue(EDITOR_ID_HEADER_TITLE));
			if(themeValue.length() > 0) {
				header.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}

		ElementNode content = pg.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		content.addChild("p", "Page content goes here.");
		if(!isTrue(EDITOR_ID_ADD_HEADER)) {
			addBackButton(content);
		}
		content.getChildren().add(SEPARATOR);

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

	void addBackButton(ElementNode parent) {
		if(isTrue(EDITOR_ID_BACK_BUTTON)) {
			ElementNode a = parent.addChild(TAG_A, page.getEditorValue(EDITOR_ID_LABEL));
			a.addAttribute(ATTR_HREF, "#");
			a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
			a.addAttribute(ATTR_DATA_REL, DATA_REL_BACK);
			String icon = page.getEditorValue(EDITOR_ID_ICON);
			if(icon.length() > 0) {
				a.addAttribute(ATTR_DATA_ICON, icon);
			}
			if(isTrue(EDITOR_ID_ICON_ONLY)) {
				a.addAttribute(ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);
			}				
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		addContent(body);
	}
	
}
