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
public class NewHeaderBarWizard extends NewJQueryWidgetWizard<NewHeaderBarWizardPage> implements JQueryConstants {

	public NewHeaderBarWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.HEADER_IMAGE));
	}

	protected NewHeaderBarWizardPage createPage() {
		return new NewHeaderBarWizardPage();
	}

	protected void addContent(ElementNode parent) {
		boolean is13 = JQueryMobileVersion.JQM_1_3.equals(getVersion());
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		
		ElementNode header = parent.addChild(TAG_DIV);
		header.addAttribute(ATTR_DATA_ROLE, ROLE_HEADER);

		addID("header-", header);

		if(isTrue(EDITOR_ID_FIXED_POSITION)) {
			header.addAttribute(ATTR_DATA_POSITION, POSITION_FIXED);
			if(isTrue(EDITOR_ID_FULL_SCREEN)) {
				header.addAttribute(ATTR_DATA_FULL_SCREEN, TRUE);			
			}
		}

		if(themeValue.length() > 0) {
			header.addAttribute(ATTR_DATA_THEME, themeValue);
		}

		if(isTrue(EDITOR_ID_LEFT_BUTTON)) {
			String label = page.getEditorValue(EDITOR_ID_LEFT_BUTTON_LABEL);
			ElementNode a = header.addChild(TAG_A, label);
			a.addAttribute(ATTR_HREF, page.getEditorValue(EDITOR_ID_LEFT_BUTTON_URL));
			String icon = page.getEditorValue(EDITOR_ID_LEFT_BUTTON_ICON);
			if(is13) {
				if(icon.length() > 0) {
					a.addAttribute(ATTR_DATA_ICON, icon);
				}
			} else {
				StringBuilder cls = new StringBuilder();
				cls.append(CLASS_UI_BTN);
				addClass(cls, CLASS_UI_MINI);
				addClass(cls, CLASS_UI_BTN_INLINE);
				addClass(cls, CLASS_UI_BTN_PREFIX + "left");
				addClass(cls, CLASS_UI_CORNER_ALL);
				if(icon.length() > 0) {
					addClass(cls, CLASS_UI_ICON_PREFIX + icon);
					addClass(cls, CLASS_UI_BTN_ICON_PREFIX + "left");
				}
				a.addAttribute(ATTR_CLASS, cls.toString());
			}
		}

		String title = page.getEditorValue(EDITOR_ID_TITLE);
		if(title.length() == 0) {
			ElementNode titleNode = header.addChild("span", title);
			titleNode.addAttribute(ATTR_CLASS, "ui-title");
		} else {
			header.addChild("h1", title);		}
	
		if(isTrue(EDITOR_ID_RIGHT_BUTTON)) {
			String label = page.getEditorValue(EDITOR_ID_RIGHT_BUTTON_LABEL);
			ElementNode a = header.addChild(TAG_A, label);
			a.addAttribute(ATTR_HREF, page.getEditorValue(EDITOR_ID_RIGHT_BUTTON_URL));
			String icon = page.getEditorValue(EDITOR_ID_RIGHT_BUTTON_ICON);
			if(is13) {
				if(icon.length() > 0) {
					a.addAttribute(ATTR_DATA_ICON, icon);
				}
				if(!isTrue(EDITOR_ID_LEFT_BUTTON)) {
					a.addAttribute(ATTR_CLASS, CLASS_BUTTON_RIGHT);
				}
			} else {
				StringBuilder cls = new StringBuilder();
				cls.append(CLASS_UI_BTN);
				addClass(cls, CLASS_UI_MINI);
				addClass(cls, CLASS_UI_BTN_INLINE);
				addClass(cls, CLASS_UI_BTN_PREFIX + "right");
				addClass(cls, CLASS_UI_CORNER_ALL);
				if(icon.length() > 0) {
					addClass(cls, CLASS_UI_ICON_PREFIX + icon);
					addClass(cls, CLASS_UI_BTN_ICON_PREFIX + "left");
				}
				a.addAttribute(ATTR_CLASS, cls.toString());
			}
		}

	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		ElementNode pg = div.addChild(TAG_DIV);
		pg.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		addID("page-", pg);
		addContent(pg);
		ElementNode content = pg.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		content.addChild("p", "Page content goes here.");
	}
	
}
