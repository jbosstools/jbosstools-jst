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
public class NewTextInputWizard extends NewIonicWidgetWizard<NewTextInputWizardPage> implements IonicConstants {

	public NewTextInputWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TEXT_INPUT_IMAGE));
	}

	@Override
	protected NewTextInputWizardPage createPage() {
		return new NewTextInputWizardPage();
	}

	protected boolean isRange() {
		return isTrue(JQueryConstants.EDITOR_ID_RANGE);
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		if(browser) {
			ElementNode form = getFormNode(parent);
			ElementNode div = form.addChild(TAG_DIV);
			div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
//			div.addAttribute(ATTR_CLASS, "list");
			parent = div;
		}

		String style = page.getEditorValue(EDITOR_ID_INPUT_LABEL_STYLE);

		String type = page.getEditorValue(JQueryConstants.EDITOR_ID_TEXT_TYPE);
		boolean isTextArea = JQueryConstants.TYPE_TEXTAREA.equals(type);
		
//		String id = getID(type + "-");
		ElementNode label = parent.addChild(TAG_LABEL);
		String labelClass = getLabelClass(style);
		if(labelClass.length() > 0) {
			label.addAttribute(ATTR_CLASS, labelClass);
		}
		
		String labelText = page.getEditorValue(JQueryConstants.EDITOR_ID_LABEL);
		if(labelText.length() > 0) {
			ElementNode span = label.addChild("span", labelText);
			span.addAttribute(ATTR_CLASS, "input-label");
		}
		ElementNode input = isTextArea ? label.addChild(JQueryConstants.TYPE_TEXTAREA, page.getEditorValue(JQueryConstants.EDITOR_ID_VALUE)) : label.addChild(TAG_INPUT);
		addAttributeIfNotEmpty(input, ATTR_NG_MODEL, ATTR_NG_MODEL);

//		if(id != null) {
//			input.addAttribute(ATTR_NAME, id);
//			input.addAttribute(ATTR_ID, id);
//		}
		addAttributeIfNotEmpty(input, ATTR_NAME, ATTR_NAME);

		if(isTrue(JQueryConstants.EDITOR_ID_DISABLED)) {
			input.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		if(!JQueryConstants.TYPE_TEXTAREA.equals(type)) {
			addAttributeIfNotEmpty(input, ATTR_VALUE, JQueryConstants.EDITOR_ID_VALUE);
		}
		
		addAttributeIfNotEmpty(input, JQueryConstants.ATTR_PLACEHOLDER, JQueryConstants.EDITOR_ID_PLACEHOLDER);
	
		if(JQueryConstants.TYPE_NUMBER.equals(type)) {
			addAttributeIfNotEmpty(input, JQueryConstants.ATTR_DATA_MIN, JQueryConstants.EDITOR_ID_MIN);
			addAttributeIfNotEmpty(input, JQueryConstants.ATTR_DATA_MAX, JQueryConstants.EDITOR_ID_MAX);
			addAttributeIfNotEmpty(input, JQueryConstants.ATTR_DATA_STEP, JQueryConstants.EDITOR_ID_STEP);
		}

		addAttributeIfNotEmpty(input, ATTR_NG_PATTERN, JQueryConstants.EDITOR_ID_PATTERN);
		addAttributeIfNotEmpty(input, JQueryConstants.ATTR_MAXLENGTH, JQueryConstants.EDITOR_ID_MAXLENGTH);
		if(isTrue(JQueryConstants.EDITOR_ID_AUTOFOCUS)) {
			input.addAttribute(JQueryConstants.ATTR_AUTOFOCUS, TRUE);
		}
		if(isTrue(JQueryConstants.EDITOR_ID_REQUIRED)) {
			input.addAttribute(JQueryConstants.ATTR_REQUIRED, TRUE);
		}

		if(!JQueryConstants.TYPE_TEXTAREA.equals(type)) {
			input.addAttribute(ATTR_TYPE, type);
		} else {
			input.addAttribute("cols", "40");
			input.addAttribute("rows", "8");
		}

	}

	String getLabelClass(String style) {
		if(style.length() == 0) {
			return "";
		}
		StringBuilder cls = new StringBuilder();
		addClass(cls, "item");
		addClass(cls, "item-input");
		if("stacked".equals(style)) {
			addClass(cls, "item-stacked-label");
		} else if("floating".equals(style)) {
			addClass(cls, "item-floating-label");
		}
		return cls.toString();
	}

}
