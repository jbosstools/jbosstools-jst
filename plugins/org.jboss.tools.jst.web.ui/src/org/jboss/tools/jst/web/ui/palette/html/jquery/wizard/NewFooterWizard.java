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
		boolean is13 = JQueryMobileVersion.JQM_1_3.equals(getVersion());

		int n = page.buttons.getNumber();
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
		String barpos = page.getEditorValue(EDITOR_ID_BAR_POSITION);

		if(themeValue.length() > 0) {
			if(is13) {
				styleClass = "ui-body-" + themeValue;
			} else {
				footer.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}		
		
		if(title.length() > 0 || n == 0) {
			footer.addChild("h6", title);
		} else {
			if(barpos != null && !BAR_POSITION_DEFAULT.equals(barpos) && page.getEditor(EDITOR_ID_BAR_POSITION).isEnabled() 
					&& !isNavbar && (!isDefault || n == 1)) {
				ElementNode span = footer.addChild("span", "");
				span.addAttribute(ATTR_CLASS, "ui-title");
			}
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
				StringBuilder divCls = new StringBuilder();
				if(!is13) {
					addClass(divCls, CLASS_UI_MINI);
				}
				if(isGrouped) {
					div.addAttribute(ATTR_DATA_TYPE, DATA_TYPE_HORIZONTAL);
					if(barpos != null && !BAR_POSITION_DEFAULT.equals(barpos)) {
						addClass(divCls, CLASS_UI_BTN_PREFIX + barpos);
					}
				}
				if(divCls.length() > 0) {
					div.addAttribute(ATTR_CLASS, divCls.toString());
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
				String btnBarPos = n == 1 && isDefault ? barpos : "";
				if(is13) {
					String iconpos13 = iconpos.length() > 0 && !isNavbar && hasIcons ? iconpos : "";
					fillButton13(a, icon, iconpos13, "", btnBarPos);
				} else {
					fillButton14(a, icon, iconpos, themeValue, btnBarPos, isDefault);
				}
			}
			if(isNavbar && hasIcons && is13) {
				if(iconpos.length() == 0) iconpos = "left";
				div.addAttribute(ATTR_DATA_ICONPOS, iconpos);
			}
		}
		
		if(styleClass.length() > 0) {
			footer.addAttribute(ATTR_CLASS, styleClass);
		}
	}

	private void fillButton13(ElementNode a, String icon, String iconpos, String themeValue, String barpos) {
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
		if(barpos != null && barpos.length() > 0 && !BAR_POSITION_DEFAULT.equals(barpos)) {
			a.addAttribute(ATTR_CLASS, CLASS_UI_BTN_PREFIX + barpos);
		}
	}

	private void fillButton14(ElementNode a, String icon, String iconpos, String themeValue, String barpos, boolean noContainer) {
		StringBuilder cls = new StringBuilder();

		addClass(cls, CLASS_UI_BTN);
		if(noContainer) {
			addClass(cls, CLASS_UI_MINI);
			addClass(cls, CLASS_UI_CORNER_ALL);
		}
		
		if(icon.length() > 0) {
			addClass(cls, CLASS_UI_ICON_PREFIX + icon);
			if(iconpos.length() == 0) iconpos = "left";
			addClass(cls, CLASS_UI_BTN_ICON_PREFIX + iconpos);
		}
		if(themeValue.length() > 0) {
			addClass(cls, CLASS_UI_BTN_PREFIX + themeValue);
		}
		if(barpos != null && barpos.length() > 0 && !BAR_POSITION_DEFAULT.equals(barpos)) {
			addClass(cls, CLASS_UI_BTN_PREFIX + barpos);
		}

		a.addAttribute(ATTR_CLASS, cls.toString());
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
