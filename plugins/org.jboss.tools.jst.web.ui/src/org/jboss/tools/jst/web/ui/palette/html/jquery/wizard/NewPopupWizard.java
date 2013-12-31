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
public class NewPopupWizard extends NewJQueryWidgetWizard<NewPopupWizardPage> implements JQueryConstants {
	static String prefixId = "popup-";

	public NewPopupWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.POPUP_IMAGE));
	}

	protected NewPopupWizardPage createPage() {
		return new NewPopupWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String id = getID(prefixId);
		
		boolean is13 = getVersion() == JQueryMobileVersion.JQM_1_3;

		if(isTrue(EDITOR_ID_POPUP_BUTTON)) {
			String label = page.getEditorValue(EDITOR_ID_LABEL);
			ElementNode a = parent.addChild(TAG_A, label);
			a.addAttribute(ATTR_HREF, "#" + id);
			if(is13) {
				a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
				a.addAttribute(ATTR_DATA_INLINE, TRUE);
			}
			String transition = page.getEditorValue(EDITOR_ID_TRANSITION);
			if(transition.length() > 0) {
				a.addAttribute(ATTR_DATA_TRANSITION, transition);
			}
			String positionTo = page.getEditorValue(EDITOR_ID_POSITION_TO);
			if(positionTo.length() > 0) {
				a.addAttribute(ATTR_DATA_POSITION_TO, positionTo);
			}
			a.addAttribute(ATTR_DATA_REL, DATA_REL_POPUP);
			if(isTrue(EDITOR_ID_INFO_STYLED) && is13) {
				a.addAttribute(ATTR_DATA_ICON, "info");
				a.addAttribute(ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);
				a.addAttribute(ATTR_DATA_THEME, "e");
//				a.addAttribute(ATTR_CLASS, "ui-icon-alt");
			}
			if(!is13) {
				StringBuilder cls = new StringBuilder();
				cls.append(CLASS_UI_BTN);
				cls.append(' ').append(CLASS_UI_CORNER_ALL);
				cls.append(' ').append(CLASS_UI_ALT_ICON);
				cls.append(' ').append(CLASS_UI_SHADOW);
				cls.append(' ').append(CLASS_UI_BTN_INLINE);
				if(isTrue(EDITOR_ID_INFO_STYLED)) {
					cls.append(' ').append(CLASS_UI_ICON_PREFIX + "info");
					cls.append(' ').append(CLASS_UI_BTN_ICON_NOTEXT);
					// theme?
				}
				a.addAttribute(ATTR_CLASS, cls.toString());
				a.addAttribute("title", label);
			}
		}
			
		ElementNode div = parent.addChild(TAG_DIV);
		div.addAttribute(ATTR_DATA_ROLE, ROLE_POPUP);
		div.addAttribute(ATTR_ID, id);
		if(!is13) {
			if(isTrue(ATTR_DATA_ARROW)) {
				div.addAttribute(ATTR_DATA_ARROW, TRUE);
			}
		}
		addAttributeIfNotEmpty(div, ATTR_DATA_THEME, EDITOR_ID_THEME);
		if(!isTrue(EDITOR_ID_CORNERS)) {
			div.addAttribute(ATTR_DATA_CORNERS, FALSE);
		}
		if(!isTrue(EDITOR_ID_DISMISSABLE)) {
			div.addAttribute(ATTR_DATA_DISMISSABLE, FALSE);
		}
		if(!isTrue(EDITOR_ID_SHADOW)) {
			div.addAttribute(ATTR_DATA_SHADOW, FALSE);
		}
		if(isTrue(EDITOR_ID_PADDING)) {
			div.addAttribute(ATTR_CLASS, CLASS_CONTENT);
		}
		addAttributeIfNotEmpty(div, ATTR_DATA_OVERLAY_THEME, EDITOR_ID_OVERLAY);

		String dataClose = page.getEditorValue(EDITOR_ID_CLOSE_BUTTON);
		if(dataClose.length() > 0 && !CLOSE_NONE.equals(dataClose)) {
			ElementNode ac = div.addChild(TAG_A, "Close");
			ac.addAttribute(ATTR_HREF, "#");
			ac.addAttribute(ATTR_DATA_REL, DATA_REL_BACK);
			if(is13) {
				ac.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
				ac.addAttribute(ATTR_DATA_THEME, "a");
				ac.addAttribute(ATTR_DATA_ICON, "delete");
				ac.addAttribute(ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);
				if(dataClose.equals(CLOSE_RIGHT)) {
					ac.addAttribute(ATTR_CLASS, CLASS_BUTTON_RIGHT);
				} else {
					ac.addAttribute(ATTR_CLASS, CLASS_BUTTON_LEFT);
				}
			} else {
				StringBuilder cls = new StringBuilder();
				cls.append(CLASS_UI_BTN);
				cls.append(' ').append(CLASS_UI_CORNER_ALL);
				cls.append(' ').append(CLASS_UI_SHADOW);
				cls.append(' ').append(CLASS_UI_BTN_INLINE);
				cls.append(' ').append(CLASS_UI_BTN_PREFIX + "a");
				cls.append(' ').append(CLASS_UI_ICON_PREFIX + "delete");
				cls.append(' ').append(CLASS_UI_BTN_ICON_NOTEXT);
				if(dataClose.equals(CLOSE_RIGHT)) {
					cls.append(' ').append(CLASS_BUTTON_RIGHT);
				} else {
					cls.append(' ').append(CLASS_BUTTON_LEFT);
				}
				ac.addAttribute(ATTR_CLASS, cls.toString());
			}
		}

		div.addChild(TAG_P, "This is a popup");
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode page = getPageContentNode(body);
		ElementNode div = page.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
