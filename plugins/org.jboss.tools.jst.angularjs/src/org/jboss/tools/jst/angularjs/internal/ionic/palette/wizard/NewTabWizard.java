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
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabWizard extends NewIonicWidgetWizard<NewTabWizardPage> implements IonicConstants {

	public NewTabWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.HEADER_IMAGE));
	}

	@Override
	protected NewTabWizardPage createPage() {
		return new NewTabWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		if(browser) {
			ElementNode tabs = parent.addChild(TAG_ION_TABS);
			tabs.addAttribute(ATTR_CLASS, "tabs-icon-left");
			parent = tabs;
		}
		String text = (browser) ? "Content of tab" : null;
		ElementNode tab = parent.addChild(TAG_ION_TAB, text);
		if(!browser) {
			tab.addComment("Tab content");
		}

		addAttributeIfNotEmpty(tab, ATTR_TITLE, ATTR_TITLE);
		addAttributeIfNotEmpty(tab, ATTR_HREF, JQueryConstants.EDITOR_ID_URL);
		addAttributeIfNotEmpty(tab, JQueryConstants.ATTR_ICON, JQueryConstants.ATTR_ICON);
		addAttributeIfNotEmpty(tab, ATTR_ICON_ON, ATTR_ICON_ON);
		addAttributeIfNotEmpty(tab, ATTR_ICON_OFF, ATTR_ICON_OFF);
		addAttributeIfNotEmpty(tab, ATTR_BADGE, ATTR_BADGE);
		addAttributeIfNotEmpty(tab, ATTR_BADGE_STYLE, ATTR_BADGE_STYLE);
		addAttributeIfNotEmpty(tab, ATTR_ON_SELECT, ATTR_ON_SELECT);
		addAttributeIfNotEmpty(tab, ATTR_ON_DESELECT, ATTR_ON_DESELECT);
		addAttributeIfNotEmpty(tab, ATTR_NG_CLICK, ATTR_NG_CLICK);
		
		addID("tab-", tab);
		tab.getChildren().add(SEPARATOR);
	}

}