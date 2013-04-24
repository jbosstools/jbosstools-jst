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
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard.ElementNode;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPanelWizard extends NewJQueryWidgetWizard<NewPanelWizardPage> implements JQueryConstants {
	static String prefixName = "panel-";

	public NewPanelWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.PANEL_IMAGE));
	}

	protected NewPanelWizardPage createPage() {
		return new NewPanelWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode div = parent.addChild(TAG_DIV, "");
		div.addAttribute(ATTR_DATA_ROLE, "panel");
		div.addAttribute(ATTR_ID, getId());
		if(POSITION_RIGHT.equals(page.getEditorValue(EDITOR_ID_PANEL_POSITION))) {
			div.addAttribute(ATTR_DATA_POSITION, POSITION_RIGHT);
		}

		if(isTrue(EDITOR_ID_FIXED_POSITION)) {
			div.addAttribute(ATTR_DATA_POSITION_FIXED, TRUE);
		}
		
		String display = page.getEditorValue(EDITOR_ID_DISPLAY);
		if(!DISPLAY_REVEAL.equals(display)) {
			div.addAttribute(ATTR_DATA_DISPLAY, display);
		}

		if(!isTrue(EDITOR_ID_DISMISSABLE)) {
			div.addAttribute(ATTR_DATA_DISMISSABLE, FALSE);
		}
		if(!isTrue(EDITOR_ID_SWIPE_CLOSE)) {
			div.addAttribute(ATTR_DATA_SWIPE_CLOSE, FALSE);
		}
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			div.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		div.getChildren().add(SEPARATOR);
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode page = body.addChild(TAG_DIV);
		page.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		page.addAttribute(ATTR_ID, "jbt");
		if("mozilla".equals(this.page.getBrowserType())) {
			ElementNode div = page.addChild(TAG_DIV);
			div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
			div.addChild(TAG_DIV, "Preview is not implemented for this element.");
		} else {
			addContent(page);
			ElementNode panel = page.getChildren().get(0);
			panel.addChild(TAG_P, "Panel Content");
			ElementNode content = page.addChild(TAG_DIV);
			content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
			ElementNode a = content.addChild(TAG_A, "Open Panel");
			a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
			a.addAttribute(ATTR_HREF, "#" + getId());
		}
	}

	private String getId() {
		String id = page.getEditorValue(EDITOR_ID_ID);
		if(id.length() == 0) {
			id = prefixName + generateIndex(prefixName, "", 1);
		}
		return id;
	}
	
}
