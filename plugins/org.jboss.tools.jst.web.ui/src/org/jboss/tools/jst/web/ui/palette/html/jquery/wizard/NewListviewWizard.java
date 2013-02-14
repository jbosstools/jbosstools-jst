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
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewListviewWizard extends NewJQueryWidgetWizard implements JQueryConstants {
	NewListviewWizardPage page;

	public NewListviewWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.LISTVIEW_IMAGE));
	}

	protected void doAddPages() {
		page = new NewListviewWizardPage();
		addPage(page);
	}

	boolean isTrue(String editorID) {
		return "true".equals(page.getEditorValue(editorID));
	}

	protected void addContent(ElementNode parent) {
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		String listTagName = isTrue(EDITOR_ID_NUMBERED) ? TAG_OL : TAG_UL;
		ElementNode listRoot = parent.addChild(listTagName);
		listRoot.addAttribute(ATTR_DATA_ROLE, ROLE_LISTVIEW);
		if(isTrue(EDITOR_ID_AUTODIVIDERS)) {
			listRoot.addAttribute(ATTR_DATA_AUTODIVIDERS, TRUE);
		}
		if(isTrue(EDITOR_ID_SEARCH_FILTER)) {
			listRoot.addAttribute(ATTR_DATA_FILTER, TRUE);
		}
		if(isTrue(EDITOR_ID_INSET)) {
			listRoot.addAttribute(ATTR_DATA_INSET, TRUE);
		}
		if(themeValue.length() > 0) {
			listRoot.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		for (int i = 1; i < 3; i++) {
			String text = "Item " + i;
			if(!isTrue(EDITOR_ID_READ_ONLY)) {
				ElementNode li = listRoot.addChild(TAG_LI, "");
				ElementNode a = li.addChild(TAG_A, text);
				a.addAttribute(ATTR_HREF, "item" + i + ".html");
			} else {
				listRoot.addChild(TAG_LI, text);
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
