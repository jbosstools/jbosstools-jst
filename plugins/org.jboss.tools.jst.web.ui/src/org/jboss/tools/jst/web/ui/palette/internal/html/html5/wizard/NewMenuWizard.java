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
public class NewMenuWizard extends NewHTMLWidgetWizard<NewMenuWizardPage> implements HTMLConstants {
	static String prefix = "menu-";

	public NewMenuWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.MENU_IMAGE));
	}

	@Override
	public NewMenuWizardPage createPage() {
		return new NewMenuWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		ElementNode menu = parent.addChild(TAG_MENU);
		addID(prefix, menu);
		menu.addAttribute(ATTR_LABEL, page.getEditorValue(HTMLFieldEditorFactory.EDITOR_ID_MENU_LABEL));
		addAttributeIfNotEmpty(menu, ATTR_TYPE, HTMLFieldEditorFactory.EDITOR_ID_MENU_TYPE);

		for (int i = 0; i < page.items.getNumber(); i++) {
			ElementNode menuitem = menu.addChild(TAG_MENUITEM);
			if(TRUE.equals(page.items.getValue(i, MenuitemsEditor.EDITOR_ID_ADD_ITEM_ID))) {
				String id = page.items.getValue(i, MenuitemsEditor.EDITOR_ID_ITEM_ID);
				if(id == null || id.length() == 0) {
					id = page.items.itemIDs[i];
				}
				menuitem.addAttribute(ATTR_ID, id);
			}
			
			Map<String, String> values = new HashMap<String, String>();
			for (String name: MenuitemsEditor.PROPERTIES) {
				String value = page.items.getValue(i, name);
				if(value != null) {
					values.put(name, value);
				}
			}
			NewMenuitemWizard.fillMenuItem(menuitem, values);
		}

	}

	protected String getMenuID() {
		return super.getID(prefix);
	}
}
