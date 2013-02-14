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
public class NewCheckBoxWizard extends NewJQueryWidgetWizard implements JQueryConstants {
	static String prefixName = "checkbox-";
	static String prefixMiniId = "checkbox-mini-";
	NewCheckBoxWizardPage page;

	public NewCheckBoxWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.createImageDescriptor(JSTWebUIImages.CHECKBOX_IMAGE));
	}

	protected void doAddPages() {
		page = new NewCheckBoxWizardPage();
		addPage(page);
	}

	boolean isMini() {
		return "true".equals(page.getEditorValue(EDITOR_ID_MINI));
	}

	protected void addContent(ElementNode parent) {
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(isMini()) {
			String name = prefixName + generateIndex(prefixName, "", 1);
			String id = prefixMiniId + generateIndex(prefixMiniId, "", 1);
			ElementNode input = parent.addChild(TAG_INPUT);
			input.addAttribute(ATTR_TYPE, TYPE_CHECKBOX);
			input.addAttribute(ATTR_NAME, name);
			input.addAttribute(ATTR_ID, id);
			input.addAttribute(ATTR_CLASS, CLASS_CUSTOM);
			input.addAttribute(ATTR_DATA_MINI, "true");
			if(themeValue.length() > 0) {
				input.addAttribute(ATTR_DATA_THEME, themeValue);
			}
			ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
			label.addAttribute(ATTR_FOR, id);
		} else {
			ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
			String name = prefixName + generateIndex(prefixName, "", 1);
			ElementNode input = label.addChild(TAG_INPUT);
			input.addAttribute(ATTR_TYPE, TYPE_CHECKBOX);
			input.addAttribute(ATTR_NAME, name);
			if(themeValue.length() > 0) {
				input.addAttribute(ATTR_DATA_THEME, themeValue);
			}
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 50px 20px 50px 20px;");
		addContent(div);
	}
	
}
