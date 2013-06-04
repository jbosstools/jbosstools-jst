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
public class NewFooterWizard extends NewJQueryWidgetWizard<NewFooterWizardPage> implements JQueryConstants {

	public NewFooterWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.FOOTER_IMAGE));
	}

	protected NewFooterWizardPage createPage() {
		return new NewFooterWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String title = page.getEditorValue(EDITOR_ID_TITLE);
		String arragement = page.getEditorValue(EDITOR_ID_ARRAGEMENT);
		boolean isNavbar = ARRAGEMENT_NAVBAR.equals(arragement);
		boolean isGrouped = ARRAGEMENT_GROUPED.equals(arragement);
		boolean isDefault = !isNavbar && !isGrouped;
		
		ElementNode footer = parent.addChild(TAG_DIV);
		footer.addAttribute(ATTR_DATA_ROLE, ROLE_FOOTER);

		addID("footer-", footer);

		if(isTrue(EDITOR_ID_FIXED_POSITION)) {
			footer.addAttribute(ATTR_DATA_POSITION, POSITION_FIXED);
			if(isTrue(EDITOR_ID_FULL_SCREEN)) {
				footer.addAttribute(ATTR_DATA_FULL_SCREEN, TRUE);			
			}
		}

		String styleClass = "";
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		if(themeValue.length() > 0) {
			styleClass = "ui-body-" + themeValue;
		}		
		
		int n = page.buttons.getNumber();

		if(title.length() > 0 || n == 0) {
			footer.addChild("h6", title);
		}

		if(n > 0) {
			boolean hasIcons = page.buttons.hasIcons();
			if(styleClass.length() > 0) {
				styleClass += " ";
			}
			styleClass += CLASS_BAR;
			ElementNode div = footer;
			if(!isDefault) {
				div = footer.addChild(TAG_DIV);
				div.addAttribute(ATTR_DATA_ROLE, isNavbar ? ROLE_NAVBAR : ROLE_GROUP);
				if(isGrouped) {
					div.addAttribute(ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
				}
			}

			String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
			if(isTrue(EDITOR_ID_ICON_ONLY)) {
				iconpos = ICONPOS_NOTEXT;
			}

			ElementNode ul = !isNavbar ? div : div.addChild(TAG_UL);
		
			for (int i = 0; i < n; i++) {
				String label = page.buttons.getLabel(i);
				String url = page.buttons.getURL(i);
				String icon = page.buttons.getIcon(i);
				ElementNode item = !isNavbar ? div : ul.addChild(TAG_LI, "");
				ElementNode a = item.addChild(TAG_A, label);
				a.addAttribute(ATTR_HREF, url);
				if(icon.length() > 0) {
					a.addAttribute(ATTR_DATA_ICON, icon);
				}			
				if(iconpos.length() > 0 && !isNavbar && hasIcons) {
					a.addAttribute(ATTR_DATA_ICONPOS, iconpos);
				}
			}
			if(isNavbar && hasIcons) {
				if(iconpos.length() == 0) iconpos = "left";
				div.addAttribute(ATTR_DATA_ICONPOS, iconpos);
			}
		}
		
		if(styleClass.length() > 0) {
			footer.addAttribute(ATTR_CLASS, styleClass);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		ElementNode pg = div.addChild(TAG_DIV);
		pg.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		addID("page-", pg);
		ElementNode content = pg.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		content.addChild("p", "Page content goes here.");		
		addContent(pg);
	}
	
}
