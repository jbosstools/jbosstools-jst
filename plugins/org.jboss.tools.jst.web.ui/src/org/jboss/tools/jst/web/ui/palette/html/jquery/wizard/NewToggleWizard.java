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
		String name = getID(prefixName);
		boolean is13 = getVersion() == JQueryMobileVersion.JQM_1_3;
		if(LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT))) {
			ElementNode div = parent.addChild(TAG_DIV);
			if(is13) {
				div.addAttribute(ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
			} else {
				div.addAttribute(ATTR_CLASS, CLASS_UI_FIELD_CONTAIN);
			}
			parent = div;
		}
		String id = name;
		ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
		label.addAttribute(ATTR_FOR, id);
		if(isTrue(EDITOR_ID_HIDE_LABEL)) {
			label.addAttribute(ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
		}
		boolean isSelect = is13 || TOGGLE_KIND_SELECT.equals(page.getEditorValue(EDITOR_ID_TOGGLE_KIND));
		ElementNode toggle = parent.addChild(isSelect ? TAG_SELECT : TAG_INPUT);
		toggle.addAttribute(ATTR_NAME, name);
		toggle.addAttribute(ATTR_ID, id);
		toggle.addAttribute(ATTR_DATA_ROLE, is13 ? ROLE_SLIDER : ROLE_FLIPSWITCH);
		if(isTrue(EDITOR_ID_DISABLED)) {
			toggle.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(isMini()) {
			toggle.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		String off = page.getEditorValue(EDITOR_ID_OFF);
		String on = page.getEditorValue(EDITOR_ID_ON);
		if(isSelect) {
			ElementNode optionOff = toggle.addChild(TAG_OPTION, off);
			optionOff.addAttribute(ATTR_VALUE, "off");
			ElementNode optionOn = toggle.addChild(TAG_OPTION, on);
			if(isTrue(EDITOR_ID_SELECTED)) {
				optionOn.addAttribute(SELECTED, SELECTED);
			}
			optionOn.addAttribute(ATTR_VALUE, "on");
		} else {
			toggle.addAttribute(ATTR_TYPE, TYPE_CHECKBOX);
			if(!"Off".equals(off)) {
				toggle.addAttribute(ATTR_DATA_OFF_TEXT, off);
			}
			if(!"On".equals(on)) {
				toggle.addAttribute(ATTR_DATA_ON_TEXT, on);
			}
			if(isTrue(EDITOR_ID_SELECTED)) {
				toggle.addAttribute(CHECKED, CHECKED);
			}
		}
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			toggle.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		if(is13) {
			String trackThemeValue = page.getEditorValue(EDITOR_ID_TRACK_THEME);
			if(trackThemeValue.length() > 0) {
				toggle.addAttribute(ATTR_DATA_TRACK_THEME, trackThemeValue);
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
