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
public class NewListviewWizard extends NewJQueryWidgetWizard<NewListviewWizardPage> implements JQueryConstants {

	public NewListviewWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.LISTVIEW_IMAGE));
	}

	protected NewListviewWizardPage createPage() {
		return new NewListviewWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String listTagName = isTrue(EDITOR_ID_NUMBERED) ? TAG_OL : TAG_UL;

		SearchCapability sc = new SearchCapability(parent, "searchForListview-");
		sc.addClassFilterable();

		ElementNode listRoot = parent.addChild(listTagName);
		listRoot.addAttribute(ATTR_DATA_ROLE, ROLE_LISTVIEW);

//		listRoot.addAttribute("data-filter-reveal", TRUE);

		addID("listview-", listRoot);

		if(isTrue(EDITOR_ID_AUTODIVIDERS)) {
			listRoot.addAttribute(ATTR_DATA_AUTODIVIDERS, TRUE);
		}
		sc.addDataFilter(listRoot);
		if(isTrue(EDITOR_ID_INSET)) {
			listRoot.addAttribute(ATTR_DATA_INSET, TRUE);
		}
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			listRoot.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		String dividerThemeValue = page.getEditorValue(EDITOR_ID_DIVIDER_THEME);
		if(dividerThemeValue.length() > 0) {
			listRoot.addAttribute(ATTR_DATA_DIVIDER_THEME, dividerThemeValue);
		}
		for (int i = 0; i < page.items.getNumber(); i++) {
			String text = page.items.getLabel(i);
			if(!isTrue(EDITOR_ID_READ_ONLY) && !page.items.isDivider(i)) {
				ElementNode li = listRoot.addChild(TAG_LI, "");
				ElementNode a = li.addChild(TAG_A, text);
				a.addAttribute(ATTR_HREF, page.items.getURL(i));
			} else {
				ElementNode li = listRoot.addChild(TAG_LI, text);
				if(page.items.isDivider(i)) {
					li.addAttribute(ATTR_DATA_ROLE, ROLE_DIVIDER);
				}
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
