/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabsWizard extends NewJQueryWidgetWizard<NewTabsWizardPage> implements JQueryConstants {
	static String PREFIX_TABS = "tabs-";
	static String PREFIX_TAB = "tab-";

	public NewTabsWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TABS_IMAGE));
	}

	protected NewTabsWizardPage createPage() {
		return new NewTabsWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		
		ElementNode div = parent.addChild(TAG_DIV);
		div.addAttribute(ATTR_DATA_ROLE, ROLE_TABS);

		addID(PREFIX_TABS, div);

		String layout = page.getEditorValue(EDITOR_ID_TABS_LAYOUT);
		boolean isNavbar = ROLE_NAVBAR.equals(layout);

		if(!isNavbar) {
			String styleText = 
				".tablist-left {width: 35%; display: inline-block;}\n"
				+ ".tablist-content {width: 50%; display: inline-block;vertical-align: top;margin-left: 5%;}";
			ElementNode style = div.addChild(TAG_STYLE);
			style.addAttribute(ATTR_SCOPED, ATTR_SCOPED);
			style.addTextChild(styleText);
		}
		
		ElementNode ul = null;
		if(isNavbar) {
			ElementNode navbar = div.addChild(TAG_DIV);
			navbar.addAttribute(ATTR_DATA_ROLE, ROLE_NAVBAR);
			ul = navbar.addChild(TAG_UL);
		} else {
			ul = div.addChild(TAG_UL);
			ul.addAttribute(ATTR_DATA_ROLE, ROLE_LISTVIEW);
			ul.addAttribute(ATTR_DATA_INSET, TRUE);
			ul.addAttribute(ATTR_CLASS, "tablist-left");
			if(themeValue.length() > 0) {
				ul.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}

		int activeIndex = -1;
		List<Integer> disabled = new ArrayList<Integer>();
		String[] ids = new String[page.tabs.getNumber()];
		int commonTabIDIndex = getMinCommonTabIDIndex(0);
		for (int i = 0; i < ids.length; i++) {
			String suffix = "" + (char)('a' + i);
			ids[i] = PREFIX_TAB + commonTabIDIndex + suffix;
			String tabName = page.tabs.getLabel(i);
			if(page.tabs.isActive(i)) {
				activeIndex = i;
			}
			if(page.tabs.isDisabled(i)) {
				disabled.add(i);
			}
			ElementNode li = ul.addChild(TAG_LI, "");
			ElementNode a = li.addChild(TAG_A, tabName);
			a.addAttribute(ATTR_HREF, "#" + ids[i]);
			a.addAttribute(ATTR_DATA_AJAX, FALSE);
			if(isNavbar && themeValue.length() > 0) {
				a.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}
		for (int i = 0; i < ids.length; i++) {
			String tabName = page.tabs.getLabel(i);
			ElementNode div_i = div.addChild(TAG_DIV);
			div_i.addAttribute(ATTR_ID, ids[i]);
			div_i.addChild(TAG_H4, tabName);
			String cls = isNavbar ? "ui-body-d ui-content" : "ui-body-d tablist-content";
			div_i.addAttribute(ATTR_CLASS, cls);
		}

		StringBuilder options = new StringBuilder();
		boolean isCollapsible = isTrue(EDITOR_ID_TABS_COLLAPSIBLE);
		if(isCollapsible || activeIndex >= 0) {
			if(isCollapsible) {
				addOption(options, "collapsible", TRUE);
			}
			if(isCollapsible && isTrue(EDITOR_ID_COLLAPSED)) {
				addOption(options, "active", FALSE);
			} else if(activeIndex > 0) {
				addOption(options, "active", "" + activeIndex);
			}
		}
		if(MOUSEOVER.equals(page.getEditorValue(EDITOR_ID_TABS_ACTIVATION))) {
			addOption(options, "event", "\"" + MOUSEOVER + "\"");
		}
		if(isTrue(EDITOR_ID_TABS_ANIMATED)) {
			addOption(options, "show", TRUE);
			addOption(options, "hide", TRUE);
		}
		if(!disabled.isEmpty()) {
			StringBuilder d = new StringBuilder();
			for (int i = 0; i < disabled.size(); i++) {
				if(i > 0) d.append(", ");
				d.append(disabled.get(i).intValue());
			}
			addOption(options, ATTR_DISABLED, "[" + d.toString() + "]");
		}
		if(options.length() > 0) {
			ElementNode script = parent.addChild(TAG_SCRIPT);
			String scriptText = "$(\"#" + getID(PREFIX_TABS) + "\").tabs({" + options + "});";
			script.addTextChild(scriptText);
		}
	}

	int getMinCommonTabIDIndex(int start) {
		int max = 1;
		while(max > start) {
			start = max;
			for (int i = 0; i < page.tabs.number; i++) {
				String suffix = "" + (char)('a' + i);
				int m = generateIndex(PREFIX_TAB, suffix, start);
				if(m > max) max = m;
			}
		}
		return max;
	}

	private void addOption(StringBuilder options, String name, String value) {
		if(options.length() > 0) {
			options.append(", ");
		}
		options.append(name).append(": ").append(value);
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
