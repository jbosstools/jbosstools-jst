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
public class NewRadioWizard extends NewJQueryWidgetWizard<NewRadioWizardPage> implements JQueryConstants {

	public NewRadioWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.RADIO_IMAGE));
	}

	protected NewRadioWizardPage createPage() {
		return new NewRadioWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode group = parent.addChild(TAG_FIELDSET);
		group.addAttribute(ATTR_DATA_ROLE, ROLE_GROUP);

		addID("radio-", group);

		if(LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT))) {
			group.addAttribute(ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
		} else {
			String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
			if(iconpos.length() > 0) {
				group.addAttribute(ATTR_DATA_ICONPOS, iconpos);
			}
		}

		group.addChild(TAG_LEGEND, page.getEditorValue(EDITOR_ID_LEGEND));

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		String prefixName = "radio-choice-";
		String suffixName = "";
		String name = prefixName + generateIndex(prefixName, suffixName, 1) + suffixName;
		
		for (int i = 0; i < page.buttons.getNumber(); i++) {
			String prefixID = "radio-choice-";
			String suffixID = "" + (char)('a' + i);
			String id = prefixID + generateIndex(prefixID, suffixID, 1) + suffixID;
			
			ElementNode item = group.addChild(TAG_INPUT);
			item.addAttribute(ATTR_NAME, name);
			item.addAttribute(ATTR_ID, id);
			if(isMini()) {
				item.addAttribute(ATTR_DATA_MINI, TRUE);
			}
			if(themeValue.length() > 0) {
				item.addAttribute(ATTR_DATA_THEME, themeValue);
			}
			item.addAttribute(ATTR_VALUE, page.buttons.getValue(i));
			item.addAttribute(ATTR_TYPE, TYPE_RADIO);
			if(page.buttons.isChecked(i)) {
				item.addAttribute(CHECKED, CHECKED);
			}
			ElementNode label = group.addChild(TAG_LABEL, page.buttons.getLabel(i));
			label.addAttribute(ATTR_FOR, id);			
		}

	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
