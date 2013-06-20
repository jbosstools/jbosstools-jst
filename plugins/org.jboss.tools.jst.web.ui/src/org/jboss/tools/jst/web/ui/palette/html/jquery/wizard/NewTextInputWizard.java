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
public class NewTextInputWizard extends NewJQueryWidgetWizard<NewTextInputWizardPage> implements JQueryConstants {

	public NewTextInputWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TEXT_INPUT_IMAGE));
	}

	protected NewTextInputWizardPage createPage() {
		return new NewTextInputWizardPage();
	}

	protected boolean isRange() {
		return isTrue(EDITOR_ID_RANGE);
	}

	protected void addContent(ElementNode parent) {
		if(isLayoutHorizontal()) {
			ElementNode div = parent.addChild(TAG_DIV);
			div.addAttribute(ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
			parent = div;
		}
		String type = page.getEditorValue(EDITOR_ID_TEXT_TYPE);
		boolean isTextArea = TYPE_TEXTAREA.equals(type);
		
		String id = getID(type + "-");
		ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
		ElementNode input = isTextArea ? parent.addChild(TYPE_TEXTAREA, page.getEditorValue(EDITOR_ID_VALUE)) : parent.addChild(TAG_INPUT);

		label.addAttribute(ATTR_FOR, id);
		input.addAttribute(ATTR_NAME, id);
		input.addAttribute(ATTR_ID, id);

		if(isTrue(EDITOR_ID_HIDE_LABEL)) {
			if(isLayoutHorizontal()) {
				parent.addAttribute(ATTR_CLASS, CLASS_HIDE_LABEL);
			} else {
				label.addAttribute(ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
			}
		}
	
		if(isTrue(EDITOR_ID_CLEAR_INPUT)) {
			input.addAttribute(ATTR_DATA_CLEAR_BTN, TRUE);
		}

		if(isMini()) {
			input.addAttribute(ATTR_DATA_MINI, TRUE);
		}

		if(isTrue(EDITOR_ID_DISABLED)) {
			input.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(!TYPE_TEXTAREA.equals(type)) {
			input.addAttribute(ATTR_DATA_VALUE, page.getEditorValue(EDITOR_ID_VALUE));
		}
		
		addAttributeIfNotEmpty(input, ATTR_PLACEHOLDER, EDITOR_ID_PLACEHOLDER);
	
		if(TYPE_NUMBER.equals(type)) {
			addAttributeIfNotEmpty(input, ATTR_DATA_MIN, EDITOR_ID_MIN);
			addAttributeIfNotEmpty(input, ATTR_DATA_MAX, EDITOR_ID_MAX);
			addAttributeIfNotEmpty(input, ATTR_DATA_STEP, EDITOR_ID_STEP);
		}

		addAttributeIfNotEmpty(input, ATTR_PATTERN, EDITOR_ID_PATTERN);
		addAttributeIfNotEmpty(input, ATTR_MAXLENGTH, EDITOR_ID_MAXLENGTH);
		if(isTrue(EDITOR_ID_AUTOFOCUS)) {
			input.addAttribute(ATTR_AUTOFOCUS, TRUE);
		}
		if(isTrue(EDITOR_ID_REQUIRED)) {
			input.addAttribute(ATTR_REQUIRED, TRUE);
		}

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			input.addAttribute(ATTR_DATA_THEME, themeValue);
		}

		if(!TYPE_TEXTAREA.equals(type)) {
			input.addAttribute(ATTR_TYPE, type);
		} else {
			input.addAttribute("cols", "40");
			input.addAttribute("rows", "8");
		}

	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}
