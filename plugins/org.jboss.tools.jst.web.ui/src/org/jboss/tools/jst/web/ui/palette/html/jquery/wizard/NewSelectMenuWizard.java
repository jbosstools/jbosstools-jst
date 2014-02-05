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
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewSelectMenuWizard extends NewJQueryWidgetWizard<NewSelectMenuWizardPage> implements JQueryConstants {

	public NewSelectMenuWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.SELECT_MENU_IMAGE));
	}

	protected NewSelectMenuWizardPage createPage() {
		return new NewSelectMenuWizardPage();
	}

	protected void addContent(ElementNode parent) {
		SearchCapability sc = new SearchCapability(parent, "searchForSelect-");

		if(LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT))) {
			ElementNode div = parent.addChild(TAG_DIV);
			if(getVersion() == JQueryMobileVersion.JQM_1_3) {
				div.addAttribute(ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
			} else {
				div.addAttribute(ATTR_CLASS, CLASS_UI_FIELD_CONTAIN);
			}
			parent = div;
		}
		
		String name = getID("select-");

		String labelText = page.getEditorValue(EDITOR_ID_LABEL);
		if(labelText.length() > 0) {
			ElementNode label = parent.addChild(TAG_LABEL, labelText);
			label.addAttribute(ATTR_FOR, name);
			if(isTrue(EDITOR_ID_HIDE_LABEL)) {
				label.addAttribute(ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
			}
		}

		ElementNode select = parent.addChild(TAG_SELECT);
		select.addAttribute(ATTR_NAME, name);
		select.addAttribute(ATTR_ID, name);
		sc.addDataFilter(select);
		if(isMini()) {
			select.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		if(isTrue(EDITOR_ID_INLINE)) {
			select.addAttribute(ATTR_DATA_INLINE, TRUE);
		}
		if(!isTrue(EDITOR_ID_CORNERS)) {
			select.addAttribute(ATTR_DATA_CORNERS, FALSE);
		}

		String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
		if(iconpos.length() > 0) {
			select.addAttribute(ATTR_DATA_ICONPOS, iconpos);
		}

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			select.addAttribute(ATTR_DATA_THEME, themeValue);
		}

		for (int i = 0; i < page.items.getNumber(); i++) {
			ElementNode item = select.addChild(TAG_OPTION, page.items.getLabel(i));
			item.addAttribute(ATTR_VALUE, page.items.getValue(i));
			if(page.items.isSelected(i)) {
				item.addAttribute(SELECTED, SELECTED);
			}
			if(page.items.isDisabled(i)) {
				item.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
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
