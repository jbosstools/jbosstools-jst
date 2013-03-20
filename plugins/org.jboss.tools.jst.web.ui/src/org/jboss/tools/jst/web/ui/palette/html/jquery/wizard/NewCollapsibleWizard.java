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
public class NewCollapsibleWizard extends NewJQueryWidgetWizard<NewCollapsibleWizardPage> implements JQueryConstants {

	public NewCollapsibleWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.COLLAPSIBLE_IMAGE));
	}

	protected NewCollapsibleWizardPage createPage() {
		return new NewCollapsibleWizardPage();
	}

	protected void addContent(ElementNode parent) {
		boolean isFieldSet = isTrue(EDITOR_ID_FIELD_SET);

		ElementNode root = parent.addChild(!isFieldSet ? TAG_DIV : TAG_FIELDSET);
		root.addAttribute(ATTR_DATA_ROLE, ROLE_COLLAPSIBLE);
		
		root.addChild(!isFieldSet ? TAG_H4 : TAG_LEGEND, page.getEditorValue(EDITOR_ID_HEADER));
		root.addChild(TAG_P, "Collapsible content.");
		root.getChildren().add(SEPARATOR);
		
		
		if(!isTrue(EDITOR_ID_COLLAPSED)) {
			root.addAttribute(ATTR_DATA_COLLAPSED, FALSE);
		}

		String icon = page.getEditorValue(EDITOR_ID_COLLAPSED_ICON);
		if(icon.length() > 0) {
			root.addAttribute(ATTR_DATA_COLLAPSED_ICON, icon);
		}
		icon = page.getEditorValue(EDITOR_ID_EXPANDED_ICON);
		if(icon.length() > 0) {
			root.addAttribute(ATTR_DATA_EXPANDED_ICON, icon);
		}
		String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
		if(iconpos.length() > 0) {
			root.addAttribute(ATTR_DATA_ICONPOS, iconpos);
		}
		if(isMini()) {
			root.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		if(!TRUE.equals(page.getEditorValue(EDITOR_ID_INSET))) {
			root.addAttribute(ATTR_DATA_INSET, FALSE);
		}
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			root.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		themeValue = page.getEditorValue(EDITOR_ID_CONTENT_THEME);
		if(themeValue.length() > 0) {
			root.addAttribute(ATTR_DATA_CONTENT_THEME, themeValue);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
