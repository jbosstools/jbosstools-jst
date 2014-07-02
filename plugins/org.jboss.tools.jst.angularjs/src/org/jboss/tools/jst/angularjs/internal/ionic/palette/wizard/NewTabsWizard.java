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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabsWizard extends NewIonicWidgetWizard<NewTabsWizardPage> {

	public NewTabsWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.TABS_IMAGE));
	}

	@Override
	protected NewTabsWizardPage createPage() {
		return new NewTabsWizardPage();
	}

	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode tabs = parent.addChild(TAG_ION_TABS);
		addAttributeIfNotEmpty(tabs, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);
		addID("tabs-", tabs);

		StringBuilder cls = new StringBuilder();
		String color = page.getEditorValue(EDITOR_ID_TABS_COLOR);
		if(color.length() > 0) {
			addClass(cls, color);
		}
		String position = page.getEditorValue(JQueryConstants.EDITOR_ID_ICON_POS);
		if(position.length() > 0) {
			addClass(cls, "tabs-icon-" + position);
		}
		if(isTrue(CLASS_TABS_ITEM_HIDE)) {
			addClass(cls, CLASS_TABS_ITEM_HIDE);
		}
		if(cls.length() > 0) {
			tabs.addAttribute(ATTR_CLASS, cls.toString());
		}

		int n = page.tabs.getNumber();
		for (int i = 0; i < n; i++) {
			String tabName = page.tabs.getTitle(i);
			String text = (browser) ? "Content of " + tabName : null;
			ElementNode tab = tabs.addChild(TAG_ION_TAB, text);
			if(!browser) {
				tab.addComment("Tab " + (i + 1) + " content");
			}
			tab.addAttribute(ATTR_TITLE, tabName);
			String url = page.tabs.getURL(i);
			if(url.length() > 0) {
				tab.addAttribute(ATTR_HREF, url);
			}
			String icon = page.tabs.getIcon(i);
			if(icon.length() > 0) {
				tab.addAttribute(IonicConstants.ATTR_ICON, icon);
			}
		}
		
	}
}
