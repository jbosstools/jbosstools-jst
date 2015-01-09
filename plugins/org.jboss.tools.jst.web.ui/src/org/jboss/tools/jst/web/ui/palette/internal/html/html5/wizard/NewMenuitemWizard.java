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

import java.util.HashMap;
import java.util.Map;

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
		Map<String, String> values = new HashMap<String, String>();
		for (String name: MenuitemsEditor.DIALOG_PROPERTIES) {
			values.put(name, page.getEditorValue(name));
		}
		fillMenuItem(menuitem, values);
	}

	static void fillMenuItem(ElementNode menuitem, Map<String, String> values) {
		String type = values.get(ATTR_TYPE);
		if(type != null && type.length() > 0) {
			menuitem.addAttribute(ATTR_TYPE, type);
		}
		String label = values.get(ATTR_LABEL);
		if(label != null) {
			menuitem.addAttribute(ATTR_LABEL, label);
		}
		if(TRUE.equals(values.get(ATTR_DEFAULT))) {
			menuitem.addAttribute(ATTR_DEFAULT, ATTR_DEFAULT);
		}
		if(TRUE.equals(values.get(ATTR_DISABLED))) {
			menuitem.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(!MENUITEM_TYPE_COMMAND.equals(type) && TRUE.equals(values.get(CHECKED))) {
			menuitem.addAttribute(CHECKED, CHECKED);
		}
		String icon = values.get(ATTR_ICON);
		if(icon != null && icon.length() > 0) {
			menuitem.addAttribute(ATTR_ICON, icon);
		}
		if(MENUITEM_TYPE_RADIO.equals(type)) {
			String radiogroup = values.get(ATTR_RADIOGROUP);
			if(radiogroup != null && radiogroup.length() > 0) {
				menuitem.addAttribute(ATTR_RADIOGROUP, radiogroup);
			}
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
