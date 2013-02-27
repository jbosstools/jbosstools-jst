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
public class NewToggleWizard extends NewJQueryWidgetWizard<NewToggleWizardPage> implements JQueryConstants {
	static String prefixName = "flip-";
	static String prefixMiniId = "flip-min";

	public NewToggleWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TOGGLE_IMAGE));
	}

	protected NewToggleWizardPage createPage() {
		return new NewToggleWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String name = page.getEditorValue(EDITOR_ID_ID);
		if(name == null || name.length() == 0) {
			name = prefixName + generateIndex(prefixName, "", 1);
		}
		if(LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT))) {
			ElementNode div = parent.addChild(TAG_DIV);
			div.addAttribute(ATTR_DATA_ROLE, "fieldcontain");
			parent = div;
		}
		String id = name;
		ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
		label.addAttribute(ATTR_FOR, id);
		if(isTrue(EDITOR_ID_HIDE_LABEL)) {
			label.addAttribute(ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
		}
		ElementNode select = parent.addChild(TAG_SELECT);
		select.addAttribute(ATTR_NAME, name);
		select.addAttribute(ATTR_ID, id);
		select.addAttribute(ATTR_DATA_ROLE, ROLE_SLIDER);
		if(isTrue(EDITOR_ID_DISABLED)) {
			select.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(isMini()) {
			select.addAttribute(ATTR_DATA_MINI, "true");
		}
		ElementNode optionOff = select.addChild(TAG_OPTION, page.getEditorValue(EDITOR_ID_OFF));
		optionOff.addAttribute(ATTR_VALUE, "off");
		ElementNode optionOn = select.addChild(TAG_OPTION, page.getEditorValue(EDITOR_ID_ON));
		optionOn.addAttribute(ATTR_VALUE, "on");
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			select.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		String trackThemeValue = page.getEditorValue(EDITOR_ID_TRACK_THEME);
		if(trackThemeValue.length() > 0) {
			select.addAttribute(ATTR_DATA_TRACK_THEME, trackThemeValue);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 50px 20px 50px 20px;");
		addContent(div);
	}
	
}
