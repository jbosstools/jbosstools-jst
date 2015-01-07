/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewMenuitemWizard extends NewHTMLWidgetWizard<NewMenuitemWizardPage> implements HTMLConstants {
	static String prefix = "menuitem-";

	public NewMenuitemWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.MENUITEM_IMAGE));
	}

	@Override
	public NewMenuitemWizardPage createPage() {
		return new NewMenuitemWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		ElementNode menuitem = parent.addChild(TAG_MENUITEM);
		addID(prefix, menuitem);
		addAttributeIfNotEmpty(menuitem, ATTR_TYPE, ATTR_TYPE);
		menuitem.addAttribute(ATTR_LABEL, page.getEditorValue(ATTR_LABEL));
		if(isTrue(ATTR_DEFAULT)) {
			menuitem.addAttribute(ATTR_DEFAULT, ATTR_DEFAULT);
		}
		if(isTrue(ATTR_DISABLED)) {
			menuitem.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(!MENUITEM_TYPE_COMMAND.equals(page.getEditorValue(ATTR_TYPE)) && isTrue(CHECKED)) {
			menuitem.addAttribute(CHECKED, CHECKED);
		}
		addAttributeIfNotEmpty(menuitem, ATTR_ICON, ATTR_ICON);
		if(MENUITEM_TYPE_RADIO.equals(page.getEditorValue(ATTR_TYPE))) {
			addAttributeIfNotEmpty(menuitem, ATTR_RADIOGROUP, ATTR_RADIOGROUP);
		}
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		StringBuilder sb = new StringBuilder();
		sb.append("var event = new CustomEvent('show',{detail: {}, bubbles: false, cancelable: true});");
		sb.append("document.getElementById('mymenu').dispatchEvent(event);");
		body.addAttribute("onload", sb.toString());
		
		body.addAttribute("contextmenu", "mymenu");
		
		ElementNode menu = body.addChild(TAG_MENU);
		menu.addAttribute(ATTR_ID, "mymenu");
		menu.addAttribute(ATTR_LABEL, "My Menu");
		menu.addAttribute(ATTR_TYPE, "context");
		addContent(menu);
	}
	
}
