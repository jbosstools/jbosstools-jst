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
public class NewCollapsibleSetWizard extends NewJQueryWidgetWizard<NewCollapsibleSetWizardPage> implements JQueryConstants {

	public NewCollapsibleSetWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.COLLAPSIBLE_SET_IMAGE));
	}

	protected NewCollapsibleSetWizardPage createPage() {
		return new NewCollapsibleSetWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode group = parent.addChild(TAG_DIV);
		group.addAttribute(ATTR_DATA_ROLE, ROLE_COLLAPSIBLE_SET);

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			group.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		themeValue = page.getEditorValue(EDITOR_ID_CONTENT_THEME);
		if(themeValue.length() > 0) {
			group.addAttribute(ATTR_DATA_CONTENT_THEME, themeValue);
		}
		String icon = page.getEditorValue(EDITOR_ID_COLLAPSED_ICON);
		if(icon.length() > 0) {
			group.addAttribute(ATTR_DATA_COLLAPSED_ICON, icon);
		}
		icon = page.getEditorValue(EDITOR_ID_EXPANDED_ICON);
		if(icon.length() > 0) {
			group.addAttribute(ATTR_DATA_EXPANDED_ICON, icon);
		}
		String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
		if(iconpos.length() > 0) {
			group.addAttribute(ATTR_DATA_ICONPOS, iconpos);
		}

		if(isMini()) {
			group.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		if(!isTrue(EDITOR_ID_INSET)) {
			group.addAttribute(ATTR_DATA_INSET, FALSE);
		}

		for (int i = 0; i < page.items.getNumber(); i++) {
			ElementNode item = group.addChild(TAG_DIV);
			item.addAttribute(ATTR_DATA_ROLE, ROLE_COLLAPSIBLE);
			if(!page.items.isCollapsed(i)) {
				item.addAttribute(ATTR_DATA_COLLAPSED, FALSE);
			}
			item.addChild(TAG_H4, page.items.getLabel(i));
			item.addChild(TAG_P, "Collapsible content.");
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
