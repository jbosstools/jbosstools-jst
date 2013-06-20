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
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewNavbarWizard extends NewJQueryWidgetWizard<NewNavbarWizardPage> implements JQueryConstants {

	public NewNavbarWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.NAVBAR_IMAGE));
	}

	protected NewNavbarWizardPage createPage() {
		return new NewNavbarWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);

		if(themeValue.length() > 0) {
			ElementNode div = parent.addChild(TAG_DIV);
			div.addAttribute(ATTR_CLASS, "ui-body-" + themeValue + " ui-body");
			parent = div;
		}		
		
		ElementNode navbar = parent.addChild(TAG_DIV);
		navbar.addAttribute(ATTR_DATA_ROLE, ROLE_NAVBAR);

		addID("navbar-", navbar);

		String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
		if(iconpos.length() > 0) {
			navbar.addAttribute(ATTR_DATA_ICONPOS, iconpos);
		}

		ElementNode ul = navbar.addChild(TAG_UL);
		
		for (int i = 0; i < page.buttons.getNumber(); i++) {
			String label = page.buttons.getLabel(i);
			String url = page.buttons.getURL(i);
			String icon = page.buttons.getIcon(i);
			ElementNode item = ul.addChild(TAG_LI, "");
			ElementNode a = item.addChild(TAG_A, label);
			a.addAttribute(ATTR_HREF, url);
			if(icon.length() > 0) {
				a.addAttribute(ATTR_DATA_ICON, icon);
			}			
		}

	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
