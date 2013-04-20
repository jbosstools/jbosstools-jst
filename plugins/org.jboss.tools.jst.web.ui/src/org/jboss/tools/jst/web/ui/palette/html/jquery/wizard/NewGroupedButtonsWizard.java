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
public class NewGroupedButtonsWizard extends NewJQueryWidgetWizard<NewGroupedButtonsWizardPage> implements JQueryConstants {

	public NewGroupedButtonsWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.GROUP_BUTTON_IMAGE));
	}

	protected NewGroupedButtonsWizardPage createPage() {
		return new NewGroupedButtonsWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
		if(TRUE.equals(page.getEditorValue(EDITOR_ID_ICON_ONLY))) {
			iconpos = ICONPOS_NOTEXT;
		}

		ElementNode group = parent.addChild(TAG_DIV);
		group.addAttribute(ATTR_DATA_ROLE, ROLE_GROUP);
		if(LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT))) {
			group.addAttribute(ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		} else {
		}
		if(isMini()) {
			group.addAttribute(ATTR_DATA_MINI, TRUE);
		}

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		for (int i = 0; i < page.buttons.getNumber(); i++) {
			String label = page.buttons.getLabel(i);
			String url = page.buttons.getURL(i);
			String icon = page.buttons.getIcon(i);
			ElementNode a = group.addChild(TAG_A, label);
			a.addAttribute(ATTR_HREF, url);
			a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
			if(icon.length() > 0) {
				a.addAttribute(ATTR_DATA_ICON, icon);
				if(iconpos.length() > 0) {
					a.addAttribute(ATTR_DATA_ICONPOS, iconpos);
				}
			}			
			if(themeValue.length() > 0) {
				a.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}

	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
