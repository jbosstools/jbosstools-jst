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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTextInputWizard extends NewHTMLWidgetWizard<NewTextInputWizardPage> implements HTMLConstants {
	static String prefix = "input-";

	public NewTextInputWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.TEXT_INPUT_IMAGE));
	}

	@Override
	protected NewTextInputWizardPage createPage() {
		return new NewTextInputWizardPage();
	}

	String getTextInputID() {
		return getID(prefix);
	}

	@Override
	protected void addContent(ElementNode parent) {
		String type = page.getEditorValue(JQueryConstants.EDITOR_ID_TEXT_TYPE);
		boolean isTextArea = JQueryConstants.TYPE_TEXTAREA.equals(type);
		
		String id = getID(prefix);
		
		String labelText = page.getEditorValue(JQueryConstants.EDITOR_ID_LABEL);
		if(labelText.length() > 0) {
			ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(JQueryConstants.EDITOR_ID_LABEL));
			label.addAttribute(ATTR_FOR, id);
		}
		ElementNode input = isTextArea ? parent.addChild(JQueryConstants.TYPE_TEXTAREA, page.getEditorValue(JQueryConstants.EDITOR_ID_VALUE)) : parent.addChild(TAG_INPUT);
		input.addAttribute(ATTR_ID, id);

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

		addAttributeIfNotEmpty(input, JQueryConstants.ATTR_PATTERN, JQueryConstants.ATTR_PATTERN);
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
			input.addAttribute(ATTR_COLS, "40");
			input.addAttribute(ATTR_ROWS, "8");
		}

		addAttributeIfNotEmpty(input, ATTR_LIST, ATTR_LIST);
		
		ElementNode datalistNode = page.getDatalistNode();
		if(datalistNode != null) {
			parent.getChildren().add(datalistNode);
		}

	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		
		addContent(div);
	}
	
}
