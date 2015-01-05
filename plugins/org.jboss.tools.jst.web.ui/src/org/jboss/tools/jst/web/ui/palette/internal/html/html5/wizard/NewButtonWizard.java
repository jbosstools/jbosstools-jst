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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewButtonWizard extends NewHTMLWidgetWizard<NewButtonWizardPage> implements HTMLConstants {

	public NewButtonWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.FORM_BUTTON_IMAGE));
	}

	protected NewButtonWizardPage createPage() {
		return new NewButtonWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String value = page.getEditorValue(JQueryConstants.EDITOR_ID_VALUE);
		String type = page.getEditorValue(JQueryConstants.EDITOR_ID_FORM_BUTTON_TYPE);
		if(value.length() == 0) {
			if(BUTTON_TYPE_BUTTON.equals(type)) {
				value = "Input";
			} else if(BUTTON_TYPE_RESET.equals(type)) {
				value = WizardMessages.buttonTypeResetLabel;
			} else if(BUTTON_TYPE_SUBMIT.equals(type)) {
				value = WizardMessages.buttonTypeSubmitLabel;
			}
		}

		ElementNode input = parent.addChild(TAG_INPUT);
		addAttributeIfNotEmpty(input, ATTR_TYPE, JQueryConstants.EDITOR_ID_FORM_BUTTON_TYPE);

		input.addAttribute(ATTR_VALUE, value);
		addAttributeIfNotEmpty(input, ATTR_FORM, ATTR_FORM);

		if(isTrue(JQueryConstants.EDITOR_ID_DISABLED)) {
			input.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}

		if(isTrue(JQueryConstants.EDITOR_ID_AUTOFOCUS)) {
			input.addAttribute(JQueryConstants.ATTR_AUTOFOCUS, JQueryConstants.ATTR_AUTOFOCUS);
		}
		addID("button-", input);
		
		boolean isSubmit = (BUTTON_TYPE_SUBMIT.equals(type));
		if(isSubmit) {
			addAttributeIfNotEmpty(input, ATTR_FORM_ACTION, ATTR_FORM_ACTION);
			String method = page.getEditorValue(ATTR_FORM_METHOD);
			if(!HTMLFieldEditorFactory.INHERITED_FROM_FORM.equals(method)) {
				addAttributeIfNotEmpty(input, ATTR_FORM_METHOD, ATTR_FORM_METHOD);
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
