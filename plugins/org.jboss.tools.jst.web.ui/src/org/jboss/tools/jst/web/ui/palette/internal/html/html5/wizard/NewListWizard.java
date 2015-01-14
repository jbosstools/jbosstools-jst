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
public class NewListWizard extends NewHTMLWidgetWizard<NewListWizardPage> implements HTMLConstants {
	protected static String prefix = "list-";

	public NewListWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.LIST_IMAGE));
	}

	@Override
	protected NewListWizardPage createPage() {
		return new NewListWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		String tagName = isTrue(HTMLFieldEditorFactory.EDITOR_ID_ORDERED) ? TAG_OL : TAG_UL;
		ElementNode list = parent.addChild(tagName);
		//
		addID(prefix, list);

		if(TAG_OL.equals(tagName)) {
			addAttributeIfNotEmpty(list, ATTR_TYPE, ATTR_TYPE);
			addAttributeIfNotEmpty(list, ATTR_START, ATTR_START);
			if(isTrue(ATTR_REVERSED)) {
				list.addAttribute(ATTR_REVERSED, ATTR_REVERSED);
			}
		}

		for (int i = 0; i < page.items.getNumber(); i++) {
			String text = page.items.getLabel(i);
			ElementNode li = list.addChild(TAG_LI, text);
			if(TAG_OL.equals(tagName)) {
				String value = page.items.getValue(i);
				if(value.length() > 0) {
					li.addAttribute(ATTR_VALUE, value);
				}
			}
		}
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		ElementNode div = body.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		
		addContent(div);
	}
	
}
